package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.pagination.CursorPageResponse
import com.wafflestudio.interpark.pagination.CursorPageable
import com.wafflestudio.interpark.review.*
import com.wafflestudio.interpark.review.service.ReplyService
import com.wafflestudio.interpark.review.service.ReviewService
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class ReviewController(
    private val reviewService: ReviewService,
) {

    @GetMapping("/api/v1/me/review")
    @Operation(
        summary = "작성한 리뷰 조회",
        description = "본인이 작성했던 리뷰들을 조회할 수 있습니다."
    )
    fun getMyReviews(
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<GetReviewResponse>{
        val reviews = reviewService.getReviewsByUser(userDetails.getUserId())
        return ResponseEntity.ok(reviews)
    }

    @GetMapping("/api/v1/performance/{performanceId}/review")
    @Operation(
        summary = "공연의 리뷰 조회",
        description = "특정 공연에 달린 리뷰들을 조회할 수 있습니다."
    )
    fun getReviews(
        @PathVariable performanceId: String,
    ): ResponseEntity<GetReviewResponse>{
        val reviews = reviewService.getReviews(performanceId)
        return ResponseEntity.ok(reviews)
    }

    @GetMapping("/api/v2/performance/{performanceId}/review")
    @Operation(
        summary = "페이지네이션이 적용된 공연의 리뷰 조회",
        description = "특정 공연에 달린 리뷰들을 조회할 수 있습니다."
    )
    fun getCursorReviews(
        @PathVariable performanceId: String,
        @RequestParam cursor: String?,
    ): ResponseEntity<GetCursorReviewResponse>{
        val cursorPageable= CursorPageable(sortFieldName = "createdAt", cursor = cursor)
        val reviews = reviewService.getReviewsWithCursor(performanceId, cursorPageable)
        return ResponseEntity.ok(reviews)
    }

    @PostMapping("/api/v1/performance/{performanceId}/review")
    @Operation(
        summary = "리뷰 작성",
    )
    fun createReview(
        @RequestBody request: CreateReviewRequest,
        @PathVariable performanceId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<CreateReviewResponse> {
        val review = reviewService.createReview(userDetails.getUserId(), performanceId, request.rating, request.title, request.content)
        return ResponseEntity.status(201).body(review)
    }

    @PutMapping("/api/v1/review/{reviewId}")
    @Operation(
        summary = "리뷰 수정",
    )
    fun editReview(
        @RequestBody request: EditReviewRequest,
        @PathVariable reviewId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<EditReviewResponse> {
        val review = reviewService.editReview(userDetails.getUserId(), reviewId, request.rating, request.title, request.content)
        return ResponseEntity.ok(review)
    }

    @DeleteMapping("/api/v1/review/{reviewId}")
    @Operation(
        summary = "리뷰 삭제",
    )
    fun deleteReview(
        @PathVariable reviewId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<String> {
        val review = reviewService.deleteReview(userDetails.getUserId(), reviewId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/api/v1/review/{reviewId}/like")
    @Operation(
        summary = "리뷰 공감",
    )
    fun likeReview(
        @PathVariable reviewId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<String> {
        reviewService.likeReview(userDetails.getUserId(), reviewId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/api/v1/review/{reviewId}/like")
    @Operation(
        summary = "리뷰 공감 취소",
    )
    fun cancelLikeReview(
        @PathVariable reviewId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<String> {
        reviewService.cancelLikeReview(userDetails.getUserId(), reviewId)
        return ResponseEntity.noContent().build()
    }
}


typealias GetReviewResponse = List<Review>

typealias GetCursorReviewResponse = CursorPageResponse<Review>

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
