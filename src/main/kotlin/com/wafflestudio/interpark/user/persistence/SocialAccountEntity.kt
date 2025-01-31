package com.wafflestudio.interpark.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class SocialAccountEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_identity_id")
    val userIdentity: UserIdentityEntity,
    @Column(name = "provider", nullable = false)
    val provider: Provider,
    @Column(name = "provider_id", nullable = false)
    val providerId: String,
)

enum class Provider(val displayName: String) {
    GOOGLE("Google"),
    KAKAO("Kakao"),
    NAVER("Naver");

    companion object {
        fun fromName(name: String): Provider? {
            return entries.find { it.name == name.uppercase() }
        }
    }
}