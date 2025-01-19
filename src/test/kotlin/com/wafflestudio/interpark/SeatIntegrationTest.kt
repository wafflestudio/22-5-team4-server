package com.wafflestudio.interpark

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.interpark.seat.service.SeatCreationService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class SeatIntegrationTest
@Autowired
constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
    private val seatCreationService: SeatCreationService,
) {
    private lateinit var accessToken: String
    private lateinit var performanceHallId: String
    private lateinit var performanceId: String
    private lateinit var performanceEventId: String
    @BeforeEach
    fun setup() {
        val username = UUID.randomUUID().toString().take(8)
        val password = "password123"

        // 1️⃣ 회원가입
        mvc.perform(
            post("/api/v1/signup")
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
        accessToken =
            mvc.perform(
                post("/api/v1/signin")
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
        performanceHallId =
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
        performanceId =
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
                            "startAt" to LocalDateTime.now(ZoneId.of("Asia/Seoul")),
                            "endAt" to LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                        ),
                    ),
                )
            .contentType(MediaType.APPLICATION_JSON)
        )

        performanceEventId =
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
    }

    @Test
    fun `가능한 좌석들의 정보를 받을 수 있다`() {
        mvc.perform(
            get("/api/v1/seat/$performanceEventId/available")
        ).andExpect(status().`is`(200))
    }

    @Test
    fun `좌석을 예매할 수 있다`() {
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
        mvc.perform(
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
        ).andExpect(status().`is`(200))
        //한번 예매된 좌석은 예매되지 않는다
        mvc.perform(
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
        ).andExpect(status().`is`(409))
    }

    @Test
    fun `좌석을 취소할 수 있다`() {
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
        mvc.perform(
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
        ).andExpect(status().`is`(200))

        mvc.perform(
            post("/api/v1/reservation/cancel")
                .content(
                    mapper.writeValueAsString(
                        mapOf(
                            "reservationId" to reservationId,
                        ),
                    ),
                )
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().`is`(204))

        //좌석을 취소하고 나면 다시 예매할 수 있다
        mvc.perform(
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
        ).andExpect(status().`is`(200))
    }

    @Test
    fun `다른 사람은 좌석을 취소할 수 없다`() {
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

        mvc.perform(
            post("/api/v1/signup")
                .content(
                    mapper.writeValueAsString(
                        mapOf(
                            "username" to "correct2",
                            "password" to "12345678",
                            "nickname" to "examplename",
                            "phoneNumber" to "010-0000-0000",
                            "email" to "test@example.com",
                        ),
                    ),
                )
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().`is`(200))
        val otherAccessToken =
            mvc.perform(
                post("/api/v1/signin")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "correct2",
                                "password" to "12345678",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it).get("accessToken").asText() }

        mvc.perform(
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
        ).andExpect(status().`is`(200))

        mvc.perform(
            post("/api/v1/reservation/cancel")
                .content(
                    mapper.writeValueAsString(
                        mapOf(
                            "reservationId" to reservationId,
                        ),
                    ),
                )
                .header("Authorization", "Bearer $otherAccessToken")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().`is`(401))
    }
}