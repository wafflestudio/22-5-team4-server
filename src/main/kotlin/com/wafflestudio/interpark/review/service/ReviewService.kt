package com.wafflestudio.interpark.review.service

import com.wafflestudio.interpark.pagination.CursorPageResponse
import com.wafflestudio.interpark.pagination.CursorPageService
import com.wafflestudio.interpark.pagination.CursorPageable
import com.wafflestudio.interpark.performance.PerformanceNotFoundException
import com.wafflestudio.interpark.performance.persistence.PerformanceEntity
import com.wafflestudio.interpark.performance.persistence.PerformanceRepository
import com.wafflestudio.interpark.review.*
import com.wafflestudio.interpark.review.controller.Review
import com.wafflestudio.interpark.review.persistence.ReviewEntity
import com.wafflestudio.interpark.review.persistence.ReviewLikeEntity
import com.wafflestudio.interpark.review.persistence.ReviewLikeRepository
import com.wafflestudio.interpark.review.persistence.ReviewRepository
import com.wafflestudio.interpark.user.AuthenticateException
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.persistence.UserRepository
import jakarta.persistence.EntityManager
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ReviewService(
    private val performanceRepository: PerformanceRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val reviewLikeRepository: ReviewLikeRepository,
    private val replyService: ReplyService,
) : CursorPageService<ReviewEntity>(reviewRepository) {
    fun getReviewsByUser(userId: String): List<Review> {
        val reviews: List<Review> =
            reviewRepository
                .findByAuthorId(userId)
                .map { Review.fromEntity(it, replyService.countReplies(it.id)) }
        return reviews
    }

    fun getReviews(performanceId: String): List<Review> {
        val reviews: List<Review> =
            reviewRepository
                .findByPerformanceId(performanceId)
                .map { Review.fromEntity(it, replyService.countReplies(it.id)) }
        return reviews
    }

    fun getReviewsWithCursor(
        performanceId: String,
        cursorPageable: CursorPageable,
    ): CursorPageResponse<Review> {
        val spec: Specification<ReviewEntity> = Specification.where { root, _, cb ->
            cb.equal(root.get<PerformanceEntity>("performance").get<String>("id"), performanceId)
        }

        val searchResult = findAllWithCursor(cursorPageable, spec)
        val reviewEntities = searchResult.data

        val reviewData = reviewEntities.map { Review.fromEntity(it, replyService.countReplies(it.id)) }

        return CursorPageResponse(
            data = reviewData,
            nextCursor = searchResult.nextCursor,
            hasNext = searchResult.hasNext,
        )
    }

    @Transactional
    fun createReview(
        authorId: String,
        performanceId: String,
        rating: Int,
        title: String,
        content: String,
    ): Review {
        validateContent(content)
        validateRating(rating)

        val performanceEntity = performanceRepository.findByIdOrNull(performanceId) ?: throw PerformanceNotFoundException()
        val authorEntity = userRepository.findByIdOrNull(authorId) ?: throw AuthenticateException()
        val reviewEntity =
            ReviewEntity(
                id = "",
                author = authorEntity,
                performance = performanceEntity,
                title = title,
                content = content,
                rating = rating,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            ).let {
                reviewRepository.save(it)
            }
        return Review.fromEntity(reviewEntity, replyService.countReplies(reviewEntity.id))
    }

    @Transactional
    fun editReview(
        authorId: String,
        reviewId: String,
        rating: Int?,
        title: String?,
        content: String?,
    ): Review {
        content?.let { validateContent(it) }
        rating?.let { validateRating(it) }
        val reviewEntity = reviewRepository.findByIdOrNull(reviewId) ?: throw ReviewNotFoundException()
        val authorEntity = userRepository.findByIdOrNull(authorId) ?: throw AuthenticateException()
        if (reviewEntity.author.id != authorEntity.id) {
            throw ReviewPermissionDeniedException()
        }
        rating?.let { reviewEntity.rating = it }
        title?.let { reviewEntity.title = it }
        content?.let { reviewEntity.content = it }
        reviewEntity.updatedAt = Instant.now()
        reviewRepository.save(reviewEntity)
        return Review.fromEntity(reviewEntity, replyService.countReplies(reviewEntity.id))
    }

    @Transactional
    fun deleteReview(
        authorId: String,
        reviewId: String,
    ) {
        val reviewEntity = reviewRepository.findByIdOrNull(reviewId) ?: throw ReviewNotFoundException()
        val authorEntity = userRepository.findByIdOrNull(authorId) ?: throw AuthenticateException()
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

    @Transactional
    fun likeReview(
        userId: String,
        reviewId: String,
    ) {
        val reviewEntity = reviewRepository.findByIdWithWriteLock(reviewId) ?: throw ReviewNotFoundException()
        val userEntity = userRepository.findByIdOrNull(userId) ?: throw AuthenticateException()
        if (reviewEntity.reviewLikes.any { it.user.id == userEntity.id }) {
            return
        }
        val reviewLikeEntity = reviewLikeRepository.save(ReviewLikeEntity(review = reviewEntity, user = userEntity))
        reviewEntity.reviewLikes += reviewLikeEntity
        reviewRepository.save(reviewEntity)
    }

    @Transactional
    fun cancelLikeReview(
        userId: String,
        reviewId: String,
    ) {
        val reviewEntity = reviewRepository.findByIdWithWriteLock(reviewId) ?: throw ReviewNotFoundException()
        val userEntity = userRepository.findByIdOrNull(userId) ?: throw AuthenticateException()
        val reviewLikeToDelete = reviewEntity.reviewLikes.find { it.user.id == userEntity.id } ?: return
        reviewEntity.reviewLikes -= reviewLikeToDelete
        reviewLikeRepository.delete(reviewLikeToDelete)
        reviewRepository.save(reviewEntity)
    }
}
