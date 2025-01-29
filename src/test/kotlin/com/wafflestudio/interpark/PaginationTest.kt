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
    private lateinit var adminAccessToken: String
    private lateinit var performanceId: String

    @BeforeEach
    fun setUp() {
        val username = UUID.randomUUID().toString().take(8)
        val adminname = UUID.randomUUID().toString().takeLast(8)
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

        // 관리자
        mvc.perform(
            post("/api/v1/local/signup")
                .content(
                    mapper.writeValueAsString(
                        mapOf(
                            "username" to adminname,
                            "password" to password,
                            "nickname" to "test_admin",
                            "phoneNumber" to "010-0000-0000",
                            "email" to "test@example.com",
                            "role" to UserRole.ADMIN,
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

        // 관리자
        adminAccessToken =
            mvc.perform(
                post("/api/v1/local/signin")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to adminname,
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
                get("/api/v1/performance/search")
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
        val maxIteration = 4
        var iterations = 0

        while(iterations < maxIteration) {
            val response = mvc.perform(
                get("/api/v1/performance/search")
                    .apply { cursor?.let { param("cursor", it) } }
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
                .andExpect(jsonPath("$.data.size()").value(Matchers.greaterThan(0)))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }

            val hasNext = response.get("hasNext").asBoolean()
            if(!hasNext) {
                break
            }
            cursor = response.get("nextCursor").asText()

            iterations++
        }
    }

    @Test
    fun `공연 일부 조회 페이지네이션 테스트`() {
        var cursor: String? = null
        val maxIteration = 4
        var iterations = 0

        while(iterations < maxIteration) {
            val response = mvc.perform(
                get("/api/v1/performance/search")
                    .param("category", PerformanceCategory.CONCERT.name)
                    .apply { cursor?.let { param("cursor", it) } }
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().`is`(200))
                // CONCERT는 테스트 코드상에서 3개만 조회되어야 한다
                .andExpect(jsonPath("$.data.size()").value(3))
                .andReturn()
                .response
                .getContentAsString(Charsets.UTF_8)
                .let { mapper.readTree(it) }

            val hasNext = response.get("hasNext").asBoolean()
            if(!hasNext) {
                break
            }
            cursor = response.get("nextCursor").asText()

            iterations++
        }
    }
}
