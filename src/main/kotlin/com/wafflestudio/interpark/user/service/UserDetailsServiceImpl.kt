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
        val userIdentityEntity = userIdentityRepository.findByUserUsername(username)
            ?: throw UserIdentityNotFoundException()

        return UserDetailsImpl(userIdentityEntity)
    }

    fun getUserIdentityByUserId(userId: String): UserIdentityEntity? {
        return userIdentityRepository.findByUserId(userId)
    }
}