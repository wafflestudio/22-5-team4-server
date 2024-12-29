package com.wafflestudio.interpark.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "username", nullable = false)
    val username: String,
    @Column(name = "nickname", nullable = false)
    val nickname: String,
    @Column(name = "phone_number", nullable = false)
    val phoneNumber: String,
    @Column(name = "email", nullable = false)
    val email: String,
    @Column(name = "address", nullable = true)
    val address: String? = null,
)
