package com.wafflestudio.interpark

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.interpark.seat.service.SeatCreationService
import com.wafflestudio.interpark.user.persistence.UserRepository
import com.wafflestudio.interpark.user.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimultaneousTest
@Autowired
constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
    private val seatCreationService: SeatCreationService,
    private val userRepository: UserRepository,
) {
    @Test
    fun `한 예매에 동시에 여러 접근이 있다면 하나만 통과시킨다`() {
        val threadPool = Executors.newFixedThreadPool(10)
        val username = UUID.randomUUID().toString().take(8)
        val password = "password123"

        // 1️⃣ 회원가입
        mvc.perform(
            post("/api/v1/local/signup")
                .content(
                    mapper.writeValueAsString(
                        mapOf(
                            "username" to username,
                            "password" to password,
                            "nickname" to "reviewer",
                            "phoneNumber" to "010-0000-0000",
                            "email" to "reviewer@example.com",
                        ),
                    ),
                )
                .contentType(MediaType.APPLICATION_JSON),
        ).andExpect(status().`is`(200))

        // 2️⃣ 로그인 → 토큰 획득
        val accessToken =
            mvc.perform(
                post("/api/v1/local/signin")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to username,
                                "password" to password,
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it).get("accessToken").asText() }

        val performanceEventId =
            mvc.perform(
                get("/api/v1/performance-event")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let {
                    val performanceEvents = mapper.readTree(it)
                    performanceEvents[0].get("id").asText()
                }

        val reservationId = mvc.perform(
            get("/api/v1/seat/$performanceEventId/available")
        ).andExpect(status().`is`(200))
            .andReturn()
            .response
            .getContentAsString(Charsets.UTF_8)
            .let {
                val availableSeats = mapper.readTree(it).get("availableSeats")
                availableSeats[0].get("reservationId").asText()
            }

        val results = mutableListOf<Int>()
        val successCnt = AtomicInteger(0)
        val conflictCnt = AtomicInteger(0)
        val tasks = (1..10).map {
            threadPool.submit {
                val responseStatus = mvc.perform(
                    post("/api/v1/reservation/reserve")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "reservationId" to reservationId,
                                ),
                            ),
                        )
                        .header("Authorization", "Bearer $accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andReturn().response.status
                results.add(responseStatus)
                if (responseStatus == 200) { successCnt.incrementAndGet() }
                if (responseStatus == 409) { conflictCnt.incrementAndGet() }
            }
        }
        tasks.forEach { it.get() }
        assert(successCnt.get() == 1) {"expected 1 success but ${successCnt.get()}"}
        assert(conflictCnt.get() == 9) {"expected 9 conflict but ${conflictCnt.get()}"}
    }

    @Test
    fun `한 예매에 동시에 여러명이 접속해도 하나만 통과한다`() {
        val threadPool = Executors.newFixedThreadPool(10)
        val username = (0..9).map { UUID.randomUUID().toString().take(16) }
        val password = "password123"

        // 1️⃣ 회원가입
        (0..9).map {
            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to username[it],
                                "password" to password,
                                "nickname" to "reviewer",
                                "phoneNumber" to "010-0000-0000",
                                "email" to "reviewer@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
        }

        // 2️⃣ 로그인 → 토큰 획득
        val accessToken =
            (0..9).map {
                mvc.perform(
                    post("/api/v1/local/signin")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "username" to username[it],
                                    "password" to password,
                                ),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(200))
                    .andReturn()
                    .response
                    .getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("accessToken").asText() }
            }

        val performanceEventId =
            mvc.perform(
                get("/api/v1/performance-event")
                    .header("Authorization", "Bearer ${accessToken[0]}"),
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let {
                    val performanceEvents = mapper.readTree(it)
                    performanceEvents[0].get("id").asText()
                }

        val reservationId = mvc.perform(
            get("/api/v1/seat/$performanceEventId/available")
        ).andExpect(status().`is`(200))
            .andReturn()
            .response
            .getContentAsString(Charsets.UTF_8)
            .let {
                val availableSeats = mapper.readTree(it).get("availableSeats")
                availableSeats[0].get("reservationId").asText()
            }

        val results = mutableListOf<Int>()
        val successCnt = AtomicInteger(0)
        val conflictCnt = AtomicInteger(0)
        val tasks = (0..9).map {
            threadPool.submit {
                val responseStatus = mvc.perform(
                    post("/api/v1/reservation/reserve")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "reservationId" to reservationId,
                                ),
                            ),
                        )
                        .header("Authorization", "Bearer ${accessToken[it]}")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andReturn().response.status
                results.add(responseStatus)
                if (responseStatus == 200) { successCnt.incrementAndGet() }
                if (responseStatus == 409) { conflictCnt.incrementAndGet() }
            }
        }
        tasks.forEach { it.get() }
        assert(successCnt.get() == 1) {"expected 1 success but ${successCnt.get()}"}
        assert(conflictCnt.get() == 9) {"expected 9 conflict but ${conflictCnt.get()}"}
    }

    @Test
    fun `동시에 다른 좌석에 접근하면 모두 잘 처리된다`() {
        val threadPool = Executors.newFixedThreadPool(10)
        val username = (0..9).map { UUID.randomUUID().toString().take(15) }
        val password = "password123"

        // 1️⃣ 회원가입
        (0..9).map {
            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to username[it],
                                "password" to password,
                                "nickname" to "reviewer",
                                "phoneNumber" to "010-0000-0000",
                                "email" to "reviewer@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
        }

        // 2️⃣ 로그인 → 토큰 획득
        val accessToken =
            (0..9).map {
                mvc.perform(
                    post("/api/v1/local/signin")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "username" to username[it],
                                    "password" to password,
                                ),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(200))
                    .andReturn()
                    .response
                    .getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("accessToken").asText() }
            }

        val performanceEventId =
            mvc.perform(
                get("/api/v1/performance-event")
                    .header("Authorization", "Bearer ${accessToken[0]}"),
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let {
                    val performanceEvents = mapper.readTree(it)
                    performanceEvents[0].get("id").asText()
                }

        val reservationId = mvc.perform(
            get("/api/v1/seat/$performanceEventId/available")
        ).andExpect(status().`is`(200))
            .andReturn()
            .response
            .getContentAsString(Charsets.UTF_8)
            .let {
                val availableSeats = mapper.readTree(it).get("availableSeats")
                (1..10).map { availableSeats[it].get("reservationId").asText() }
            }

        val results = mutableListOf<Int>()
        val successCnt = AtomicInteger(0)
        val conflictCnt = AtomicInteger(0)
        val tasks = (0..9).map {
            threadPool.submit {
                val responseStatus = mvc.perform(
                    post("/api/v1/reservation/reserve")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "reservationId" to reservationId[it],
                                ),
                            ),
                        )
                        .header("Authorization", "Bearer ${accessToken[it]}")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andReturn().response.status
                results.add(responseStatus)
                if (responseStatus == 200) { successCnt.incrementAndGet() }
                if (responseStatus == 409) { conflictCnt.incrementAndGet() }
            }
        }
        tasks.forEach { it.get() }
        assert(successCnt.get() == 10) {"expected 10 success but ${successCnt.get()}"}
        assert(conflictCnt.get() == 0) {"expected 0 conflict but ${conflictCnt.get()}"}
    }
}