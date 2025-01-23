package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.review.*
import com.wafflestudio.interpark.review.service.ReplyService
import com.wafflestudio.interpark.review.service.ReviewService
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class ReviewController(
    private val reviewService: ReviewService,
) {

    @GetMapping("/api/v1/me/review")
    fun getMyReviews(
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<GetReviewResponse>{
        val reviews = reviewService.getReviewsByUser(userDetails.getUserId())
        return ResponseEntity.ok(reviews)
    }

    @GetMapping("/api/v1/performance/{performanceId}/review")
    fun getReviews(
        @PathVariable performanceId: String,
    ): ResponseEntity<GetReviewResponse>{
        val reviews = reviewService.getReviews(performanceId)
        return ResponseEntity.ok(reviews)
    }

    @PostMapping("/api/v1/performance/{performanceId}/review")
    fun createReview(
        @RequestBody request: CreateReviewRequest,
        @PathVariable performanceId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<CreateReviewResponse> {
        val review = reviewService.createReview(userDetails.getUserId(), performanceId, request.rating, request.title, request.content)
        return ResponseEntity.status(201).body(review)
    }

    @PutMapping("/api/v1/review/{reviewId}")
    fun editReview(
        @RequestBody request: EditReviewRequest,
        @PathVariable reviewId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<EditReviewResponse> {
        val review = reviewService.editReview(userDetails.getUserId(), reviewId, request.rating, request.title, request.content)
        return ResponseEntity.ok(review)
    }

    @DeleteMapping("/api/v1/review/{reviewId}")
    fun deleteReview(
        @PathVariable reviewId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<String> {
        val review = reviewService.deleteReview(userDetails.getUserId(), reviewId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/api/v1/review/{reviewId}/like")
    fun likeReview(
        @PathVariable reviewId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<String> {
        reviewService.likeReview(userDetails.getUserId(), reviewId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/api/v1/review/{reviewId}/like")
    fun cancelLikeReview(
        @PathVariable reviewId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<String> {
        reviewService.cancelLikeReview(userDetails.getUserId(), reviewId)
        return ResponseEntity.noContent().build()
    }
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
