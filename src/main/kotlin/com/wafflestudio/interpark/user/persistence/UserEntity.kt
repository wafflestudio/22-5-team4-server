package com.wafflestudio.interpark.user.persistence

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null
}