package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.review.persistence.ReviewEntity
import java.time.Instant

data class Review(
    val id: String,
    val author: String,
    val performance: String,
    // val stageId: String,
    val rating: Int,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    // val replyId: List<String>
) {
    companion object {
        fun fromEntity(entity: ReviewEntity): Review {
            return Review(
                id = entity.id!!,
                author = entity.author.id!!,
                performance = entity.performanceId,
                // stageId = entity.stageId,
                rating = entity.rating,
                title = entity.title,
                content = entity.content,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}
