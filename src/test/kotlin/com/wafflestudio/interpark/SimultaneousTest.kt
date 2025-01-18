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

        //Seat와 Reservation 만들기 위한 EventId 만들기
        val performanceHallId =
            mvc.perform(
                get("/api/v1/performance-hall")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let {
                    val performanceHalls = mapper.readTree(it)
                    performanceHalls[0].get("id").asText()
                }
        val performanceId =
            mvc.perform(
                get("/api/v1/performance/search")
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let {
                    val performances = mapper.readTree(it)
                    performances[0].get("id").asText()
                }

        mvc.perform(
            post("/admin/v1/performance-event")
                .content(
                    mapper.writeValueAsString(
                        mapOf(
                            "performanceId" to performanceId,
                            "performanceHallId" to performanceHallId,
                            "startAt" to Instant.now(),
                            "endAt" to Instant.now(),
                        ),
                    ),
                )
                .contentType(MediaType.APPLICATION_JSON)
        )

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
        //Seat와 Reservation만들기
        seatCreationService.createSeats(performanceHallId, "DEFAULT")
        seatCreationService.createEmptyReservations(performanceEventId)

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
        var successCnt = AtomicInteger(0)
        var conflictCnt = AtomicInteger(0)
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
}