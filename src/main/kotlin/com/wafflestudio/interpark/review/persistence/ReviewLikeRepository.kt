package com.wafflestudio.interpark.review.persistence

import com.wafflestudio.interpark.user.persistence.UserEntity

import jakarta.persistence.LockModeType

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface ReviewLikeRepository : JpaRepository<ReviewLikeEntity, String> {
    fun countByReview(review: ReviewEntity): Int

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByReviewAndUser(
        review: ReviewEntity,
        user: UserEntity,
    ): ReviewLikeEntity?
}
