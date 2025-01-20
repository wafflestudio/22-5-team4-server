package com.wafflestudio.interpark

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.interpark.user.UserAccessTokenUtil
import com.wafflestudio.interpark.user.persistence.UserRepository
import com.wafflestudio.interpark.user.persistence.UserRole
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class UserIntegrationTest
    @Autowired
    constructor(
        private val mvc: MockMvc,
        private val mapper: ObjectMapper,
    ) {
        @Test
        fun `회원가입시에 유저 이름 혹은 비밀번호가 정해진 규칙에 맞지 않는 경우 400 응답을 내려준다`() {
            // bad username
            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "bad",
                                "password" to "correct",
                                "nickname" to "examplename",
                                "phoneNumber" to "010-0000-0000",
                                "email" to "test@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(400))

            // bad password
            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "correct",
                                "password" to "bad",
                                "nickname" to "examplename",
                                "phoneNumber" to "010-0000-0000",
                                "email" to "test@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(400))

            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "correct",
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
        }

        @Test
        fun `회원가입시에 이미 해당 유저 이름이 존재하면 409 응답을 내려준다`() {
            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "correct2",
                                "password" to "correct1",
                                "nickname" to "examplename",
                                "phoneNumber" to "010-0000-0000",
                                "email" to "test@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(200))

            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "correct2",
                                "password" to "correct2",
                                "nickname" to "https://wafflestudio.com/nicknames/icon_intro2.svg",
                                "phoneNumber" to "010-0000-0000",
                                "email" to "test@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(409))
        }

        @Test
        fun `로그인 정보가 정확하지 않으면 401 응답을 내려준다`() {
            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "correct3",
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

            // not exist username
            mvc.perform(
                post("/api/v1/local/signin")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "wrong404",
                                "password" to "12345678",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(401))

            // wrong password
            mvc.perform(
                post("/api/v1/local/signin")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "correct3",
                                "password" to "87654321",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(401))

            mvc.perform(
                post("/api/v1/local/signin")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to "correct3",
                                "password" to "12345678",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(200))
        }

        @Test
        fun `토큰 재발행이 가능하다`() {
            val (username, password) = "correct5" to "12345678"
            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to username,
                                "password" to password,
                                "nickname" to username,
                                "phoneNumber" to "010-0000-0000",
                                "email" to "test@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(200))

            val refreshToken = mvc.perform(
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
            ).andReturn().response.cookies.find { it.name == "refreshToken" }!!.value

            val newAccessToken =
                mvc.perform(
                    post("/api/v1/auth/refresh_token")
                        .cookie(Cookie("refreshToken", refreshToken))
                )
                    .andExpect(status().`is`(200))
                    .andReturn()
                    .response.getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("accessToken").asText() }

            mvc.perform(
                get("/api/v1/users/me")
                    .header("Authorization", "Bearer $newAccessToken"),
            )
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.nickname").value(username))
        }

        @Test
        fun `잘못된 인증 토큰으로 인증시 401 응답을 내려준다`() {
            val (username, password) = "correct4" to "12345678"
            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to username,
                                "password" to password,
                                "nickname" to username,
                                "phoneNumber" to "010-0000-0000",
                                "email" to "test@example.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(200))

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
                )
                    .andExpect(status().`is`(200))
                    .andReturn()
                    .response.getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("accessToken").asText() }

            mvc.perform(
                get("/api/v1/users/me")
                    .header("Authorization", "Bearer bad"),
            )
                .andExpect(status().`is`(403))

            mvc.perform(
                get("/api/v1/users/me")
                    .header("Authorization", "Bearer $accessToken"),
            )
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.nickname").value(username))
        }

        @Test
        fun `관리자가 API 엔드포인트에 접근가능하다`(){
            val (username, password) = "correct5" to "12345678"
            mvc.perform(
                post("/api/v1/local/signup")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "username" to username,
                                "password" to password,
                                "nickname" to username,
                                "phoneNumber" to "010-0000-0000",
                                "email" to "test@example.com",
                                "role" to UserRole.ADMIN,
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().`is`(200))

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
                )
                    .andExpect(status().`is`(200))
                    .andReturn()
                    .response.getContentAsString(Charsets.UTF_8)
                    .let { mapper.readTree(it).get("accessToken").asText() }

            mvc.perform(
                get("/api/v1/users/me")
                    .header("Authorization", "Bearer bad"),
            )
                .andExpect(status().`is`(403))

            mvc.perform(
                get("/api/v1/users/me")
                    .header("Authorization", "Bearer $accessToken"),
            )
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.nickname").value(username))
        }
    }
