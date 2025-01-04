package com.wafflestudio.interpark.user.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface UserIdentityRepository : JpaRepository<UserIdentityEntity, String> {
    fun findByUser(user: UserEntity): UserIdentityEntity?
}