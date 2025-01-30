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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ReplyIntegrationTest
    @Autowired
    constructor(
        private val mvc: MockMvc,
        private val mapper: ObjectMapper,
    ) {
        private lateinit var accessToken: String
        private lateinit var performanceId: String
        private lateinit var reviewId: String
        private lateinit var replyId: String

        @BeforeEach
        fun setUp() {
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
                                "nickname" to "test_user",
                                "phoneNumber" to "010-0000-0000",
                                "email" to "test@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))

            // 2️⃣ 로그인 → 토큰 획득
            accessToken =
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

            //테스트 용으로 아무 공연 Id를 하나 가져온다
            performanceId =
                mvc.perform(
                    get("/api/v2/performance/search")
                ).andExpect(status().`is`(200))
                    .andReturn()
                    .response
                    .getContentAsString(Charsets.UTF_8)
                    .let {
                        val performances = mapper.readTree(it).get("data")
                        performances[0].get("id").asText()
                    }

            // 3️⃣ 리뷰 생성 (테스트용)
            reviewId =
                mvc.perform(
                    post("/api/v1/performance/$performanceId/review")
                        .header("Authorization", "Bearer $accessToken")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "rating" to 5,
                                    "title" to "Amazing Show!",
                                    "content" to "Loved every bit of it!",
                                ),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(201))
                    .andReturn()
                    .response
                    .getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("id").asText() }
        }

        @Test
        fun `댓글 작성, 조회, 수정, 삭제 플로우 테스트`() {
            // 4️⃣ 댓글 작성 (성공)
            replyId =
                mvc.perform(
                    post("/api/v1/review/$reviewId/reply")
                        .header("Authorization", "Bearer $accessToken")
                        .content(
                            mapper.writeValueAsString(
                                mapOf("content" to "Great review! I totally agree."),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(201))
                    .andReturn()
                    .response
                    .getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("id").asText() }

            // 5️⃣ 댓글 조회 (성공)
            mvc.perform(
                get("/api/v1/review/$reviewId/reply")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$[?(@.id == '$replyId')]").exists()) // replyId가 포함된 객체가 존재하는지 확인

            // 6️⃣ 댓글 수정 (성공)
            mvc.perform(
                put("/api/v1/reply/$replyId")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf("content" to "Actually, I have a different opinion now."),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$.content").value("Actually, I have a different opinion now."))

            // 7️⃣ 댓글 삭제 (성공)
            mvc.perform(
                delete("/api/v1/reply/$replyId")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(204))

            // 8️⃣ 댓글 삭제 이후 조회 시 존재하지 않아야 함
            mvc.perform(
                get("/api/v1/review/$reviewId/reply")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$[?(@.id == '$replyId')]").doesNotExist()) // replyId가 포함된 객체가 존재하지 않는지 확인
        }

        @Test
        fun `댓글 작성 실패 - 빈 content`() {
            mvc.perform(
                post("/api/v1/review/$reviewId/reply")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf("content" to ""),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(400))
                .andExpect(jsonPath("$.error").value("Empty Or Too Long Content"))
        }

        @Test
        fun `댓글 수정 실패 - 댓글이 존재하지 않음`() {
            val fakeReplyId = "non-existent-reply"

            mvc.perform(
                put("/api/v1/reply/$fakeReplyId")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf("content" to "This should fail."),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(404))
                .andExpect(jsonPath("$.error").value("Reply Not Found"))
        }

        @Test
        fun `댓글 삭제 실패 - 권한 없음`() {
            // 댓글 생성
            replyId =
                mvc.perform(
                    post("/api/v1/review/$reviewId/reply")
                        .header("Authorization", "Bearer $accessToken")
                        .content(
                            mapper.writeValueAsString(
                                mapOf("content" to "I'm not gonna delete this reply."),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(201))
                    .andReturn()
                    .response
                    .getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("id").asText() }

            // 다른 사용자 생성
            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "hacker",
                                "password" to "badPassword",
                                "nickname" to "h4x0r",
                                "phoneNumber" to "010-1111-2222",
                                "email" to "hacker@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))

            // 다른 사용자 로그인
            val otherAccessToken =
                mvc.perform(
                    post("/api/v1/local/signin")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "username" to "hacker",
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

            // 기존 사용자가 작성한 댓글을 다른 사용자가 삭제 시도 (권한 없음)
            mvc.perform(
                delete("/api/v1/reply/$replyId")
                    .header("Authorization", "Bearer $otherAccessToken"),
            ).andExpect(status().`is`(401))
                .andExpect(jsonPath("$.error").value("Unauthorized Access To Reply"))
        }

        @Test
        fun `댓글을 달면 댓글수가 증가한다`() {
            var reviews = mvc.perform(
                get("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$[?(@.id == '$reviewId')]").exists())
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }
            var targetReview = reviews.find { it.get("id").asText() == reviewId }
            assert(targetReview!!.get("replyCount").asText() == "0") { "expected 0 replyCount but ${targetReview!!.get("reply").asText()}"}

            mvc.perform(
                post("/api/v1/review/$reviewId/reply")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf("content" to "Great review! I totally agree."),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(201))

            reviews = mvc.perform(
                get("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$[?(@.id == '$reviewId')]").exists())
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }
            targetReview = reviews.find { it.get("id").asText() == reviewId }
            assert(targetReview!!.get("replyCount").asText() == "1") { "expected 1 replyCount but ${targetReview!!.get("replyCount").asText()}"}
        }

        @Test
        fun `댓글은 여러 개를 달 수 있다`() {
            mvc.perform(
                post("/api/v1/review/$reviewId/reply")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf("content" to "Great review! I totally agree."),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(201))

            mvc.perform(
                post("/api/v1/review/$reviewId/reply")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf("content" to "I can't stop thinking about this review! I totally agree again."),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(201))

            mvc.perform(
                post("/api/v1/review/$reviewId/reply")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf("content" to "Again and Again and Again"),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(201))

            val reviews = mvc.perform(
                get("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$[?(@.id == '$reviewId')]").exists())
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }
            val targetReview = reviews.find { it.get("id").asText() == reviewId }
            assert(targetReview!!.get("replyCount").asText() == "3") { "expected 3 replyCount but ${targetReview!!.get("replyCount").asText()}"}
        }

        @Test
        fun `댓글을 지우면 댓글수가 줄어든다`() {
            replyId =
                mvc.perform(
                    post("/api/v1/review/$reviewId/reply")
                        .header("Authorization", "Bearer $accessToken")
                        .content(
                            mapper.writeValueAsString(
                                mapOf("content" to "Great review! I totally agree."),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(201))
                    .andReturn()
                    .response
                    .getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("id").asText() }

            var reviews = mvc.perform(
                get("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$[?(@.id == '$reviewId')]").exists())
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }
            var targetReview = reviews.find { it.get("id").asText() == reviewId }
            assert(targetReview!!.get("replyCount").asText() == "1") { "expected 1 replyCount but ${targetReview!!.get("replyCount").asText()}"}

            mvc.perform(
                delete("/api/v1/reply/$replyId")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(204))

            reviews = mvc.perform(
                get("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $accessToken"),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$[?(@.id == '$reviewId')]").exists())
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }
            targetReview = reviews.find { it.get("id").asText() == reviewId }
            assert(targetReview!!.get("replyCount").asText() == "0") { "expected 0 replyCount but ${targetReview!!.get("likeCount").asText()}"}

        }

        @Test
        fun `GET을 했을 때 댓글을 최신순으로 정렬되어 반환된다`() {
            val otherAccessTokens = (1..5).map { num ->
                mvc.perform(
                    post("/api/v1/local/signup")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "username" to "otherMan2$num",
                                    "password" to "goodPassword",
                                    "nickname" to "NICKNAME",
                                    "phoneNumber" to "010-1234-5678",
                                    "email" to "hacker@example.com",
                                ),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(200))

                mvc.perform(
                    post("/api/v1/local/signin")
                        .content(
                            mapper.writeValueAsString(
                                mapOf(
                                    "username" to "otherMan2$num",
                                    "password" to "goodPassword",
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
            (1..5).forEach {
                mvc.perform(
                    post("/api/v1/review/$reviewId/reply")
                        .header("Authorization", "Bearer ${otherAccessTokens[it-1]}")
                        .content(
                            mapper.writeValueAsString(
                                mapOf("content" to "$it"),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(201))
                    .andReturn()
            }

            (1..5).forEach {
                mvc.perform(
                    post("/api/v1/review/$reviewId/reply")
                        .header("Authorization", "Bearer $accessToken")
                        .content(
                            mapper.writeValueAsString(
                                mapOf("content" to "$it"),
                            ),
                        )
                        .contentType(MediaType.APPLICATION_JSON),
                ).andExpect(status().`is`(201))
                    .andReturn()
            }

            val reviewReplies = mvc.perform(
                get("/api/v1/review/$reviewId/reply")
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }
                .map { LocalDateTime.parse(it.get("createdAt").asText()) }
            val isReviewReplySorted = reviewReplies.zipWithNext { a,b -> a>=b }.all {it}
            assert (isReviewReplySorted) { "expected review rating sorted but not" }

            val userReplies = mvc.perform(
                get("/api/v1/me/reply")
                    .header("Authorization", "Bearer $accessToken")
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }
                .map { LocalDateTime.parse(it.get("createdAt").asText()) }
            val isUserReplySorted = userReplies.zipWithNext { a,b -> a >=b }.all {it}
            assert (isUserReplySorted) { "expected user reply sorted but not" }
        }
    }
