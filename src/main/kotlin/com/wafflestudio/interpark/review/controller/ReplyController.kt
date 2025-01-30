package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.pagination.CursorPageResponse
import com.wafflestudio.interpark.pagination.CursorPageable
import com.wafflestudio.interpark.review.service.ReplyService
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class ReplyController(
    private val replyService: ReplyService,
) {
    @GetMapping("/api/v1/me/reply")
    fun getRepliesByUser(
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<GetReplyResponse>{
        val replies = replyService.getRepliesByUser(userDetails.getUserId())
        return ResponseEntity.ok(replies)
    }

    @GetMapping("/api/v1/review/{reviewId}/reply")
    fun getReplies(
        @PathVariable reviewId: String,
    ): ResponseEntity<GetReplyResponse>{
        val replies = replyService.getReplies(reviewId)
        return ResponseEntity.ok(replies)
    }

    @GetMapping("/api/v2/review/{reviewId}/reply")
    fun getCursorReplies(
        @PathVariable reviewId: String,
        @RequestParam cursor: String?,
    ): ResponseEntity<GetCursorReplyResponse>{
        val cursorPageable= CursorPageable(sortFieldName = "createdAt", cursor = cursor)
        val reviews = replyService.getRepliesWithCursor(reviewId, cursorPageable)
        return ResponseEntity.ok(reviews)
    }

    @PostMapping("/api/v1/review/{reviewId}/reply")
    fun createReply(
        @RequestBody request: CreateReplyRequest,
        @PathVariable reviewId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<CreateReplyResponse> {
        val reply = replyService.createReply(userDetails.getUserId(), reviewId, request.content)
        return ResponseEntity.status(201).body(reply)
    }

    @PutMapping("/api/v1/reply/{replyId}")
    fun editReply(
        @RequestBody request: EditReplyRequest,
        @PathVariable replyId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<EditReplyResponse> {
        val reply = replyService.editReply(userDetails.getUserId(), replyId, request.content)
        return ResponseEntity.ok(reply)
    }

    @DeleteMapping("/api/v1/reply/{replyId}")
    fun deleteReply(
        @PathVariable replyId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<String> {
        replyService.deleteReply(userDetails.getUserId(), replyId)
        return ResponseEntity.noContent().build()
    }

}


typealias GetReplyResponse = List<Reply>

typealias GetCursorReplyResponse = CursorPageResponse<Reply>

data class CreateReplyRequest(
    val content: String,
)

typealias CreateReplyResponse = Reply

data class EditReplyRequest(
    val content: String,
)

typealias EditReplyResponse = Reply
