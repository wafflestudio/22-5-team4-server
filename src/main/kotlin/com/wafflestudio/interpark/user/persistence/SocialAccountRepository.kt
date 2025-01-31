package com.wafflestudio.interpark.user.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface SocialAccountRepository : JpaRepository<SocialAccountEntity, Long> {
    fun findByProviderAndProviderId(provider: Provider, providerId: String): SocialAccountEntity?
}