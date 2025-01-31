package com.wafflestudio.interpark.review.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface ReplyRepository : JpaRepository<ReplyEntity, String>,
    JpaSpecificationExecutor<ReplyEntity> {
    @Query("SELECT r FROM ReplyEntity r WHERE r.review.id = :reviewId ORDER BY r.createdAt DESC")
    fun findByReviewId(reviewId: String): List<ReplyEntity>
    @Query("SELECT r FROM ReplyEntity r WHERE r.author.id = :authorId ORDER BY r.createdAt DESC")
    fun findByAuthorId(authorId: String): List<ReplyEntity>
    fun countByReviewId(reviewId: String): Int
}
