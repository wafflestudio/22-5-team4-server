package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.review.*
import com.wafflestudio.interpark.review.service.ReviewService
import com.wafflestudio.interpark.user.AuthUser
import com.wafflestudio.interpark.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ReviewController(
    private val reviewService: ReviewService,
) {

    @GetMapping("/api/v1/performance/{performanceId}/review")
    fun getReviews(
        @PathVariable performanceId: String,
        @AuthUser user: User,
    ): ResponseEntity<GetReviewResponse>{
        val reveiws = reviewService.getReviews(performanceId)
        return ResponseEntity.ok(reveiws)
    }

    @PostMapping("/api/v1/performance/{performanceId}/review")
    fun createReview(
        @RequestBody request: CreateReviewRequest,
        @PathVariable performanceId: String,
        @AuthUser user: User,
    ): ResponseEntity<CreateReviewResponse> {
        val review = reviewService.createReview(user, performanceId, request.rating, request.title, request.content)
        return ResponseEntity.status(201).body(review)
    }

    @PutMapping("/api/v1/review/{reviewId}")
    fun editReview(
        @RequestBody request: EditReviewRequest,
        @PathVariable reviewId: String,
        @AuthUser user: User,
    ): ResponseEntity<EditReviewResponse> {
        val review = reviewService.editReview(user, reviewId, request.rating, request.title, request.content)
        return ResponseEntity.ok(review)
    }

    @DeleteMapping("/api/v1/review/{reviewId}")
    fun deleteReview(
        @PathVariable reviewId: String,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        val review = reviewService.deleteReview(user, reviewId)
        return ResponseEntity.noContent().build()
    }

    // @PostMapping("/api/v1/reviews/{reviewId}/like")
    // fun likeReview(
    //     @PathVariable reviewId: String,
    //     @AuthUser user: User,
    // ): ResponseEntity<String> {
    //     reviewService.likeReview(user, reviewId)
    //     return ResponseEntity.noContent().build()
    // }

    // @PostMapping("/api/v1/reviews/{reviewId}/unlike")
    // fun unlikeReview(
    //     @PathVariable reviewId: String,
    //     @AuthUser user: User,
    // ): ResponseEntity<String> {
    //     reviewService.unlikeReview(user, reviewId)
    //     return ResponseEntity.noContent().build()
    // }
}


typealias GetReviewResponse = List<Review>


data class CreateReviewRequest(
    val rating: Int,
    val title: String,
    val content: String,
)

typealias CreateReviewResponse = Review

data class EditReviewRequest(
    val rating: Int?,
    val title: String?,
    val content: String?,
)

typealias EditReviewResponse = Review
