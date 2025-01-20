package com.wafflestudio.interpark.user.service

import com.wafflestudio.interpark.user.UserIdentityNotFoundException
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
import com.wafflestudio.interpark.user.persistence.UserIdentityEntity
import com.wafflestudio.interpark.user.persistence.UserIdentityRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl (
    private val userIdentityRepository: UserIdentityRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        /*
        * 원래는 username으로 찾아야 함.
        * 허나 jwt토큰의 subject에 userid를 저장하고 있음
        * userAccessTokenUtil.kt의 genereateAccessToken에서 파라미터명은 username으로 되어 있지만
        * caller(UserService.kt)에서 인자로 userId를 전달하고 있음.
        * 이를 해결하려면 RefreshTokenEntity의 필드에 username도 추가해야되는 것으로 보임
        * UserService.kt의 generateAccessToken()에서는 targetUser.username으로 변경하면되지만
        * UserAccessTokenUtil.kt의 generateAccessToken()에서는 storedRefreshToken.username으로 변경 못하기 때문
        *
        * 일단, 보류하고 userId로 찾는 것으로 진행하겠음. */
        val userIdentityEntity = userIdentityRepository.findByUserUsername(username)
            ?: throw UserIdentityNotFoundException()

        return UserDetailsImpl(userIdentityEntity)
    }

    fun loadUserByUserId(userId: String): UserDetails {
        val userIdentityEntity = userIdentityRepository.findByUserId(userId)
            ?: throw UserIdentityNotFoundException()

        return UserDetailsImpl(userIdentityEntity)
    }
}