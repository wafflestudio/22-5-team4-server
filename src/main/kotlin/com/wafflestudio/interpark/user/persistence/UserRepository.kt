package com.wafflestudio.interpark.user.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByUsername(username: String): UserEntity?

    fun existsByUsername(username: String): Boolean
}
