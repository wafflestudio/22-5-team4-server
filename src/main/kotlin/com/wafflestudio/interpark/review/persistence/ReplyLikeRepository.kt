package com.wafflestudio.interpark.review.persistence

import com.wafflestudio.interpark.user.persistence.UserEntity

import jakarta.persistence.LockModeType

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface ReplyLikeRepository : JpaRepository<ReplyLikeEntity, String> {
    fun countByReply(reply: ReplyEntity): Int

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByReplyAndUser(
        reply: ReplyEntity,
        user: UserEntity,
    ): ReplyLikeEntity?
}
