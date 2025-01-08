package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.review.persistence.ReplyEntity
import java.time.Instant

data class Reply(
    val id: String,
    val author: String,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun fromEntity(entity: ReplyEntity): Reply {
            return Reply(
                id = entity.id!!,
                author = entity.author.id!!,
                content = entity.content,
                createdAt = entity.createdAt,
                updatedAt = entity.modifiedAt,
            )
        }
    }
}