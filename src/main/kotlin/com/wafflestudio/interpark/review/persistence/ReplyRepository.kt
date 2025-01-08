package com.wafflestudio.interpark.review.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ReplyRepository : JpaRepository<ReplyEntity, String> {
    fun findByReviewId(replyId: String): List<ReplyEntity>
}
