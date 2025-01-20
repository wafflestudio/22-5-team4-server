package com.wafflestudio.interpark.review.service

import com.wafflestudio.interpark.review.*
import com.wafflestudio.interpark.review.controller.Review
import com.wafflestudio.interpark.review.persistence.ReviewEntity
import com.wafflestudio.interpark.review.persistence.ReviewRepository
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
    private val userRepository: UserRepository,
) {
    fun getReviewsByUser(userId: String): List<Review> {
        val reviews: List<Review> =
            reviewRepository
                .findByAuthorId(userId)
                .map { Review.fromEntity(it) }
        return reviews
    }

    // TODO: 검색기능 구현해야 함
    fun getReviews(performanceId: String): List<Review> {
        val reviews: List<Review> =
            reviewRepository
                .findByPerformanceId(performanceId)
                .map { Review.fromEntity(it) }
        return reviews
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
        val performanceIdString = performanceId
        // val performanceEntity = entityManager.getReference(PerformanceEntity::class.java, performanceId)
        // val performanceEntity = performanceRepository.findByIdOrNull(performanceId) ?: throw PerformanceNotFoundException()
        val authorEntity = userRepository.findByIdOrNull(authorId) ?: throw AuthenticateException()
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
        return Review.fromEntity(reviewEntity)
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

    // @Transactional
    // fun likeReview(
    //     user: User,
    //     reviewId: String,
    // ) {
    //     val reviewEntity = reviewRepository.findByIdWithWriteLock(reviewId) ?: throw ReviewNotFoundException()
    //     val userEntity = userRepository.findByIdOrNull(user.id) ?: throw AuthenticateException()
    //     if (reviewEntity.reviewLikes.any { it.user.id == userEntity.id }) {
    //         return
    //     }
    //     val reviewLikeEntity = reviewLikeRepository.save(ReviewLikeEntity(review = reviewEntity, user = userEntity, createdAt = Instant.now(), updatedAt = Instant.now()))
    //     reviewEntity.reviewLikes += reviewLikeEntity
    //     reviewRepository.save(reviewEntity)
    // }

    // @Transactional
    // fun unlikeReview(
    //     user: User,
    //     reviewId: String,
    // ) {
    //     val reviewEntity = reviewRepository.findByIdWithWriteLock(reviewId) ?: throw ReviewNotFoundException()
    //     val userEntity = userRepository.findByIdOrNull(user.id) ?: throw AuthenticateException()
    //     val reviewToDelete = reviewEntity.reviewLikes.find { it.user.id == userEntity.id } ?: return
    //     reviewEntity.reviewLikes -= reviewToDelete
    //     reviewLikeRepository.delete(reviewToDelete)
    //     reviewRepository.save(reviewEntity)
    // }
}