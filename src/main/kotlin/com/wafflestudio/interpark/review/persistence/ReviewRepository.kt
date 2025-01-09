package com.wafflestudio.interpark.review.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<ReviewEntity, String> {
    fun findByPerformanceId(performanceId: String): List<ReviewEntity>
    fun findByAuthorId(authorId: String): List<ReviewEntity>
}
