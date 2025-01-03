package com.wafflestudio.interpark.user.service

import com.wafflestudio.interpark.user.*
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.persistence.UserEntity
import com.wafflestudio.interpark.user.persistence.UserIdentityEntity
import com.wafflestudio.interpark.user.persistence.UserIdentityRepository
import com.wafflestudio.interpark.user.persistence.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userIdentityRepository: UserIdentityRepository,
) {
    @Transactional
    fun signUp(
        username: String,
        password: String,
        nickname: String,
        phoneNumber: String,
        email: String,
    ): User {
        if (username.length < 6 || username.length > 20) {
            throw SignUpBadUsernameException()
        }
        if (password.length < 8 || password.length > 12) {
            throw SignUpBadPasswordException()
        }
        if (userRepository.existsByUsername(username)) {
            throw SignUpUsernameConflictException()
        }
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val user =
            userRepository.save(
                UserEntity(
                    username = username,
                    nickname = nickname,
                    phoneNumber = phoneNumber,
                    email = email,
                ),
            )
        userIdentityRepository.save(
            UserIdentityEntity(
                user = user,
                role = "USER",
                hashedPassword = encryptedPassword,
                provider = "self",
            )
        )
        return User.fromEntity(user)
    }

    @Transactional
    fun signIn(
        username: String,
        password: String,
    ): Pair<User, Pair<String, String>> {
        val targetUser = userRepository.findByUsername(username) ?: throw SignInUserNotFoundException()
        val targetIdentity = userIdentityRepository.findByUser(targetUser) ?: throw SignInUserNotFoundException()
        if(!BCrypt.checkpw(password, targetIdentity.hashedPassword)) {
            throw SignInInvalidPasswordException()
        }
        val accessToken = UserAccessTokenUtil.generateAccessToken(targetUser.id!!)
        val refreshToken = UserAccessTokenUtil.generateRefreshToken(targetIdentity.id!!)
        return Pair(User.fromEntity(targetUser), Pair(accessToken, refreshToken))
    }
}
