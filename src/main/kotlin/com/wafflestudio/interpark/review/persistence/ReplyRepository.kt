package com.wafflestudio.interpark.review.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ReplyRepository : JpaRepository<ReplyEntity, String> {
    fun findByPerformanceId(performanceId: String): List<ReplyEntity>
    fun findByReviewId(replyId: String): List<ReplyEntity>
}
