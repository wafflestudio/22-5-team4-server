package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.review.*
import com.wafflestudio.interpark.user.AuthUser
import com.wafflestudio.interpark.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// @RestController
class ReplyController(
    private val replyService: ReplyService,
) {

    @GetMapping("/api/v1/reviews/{reviewId}/reply")
    fun getReplies(
        @PathVariable reviewId: String,
        @AuthUser user: User,
    ): ResponseEntity<GetReplyResponse>{
        val replies = replyService.getReplies(reviewId)
        return ResponseEntity.ok(replies)
    }

    // @PostMapping("/api/v1/reviews/{reviewId}/reply")
    // fun createReply(
    //     @PathVariable reviewId: String,
    //     @AuthUser user: User,
    // ): ResponseEntity<CreateReplyResponse> {
    //     val reply = replyservice.createReply(user, reviewId)
    //     return ResponseEntity.ok(reply)
    // }

    // @PutMapping("/api/v1/reply/{replyId}")
    // fun editReply(
    //     @RequestBody request: EditReplyRequest,
    //     @PathVariable replyId: String,
    //     @AuthUser user: User,
    // ): ResponseEntity<EditReplyResponse> {
    //     val reply = replyService.editReply(user, reviewId, request.)
    // }

    // @DeleteMapping("/api/v1/reply/{replyId}")
    // fun deleteReply(
    //     @PathVariable replyId: String,
    //     @AuthUser user: User,
    // ): ResponseEntity<EditReplyResponse> {
    //     val reply = replyService.deleteReply(user, replyId)
    // }


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


typealias ReplyGetResponse = List<Reply>


data class ReplyCreateRequest(
    val content: String,
)

typealias ReplyCreateResponse = Reply

data class ReplyEditRequest(
    val content: String?,
)

typealias ReplyEditResponse = Reply
