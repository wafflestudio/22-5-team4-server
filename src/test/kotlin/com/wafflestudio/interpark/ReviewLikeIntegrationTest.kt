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
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ReviewLikeIntegrationTest
@Autowired
constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    private lateinit var accessToken: String
    private lateinit var otherAccessToken: String
    private lateinit var performanceId: String
    private lateinit var reviewId: String

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

        //다른 사용자로도 좋아요가 되는지 체크
        mvc.perform(
            post("/api/v1/local/signup")
                .content(
                    mapper.writeValueAsString(
                        mapOf(
                            "username" to "otherUser1",
                            "password" to "goodPassword",
                            "nickname" to "otherUser1",
                            "phoneNumber" to "010-1234-5678",
                            "email" to "other@example.com",
                        ),
                    ),
                )
                .contentType(MediaType.APPLICATION_JSON),
        ).andExpect(status().`is`(200))

        otherAccessToken =
            mvc.perform(
                post("/api/v1/local/signin")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "otherUser1",
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

        //테스트 용으로 아무 공연 Id를 하나 가져온다
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
    }

    @Test
    fun `좋아요를 하면 좋아요 수가 증가한다`() {
        mvc.perform(
            post("/api/v1/review/$reviewId/like")
                .header("Authorization", "Bearer $accessToken")
        ).andExpect(status().`is`(204))

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
        assert(targetReview!!.get("likeCount").asText() == "1") { "expected 1 likeCount but ${targetReview!!.get("likeCount").asText()}"}

        mvc.perform(
            post("/api/v1/review/$reviewId/like")
                .header("Authorization", "Bearer $otherAccessToken")
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
        assert(targetReview!!.get("likeCount").asText() == "2") { "expected 2 likeCount but ${targetReview!!.get("likeCount").asText()}"}
    }

    @Test
    fun `한 사람이 좋아요를 여러 번 눌러도 좋아요는 1만 증가한다`() {
        mvc.perform(
            post("/api/v1/review/$reviewId/like")
                .header("Authorization", "Bearer $accessToken")
        ).andExpect(status().`is`(204))

        mvc.perform(
            post("/api/v1/review/$reviewId/like")
                .header("Authorization", "Bearer $accessToken")
        ).andExpect(status().`is`(204))

        mvc.perform(
            post("/api/v1/review/$reviewId/like")
                .header("Authorization", "Bearer $accessToken")
        ).andExpect(status().`is`(204))

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
        assert(targetReview!!.get("likeCount").asText() == "1") { "expected 1 likeCount but ${targetReview!!.get("likeCount").asText()}"}
    }

    @Test
    fun `좋아요를 취소하면 좋아요 수가 줄어든다`() {
        mvc.perform(
            post("/api/v1/review/$reviewId/like")
                .header("Authorization", "Bearer $accessToken")
        ).andExpect(status().`is`(204))

        mvc.perform(
            delete("/api/v1/review/$reviewId/like")
                .header("Authorization", "Bearer $accessToken")
        ).andExpect(status().`is`(204))

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
        assert(targetReview!!.get("likeCount").asText() == "0") { "expected 0 likeCount but ${targetReview!!.get("likeCount").asText()}"}

        mvc.perform(
            post("/api/v1/review/$reviewId/like")
                .header("Authorization", "Bearer $accessToken")
        ).andExpect(status().`is`(204))

        mvc.perform(
            delete("/api/v1/review/$reviewId/like")
                .header("Authorization", "Bearer $otherAccessToken")
        ).andExpect(status().`is`(204))

        // otherUser은 좋아요를 누르지 않았으니 취소해도 좋아요가 줄어들지 않는다
        // 좋아요를 누르지 않은 채로 취소해도 버그는 나지 않는다
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
        assert(targetReview!!.get("likeCount").asText() == "1") { "expected 1 likeCount but ${targetReview!!.get("likeCount").asText()}"}
    }
}
