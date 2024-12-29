package com.wafflestudio.interpark.user.persistence

import jakarta.persistence.*

@Entity
class UserIdentityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "hashedPassword", nullable = false)
    val hashedPassword: String,
) {
}