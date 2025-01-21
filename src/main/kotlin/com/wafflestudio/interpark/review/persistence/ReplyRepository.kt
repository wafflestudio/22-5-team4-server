package com.wafflestudio.interpark.review.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ReplyRepository : JpaRepository<ReplyEntity, String> {
    fun findByReviewId(reviewId: String): List<ReplyEntity>
    fun findByAuthorId(authorId: String): List<ReplyEntity>
    fun countByReviewId(reviewId: String): Int
}
