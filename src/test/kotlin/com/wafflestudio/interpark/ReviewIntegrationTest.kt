package com.wafflestudio.interpark.review

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ReviewIntegrationTest
    @Autowired
    constructor(
        private val mvc: MockMvc,
        private val mapper: ObjectMapper,
    ) {
        private lateinit var accessToken: String
        private lateinit var performanceId: String
        private lateinit var reviewId: String

        @BeforeEach
        fun setUp() {
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

            performanceId = "sample-performance" // 가상의 공연 ID
        }

        @Test
        fun `리뷰 작성, 조회, 수정, 삭제 플로우 테스트`() {
            // 3️⃣ 리뷰 작성 (성공)
            reviewId =
                mvc.perform(
                    post("/api/v1/performance/$performanceId/review")
                        .header("Authorization", "Bearer $accessToken")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "rating" to 5,
                                    "title" to "Great Performance!",
                                    "content" to "Absolutely amazing. Highly recommend!",
                                ),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(201))
                    .andReturn()
                    .response
                    .getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("id").asText() }

            // 4️⃣ 리뷰 조회 (성공)
            mvc.perform(
                get("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$[?(@.id == '$reviewId')]").exists()) // reviewId가 포함된 객체가 존재하는지 확인

            // 5️⃣ 리뷰 수정 (성공)
            mvc.perform(
                put("/api/v1/review/$reviewId")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "rating" to 4,
                                "title" to "Still great, but...",
                                "content" to "I loved it, but the ending could be better.",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$.rating").value(4))

            // 6️⃣ 리뷰 삭제 (성공)
            mvc.perform(
                delete("/api/v1/review/$reviewId")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(204))

            // 7️⃣ 리뷰 삭제 이후 0건 조회
            mvc.perform(
                get("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$[?(@.id == '$reviewId')]").doesNotExist()) // reviewId가 포함된 객체가 존재하는지 확인
        }

        @Test
        fun `리뷰 작성 실패 - 잘못된 rating`() {
            mvc.perform(
                post("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "rating" to 10,
                                "title" to "Invalid rating",
                                "content" to "Should fail",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(400))
                .andExpect(jsonPath("$.error").value("Rating Out Of Range"))
        }

        @Test
        fun `리뷰 수정 실패 - 리뷰가 존재하지 않음`() {
            val fakeReviewId = "non-existent-review"

            mvc.perform(
                put("/api/v1/review/$fakeReviewId")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "rating" to 3,
                                "title" to "Unknown review",
                                "content" to "This should not be found.",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andDo(print()) // ✅ 콘솔에 응답 JSON 출력
                .andExpect(status().`is`(404))
                .andExpect(jsonPath("$.error").value("Review Not Found"))
        }

        @Test
        fun `리뷰 삭제 실패 - 권한 없음`() {
            // 다른 사용자 생성
            mvc.perform(
                post("/api/v1/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "hacker2",
                                "password" to "badPassword",
                                "nickname" to "h4x0r",
                                "phoneNumber" to "010-1111-2222",
                                "email" to "hacker@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))

            // 다른 사용자로 로그인
            val otherAccessToken =
                mvc.perform(
                    post("/api/v1/signin")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "username" to "hacker2",
                                    "password" to "badPassword",
                                ),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(200))
                    .andReturn()
                    .response
                    .getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("accessToken").asText() }

            // 리뷰 작성 (성공)
            reviewId =
                mvc.perform(
                    post("/api/v1/performance/$performanceId/review")
                        .header("Authorization", "Bearer $accessToken")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "rating" to 5,
                                    "title" to "This is my review",
                                    "content" to "Don't touch!",
                                ),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(201))
                    .andReturn()
                    .response
                    .getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("id").asText() }

            // 원래 유저가 작성한 리뷰를 삭제하려고 시도
            mvc.perform(
                delete("/api/v1/review/$reviewId")
                    .header("Authorization", "Bearer $otherAccessToken"),
            ).andExpect(status().`is`(401))
                .andExpect(jsonPath("$.error").value("Unauthorized Access To Review"))
        }
    }
