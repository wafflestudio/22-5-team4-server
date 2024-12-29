package com.wafflestudio.interpark.user.service

import com.wafflestudio.interpark.user.*
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.persistence.UserEntity
import com.wafflestudio.interpark.user.persistence.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun signUp(
        username: String,
        password: String,
        nickname: String,
        phoneNumber: String,
        email: String,
    ): User {
        //TODO : 회원가입 기능 만들기
        val user =
            userRepository.save(
                UserEntity(
                    username = username,
                    nickname = nickname,
                    phoneNumber = phoneNumber,
                    email = email,
                ),
            )
        return User.fromEntity(user)
    }
}
