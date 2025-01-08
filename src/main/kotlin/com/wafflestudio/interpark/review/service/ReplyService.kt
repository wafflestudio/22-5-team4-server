package com.wafflestudio.interpark.review.service

import com.wafflestudio.interpark.review.*
import com.wafflestudio.interpark.review.controller.Review
import com.wafflestudio.interpark.review.persistence.ReviewEntity
import com.wafflestudio.interpark.review.persistence.ReviewRepository
import com.wafflestudio.interpark.review.controller.Reply
import com.wafflestudio.interpark.review.persistence.ReplyEntity
import com.wafflestudio.interpark.review.persistence.ReplyRepository
import com.wafflestudio.interpark.user.AuthenticateException
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.persistence.UserRepository
import jakarta.persistence.EntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ReviewService(
    private val entityManager: EntityManager,
    private val reviewRepository: ReviewRepository,
    private val replyRepository: ReplyRepository,
    private val userRepository: UserRepository,
) {
    // TODO: 검색기능 구현해야 함
    fun getReplies(reviewId: String): List<Reply> {
        val replies: List<Reply> =
            replyRepository
                .findByReviewId(reviewId)
                .map { Reply.fromEntity(it) }
        return replies
    }

    @Transactional
    fun createReply(
        author: User,
        reviewId: String,
        content: String,
    ): Review {
        validateContent(content)
        val performanceIdString = performanceId
        val authorEntity = userRepository.findByIdOrNull(author.id) ?: throw AuthenticateException()
        val reviewEntity =
            ReviewEntity(
                id = "",
                author = authorEntity,
                performanceId = performanceId,
                // performance = performanceEntity,
                title = title,
                content = content,
                rating = rating,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            ).let {
                reviewRepository.save(it)
            }
        return Review.fromEntity(reviewEntity)
    }

    @Transactional
    fun editReview(
        author: User,
        reviewId: String,
        rating: Int?,
        title: String?,
        content: String?,
    ): Review {
        content?.let { validateContent(it) }
        rating?.let { validateRating(it) }
        val reviewEntity = reviewRepository.findByIdOrNull(reviewId) ?: throw ReviewNotFoundException()
        val authorEntity = userRepository.findByIdOrNull(author.id) ?: throw AuthenticateException()
        if (reviewEntity.author.id != authorEntity.id) {
            throw ReviewPermissionDeniedException()
        }
        rating?.let { reviewEntity.rating = it }
        title?.let { reviewEntity.title = it }
        content?.let { reviewEntity.content = it }
        reviewEntity.updatedAt = Instant.now()
        reviewRepository.save(reviewEntity)
        return Review.fromEntity(reviewEntity)
    }

    @Transactional
    fun deleteReview(
        author: User,
        reviewId: String,
    ) {
        val reviewEntity = reviewRepository.findByIdOrNull(reviewId) ?: throw ReviewNotFoundException()
        val authorEntity = userRepository.findByIdOrNull(author.id) ?: throw AuthenticateException()
        if (reviewEntity.author.id != authorEntity.id) {
            throw ReviewPermissionDeniedException()
        }
        reviewRepository.delete(reviewEntity)
    }

    private fun validateContent(content: String) {
        if (content.isBlank()) {
            throw ReviewContentLengthOutOfRangeException()
        }
        if (content.length > 3000) {
            throw ReviewContentLengthOutOfRangeException()
        }
    }

    private fun validateRating(rating: Int) {
        if (rating !in 1..5) {
            throw ReviewRatingOutOfRangeException()
        }
    }
}