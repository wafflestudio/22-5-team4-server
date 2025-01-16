package com.wafflestudio.interpark

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
import com.wafflestudio.interpark.performance.persistence.PerformanceRepository
import com.wafflestudio.interpark.user.persistence.UserRole
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class PerformanceIntegrationTest
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

        // 관리자
        mvc.perform(
            post("/api/v1/signup")
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

        // 관리자
        adminAccessToken =
            mvc.perform(
                post("/api/v1/signin")
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
                    val firstItem = node.firstOrNull() ?: error("Response array is empty")
                    val idNode = firstItem.get("id")
                    requireNotNull(idNode) { "ID not found in response item: $firstItem" }
                    idNode.asText()
                }
    }

    @Test
    fun `공연 검색 플로우 테스트`() {
        // 4️⃣ 공연 검색 (title 조건)
        mvc.perform(
            get("/api/v1/performance/search")
                .header("Authorization", "Bearer $userAccessToken")
                .param("title", "지킬앤하이드")
                .contentType(MediaType.APPLICATION_JSON),
        ).andExpect(status().`is`(200))
            .andExpect(jsonPath("$[0].title").value("뮤지컬 지킬앤하이드"))
            .andExpect(jsonPath("$.length()").value(1))

        // 5️⃣ 공연 검색 (category 조건)
        mvc.perform(
            get("/api/v1/performance/search")
                .header("Authorization", "Bearer $userAccessToken")
                .param("category", PerformanceCategory.CONCERT.name)
                .contentType(MediaType.APPLICATION_JSON),
        ).andExpect(status().`is`(200))
            .andExpect(jsonPath("$").isArray) // 응답이 배열인지 확인
            .andExpect(jsonPath("$.length()").value(3)) // 배열의 길이가 0인지 확인
    }

    @Test
    fun `공연 상세 조회 테스트`() {
        // 6️⃣ 공연 상세 조회
        println("Generated performanceId: $performanceId")
        mvc.perform(
            get("/api/v1/performance/$performanceId")
                .header("Authorization", "Bearer $userAccessToken"),
        ).andExpect(status().`is`(200))
            .andExpect(jsonPath("$.id").value(performanceId))
            .andExpect(jsonPath("$.title").value("뮤지컬 지킬앤하이드"))
    }

    @Test
    fun `공연 삭제 플로우 테스트`() {
        // 일반 유저 공연 삭제 실패
        mvc.perform(
            delete("/admin/v1/performance/$performanceId")
                .header("Authorization", "Bearer $userAccessToken"),
        ).andExpect(status().`is`(403))

        // 7️⃣ 공연 삭제
        mvc.perform(
            delete("/admin/v1/performance/$performanceId")
                .header("Authorization", "Bearer $adminAccessToken"),
        ).andExpect(status().`is`(204))

        // 8️⃣ 삭제된 공연 상세 조회 실패 확인
        mvc.perform(
            get("/api/v1/performance/$performanceId")
                .header("Authorization", "Bearer $adminAccessToken"),
        ).andExpect(status().`is`(404))
            .andExpect(jsonPath("$.error").value("Performance Not Found"))
    }

    @Test
    fun `공연 생성 테스트 - 관리자 성공`() {
        // 1️⃣ 공연 생성 요청 데이터
        val createPerformanceRequest = mapOf(
            "title" to "뮤지컬 캣츠",
            "detail" to "https://example.com/cats-detail.jpg",
            "category" to PerformanceCategory.MUSICAL.name,
            "posterUri" to "https://example.com/cats-poster.jpg",
            "backdropImageUri" to "https://example.com/cats-backdrop.jpg"
        )

        // 2️⃣ 공연 생성 요청 및 응답 확인
        val result = mvc.perform(
            post("/admin/v1/performance")
                .header("Authorization", "Bearer $adminAccessToken")
                .content(mapper.writeValueAsString(createPerformanceRequest))
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().`is`(201)) // HTTP 201 Created 확인
            .andExpect(jsonPath("$.title").value("뮤지컬 캣츠"))
            .andExpect(jsonPath("$.detail").value("https://example.com/cats-detail.jpg"))
            .andExpect(jsonPath("$.category").value(PerformanceCategory.MUSICAL.name))
            .andExpect(jsonPath("$.posterUri").value("https://example.com/cats-poster.jpg"))
            .andExpect(jsonPath("$.backdropImageUri").value("https://example.com/cats-backdrop.jpg"))
            .andReturn()
    }

    @Test
    fun `공연 생성 테스트 - 일반 유저 실패`() {
        // 1️⃣ 공연 생성 요청 데이터
        val createPerformanceRequest = mapOf(
            "title" to "뮤지컬 캣츠",
            "detail" to "https://example.com/cats-detail.jpg",
            "category" to PerformanceCategory.MUSICAL.name,
            "posterUri" to "https://example.com/cats-poster.jpg",
            "backdropImageUri" to "https://example.com/cats-backdrop.jpg"
        )

        // 2️⃣ 공연 생성 요청 및 응답 확인
        val result = mvc.perform(
            post("/admin/v1/performance")
                .header("Authorization", "Bearer $userAccessToken")
                .content(mapper.writeValueAsString(createPerformanceRequest))
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().`is`(403)) // HTTP 403 Forbidden 확인
    }

    @Test
    fun `공연 생성 실패 - 필수 정보 누락`() {
        mvc.perform(
            post("/admin/v1/performance")
                .header("Authorization", "Bearer $adminAccessToken")
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
 