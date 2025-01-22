package com.wafflestudio.interpark.user.controller

import com.wafflestudio.interpark.user.persistence.UserEntity

data class User(
    val id: String,
    val username: String,
    val nickname: String,
    val phoneNumber: String,
    val email: String,
) {
    companion object {
        fun fromEntity(entity: UserEntity): User {
            return User(
                id = entity.id!!,
                username = entity.username,
                nickname = entity.nickname,
                phoneNumber = entity.phoneNumber,
                email = entity.email,
            )
        }
    }
}
