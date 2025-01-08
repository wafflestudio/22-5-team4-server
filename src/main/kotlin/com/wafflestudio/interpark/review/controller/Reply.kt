package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.review.persistence.ReplyEntity
import java.time.Instant

data class Reply(
    val id: String,
    // val reviewId: String,
    val author: String,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun fromEntity(entity: ReplyEntity): Reply {
            return Reply(
                id = entity.id!!,
                reviewId = entity.reviewId,
                performance = entity.performanceId,
                author = entity.author.id!!,
                content = entity.content,
                createdAt = entity.createdAt,
                updatedAt = entity.modifiedAt
            )
        }
    }
}