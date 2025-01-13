package com.wafflestudio.interpark

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
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
class PerformanceServiceTest
@Autowired
constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    private lateinit var accessToken: String
    private lateinit var performanceId: String

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

        // 3️⃣ 공연 생성
        performanceId =
            mvc.perform(
                post("/admin/v1/performance")
                    .header("Authorization", "Bearer $accessToken")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "title" to "뮤지컬 지킬앤하이드",
                                "detail" to "지금 이 순간, 끝나지 않는 신화",
                                "category" to PerformanceCategory.MUSICAL.name,
                                "posterUri" to "https://example.com/poster.jpg",
                                "backdropImageUri" to "https://example.com/backdrop.jpg",
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
    fun `공연 검색 플로우 테스트`() {
        // 4️⃣ 공연 검색 (title 조건)
        mvc.perform(
            get("/api/v1/performance/search")
                .header("Authorization", "Bearer $accessToken")
                .param("title", "지킬앤하이드")
                .contentType(MediaType.APPLICATION_JSON),
        ).andExpect(status().`is`(200))
            .andExpect(jsonPath("$[0].title").value("뮤지컬 지킬앤하이드"))

        // 5️⃣ 공연 검색 (category 조건)
        mvc.perform(
            get("/api/v1/performance/search")
                .header("Authorization", "Bearer $accessToken")
                .param("category", PerformanceCategory.MUSICAL.name)
                .contentType(MediaType.APPLICATION_JSON),
        ).andExpect(status().`is`(200))
            .andExpect(jsonPath("$[0].category").value(PerformanceCategory.MUSICAL.name))
    }

    @Test
    fun `공연 상세 조회 테스트`() {
        // 6️⃣ 공연 상세 조회
        println("Generated performanceId: $performanceId")
        mvc.perform(
            get("/api/v1/performance/$performanceId")
                .header("Authorization", "Bearer $accessToken"),
        ).andExpect(status().`is`(200))
            .andExpect(jsonPath("$.id").value(performanceId))
            .andExpect(jsonPath("$.title").value("뮤지컬 지킬앤하이드"))
    }

    @Test
    fun `공연 삭제 플로우 테스트`() {
        // 7️⃣ 공연 삭제
        mvc.perform(
            delete("/admin/v1/performance/$performanceId")
                .header("Authorization", "Bearer $accessToken"),
        ).andExpect(status().`is`(204))

        // 8️⃣ 삭제된 공연 상세 조회 실패 확인
        mvc.perform(
            get("/api/v1/performance/$performanceId")
                .header("Authorization", "Bearer $accessToken"),
        ).andExpect(status().`is`(404))
            .andExpect(jsonPath("$.error").value("Performance Not Found"))
    }

    @Test
    fun `공연 생성 실패 - 필수 정보 누락`() {
        mvc.perform(
            post("/admin/v1/performance")
                .header("Authorization", "Bearer $accessToken")
                .content(
                    mapper.writeValueAsString(
                        mapOf(
                            "title" to "",
                            "detail" to "지금 이 순간, 끝나지 않는 신화",
                            "category" to PerformanceCategory.MUSICAL.name,
                            "posterUri" to "",
                            "backdropImageUri" to "",
                        ),
                    ),
                )
                .contentType(MediaType.APPLICATION_JSON),
        ).andExpect(status().`is`(400))
            .andExpect(jsonPath("$.error").value("Method Argument Validation failed"))
    }
}