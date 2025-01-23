package com.wafflestudio.interpark.user.service

import com.wafflestudio.interpark.user.*
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.persistence.Provider
import com.wafflestudio.interpark.user.persistence.SocialAccountEntity
import com.wafflestudio.interpark.user.persistence.SocialAccountRepository
import com.wafflestudio.interpark.user.persistence.UserEntity
import com.wafflestudio.interpark.user.persistence.UserIdentityEntity
import com.wafflestudio.interpark.user.persistence.UserIdentityRepository
import com.wafflestudio.interpark.user.persistence.UserRepository
import com.wafflestudio.interpark.user.persistence.UserRole
import org.mindrot.jbcrypt.BCrypt
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userIdentityRepository: UserIdentityRepository,
    private val userAccessTokenUtil: UserAccessTokenUtil,
    private val socialAccountRepository: SocialAccountRepository,

) {
    @Transactional
    fun signUp(
        username: String,
        password: String,
        nickname: String,
        phoneNumber: String,
        email: String,
        role: UserRole = UserRole.USER,
        provider: Provider? = null,
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
                role = role,
                hashedPassword = encryptedPassword,
            ),
        )

        // TODO: provider가 null이 아니라면 소셜계정 연동해주기

        return User.fromEntity(user)
    }

    @Transactional
    fun signIn(
        username: String,
        password: String,
    ): Triple<User, String, String> {
        val targetUser = userRepository.findByUsername(username) ?: throw SignInUserNotFoundException()
        val user = User.fromEntity(targetUser)
        val targetIdentity = userIdentityRepository.findByUser(targetUser) ?: throw SignInUserNotFoundException()
        if (!BCrypt.checkpw(password, targetIdentity.hashedPassword)) {
            throw SignInInvalidPasswordException()
        }
        val accessToken = userAccessTokenUtil.generateAccessToken(targetUser.id!!)
        val refreshToken = userAccessTokenUtil.generateRefreshToken(targetUser.id!!)
        return Triple(user, accessToken, refreshToken)
    }

    @Transactional
    fun signOut(refreshToken: String) {
        userAccessTokenUtil.removeRefreshToken(refreshToken)
    }

    // spring security로 전가돼서 사실상 호출 안 됨.
    @Transactional
    fun authenticate(accessToken: String): User {
        val userId = userAccessTokenUtil.validateAccessToken(accessToken) ?: throw AuthenticateException()
        val user = userRepository.findByIdOrNull(userId) ?: throw AuthenticateException()
        return User.fromEntity(user)
    }

    @Transactional
    fun refreshAccessToken(refreshToken: String): Pair<String, String> {
        return userAccessTokenUtil.refreshAccessToken(refreshToken) ?: throw AuthenticateException()
    }

    @Transactional
    fun linkSocialAccount(
        userId: String,
        provider: Provider,
        providerId: String
    ): UserIdentityEntity {
        // 유저 확인
        val userIdentity = userIdentityRepository.findById(userId)
            .orElseThrow { UserIdentityNotFoundException() }

        // 소셜 계정 중복 확인
        val existingSocialAccount = socialAccountRepository.findByProviderAndProviderId(provider, providerId)
        if (existingSocialAccount != null) {
            if (existingSocialAccount.userIdentity.user.id != userId) {
                throw SocialAccountAlreadyLinkedException()
            }
        }

        // 소셜 계정 생성 및 연동
        val socialAccount = SocialAccountEntity(
            userIdentity = userIdentity,
            provider = provider,
            providerId = providerId
        )
        socialAccountRepository.save(socialAccount)

        return userIdentity
    }
}
