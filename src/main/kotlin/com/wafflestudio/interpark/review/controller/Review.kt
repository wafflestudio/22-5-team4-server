package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.review.persistence.ReviewEntity
import java.time.Instant

data class Review(
    val id: String,
    val author: String,
    val rating: Int,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val likeCount: Int,
    val replyCount: Int,
) {
    companion object {
        fun fromEntity(entity: ReviewEntity, replyCount: Int): Review {
            return Review(
                id = entity.id!!,
                author = entity.author.nickname,
                rating = entity.rating,
                title = entity.title,
                content = entity.content,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                likeCount = entity.reviewLikes.size,
                replyCount = replyCount,
            )
        }
    }
}
