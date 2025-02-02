package com.wafflestudio.interpark

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
import com.wafflestudio.interpark.user.persistence.UserRole
import org.hamcrest.Matchers
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
class PaginationTest
@Autowired
constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    private lateinit var userAccessToken: String
    private lateinit var performanceId: String

    @BeforeEach
    fun setUp() {
        val username = UUID.randomUUID().toString().take(8)
        val password = "password123"

        // 1️⃣ 회원가입
        // 일반 유저
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
        // 일반 유저
        userAccessToken =
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

        // 3️⃣ 테스트용 공연 ID 반환
        performanceId =
            mvc.perform(
                get("/api/v2/performance/search")
                    .header("Authorization", "Bearer $userAccessToken")
                    .param("title", "지킬앤하이드")
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let {
                    val node = mapper.readTree(it)
                    val firstItem = node.get("data").firstOrNull() ?: error("Response array is empty")
                    val idNode = firstItem.get("id")
                    requireNotNull(idNode) { "ID not found in response item: $firstItem" }
                    idNode.asText()
                }
    }

    @Test
    fun `공연 전체 조회 페이지네이션 테스트`() {
        var cursor: String? = null
        val maxIteration = 15
        var iterations = 0
        var totalItems = 0

        while(iterations < maxIteration) {
            val response = mvc.perform(
                get("/api/v2/performance/search")
                    .apply { cursor?.let { param("cursor", it) } }
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$.data.size()").value(Matchers.greaterThan(0)))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }

            val hasNext = response.get("hasNext").asBoolean()
            val dataSize = response.get("data").size()
            totalItems += dataSize
            if(!hasNext) {
                break
            }
            cursor = response.get("nextCursor").asText()

            iterations++
        }

        // 전체 데이터를 다 가져왔는지 확인
        val totalSize = mvc.perform(
            get("/api/v1/performance/search")
                .contentType(MediaType.APPLICATION_JSON),
        ).andExpect(status().`is`(200))
            .andReturn()
            .response
            .getContentAsString(Charsets.UTF_8)
            .let { mapper.readTree(it).size() }

        assert( totalItems == totalSize ) {"Expected $totalSize items but got $totalItems"}
    }

    @Test
    fun `공연 일부 조회 페이지네이션 테스트`() {
        var cursor: String? = null
        val maxIteration = 15
        var iterations = 0
        var totalItems = 0

        while(iterations < maxIteration) {
            val response = mvc.perform(
                get("/api/v2/performance/search")
                    .param("category", PerformanceCategory.CONCERT.name)
                    .apply { cursor?.let { param("cursor", it) } }
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }

            val hasNext = response.get("hasNext").asBoolean()
            val dataSize = response.get("data").size()
            totalItems += dataSize
            if(!hasNext) {
                break
            }
            cursor = response.get("nextCursor").asText()

            iterations++
        }

        // 전체 데이터를 다 가져왔는지 확인
        val totalSize = mvc.perform(
            get("/api/v1/performance/search")
                .param("category", PerformanceCategory.CONCERT.name)
                .contentType(MediaType.APPLICATION_JSON),
        ).andExpect(status().`is`(200))
            .andReturn()
            .response
            .getContentAsString(Charsets.UTF_8)
            .let { mapper.readTree(it).size() }

        assert( totalItems == totalSize ) {"Expected $totalSize items but got $totalItems"}
    }

    @Test
    fun `잘못된 커서로 요청하면 오류`() {
        mvc.perform(
            get("/api/v2/performance/search")
                .apply { param("cursor", "WrongCursor") }
                .contentType(MediaType.APPLICATION_JSON),
        ).andExpect(status().`is`(400))
    }

    @Test
    fun `공연의 리뷰 조회 페이지네이션 테스트`() {
        val reviewId1 =
            mvc.perform(
                post("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $userAccessToken")
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

        (1..5).forEach {
            mvc.perform(
                post("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $userAccessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "rating" to it,
                                "title" to "Great Performance! $it",
                                "content" to "Absolutely amazing. Highly recommend!",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(201))
        }

        val response = mvc.perform(
            get("/api/v2/performance/$performanceId/review")
        ).andExpect(status().`is`(200))
            .andReturn()
            .response
            .getContentAsString(Charsets.UTF_8)
            .let { mapper.readTree(it) }

        val hasNext = response.get("hasNext").asBoolean()
        assert(hasNext) {"expected hasNext true but false"}

        val cursor = response.get("nextCursor").asText()
        // 가장 먼저 등록한 리뷰가 가장 마지막에 조회된다
        mvc.perform(
            get("/api/v2/performance/$performanceId/review")
                .apply { param("cursor", cursor) }
        ).andExpect(status().`is`(200))
            .andExpect(jsonPath("$.data[?(@.id == '$reviewId1')]").exists())
            .andExpect(jsonPath("$.hasNext").value(false))
    }

    @Test
    fun `리뷰의 댓글 조회 페이지네이션 테스트`() {
        val reviewId =
            mvc.perform(
                post("/api/v1/performance/$performanceId/review")
                    .header("Authorization", "Bearer $userAccessToken")
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

        val replyId1 =
            mvc.perform(
                post("/api/v1/review/$reviewId/reply")
                    .header("Authorization", "Bearer $userAccessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf("content" to "First Reply"),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(201))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it).get("id").asText() }

        (1..5).forEach {
            mvc.perform(
                post("/api/v1/review/$reviewId/reply")
                    .header("Authorization", "Bearer $userAccessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf("content" to "$it"),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(201))
                .andReturn()
        }

        val response = mvc.perform(
            get("/api/v2/review/$reviewId/reply")
        ).andExpect(status().`is`(200))
            .andReturn()
            .response
            .getContentAsString(Charsets.UTF_8)
            .let { mapper.readTree(it) }

        val hasNext = response.get("hasNext").asBoolean()
        assert(hasNext) {"expected hasNext true but false"}

        val cursor = response.get("nextCursor").asText()
        // 가장 먼저 등록한 댓글이 가장 마지막에 조회된다
        mvc.perform(
            get("/api/v2/review/$reviewId/reply")
                .apply { param("cursor", cursor) }
        ).andExpect(status().`is`(200))
            .andExpect(jsonPath("$.data[?(@.id == '$replyId1')]").exists())
            .andExpect(jsonPath("$.hasNext").value(false))
    }
}
