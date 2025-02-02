package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.pagination.CursorPageResponse
import com.wafflestudio.interpark.pagination.CursorPageable
import com.wafflestudio.interpark.review.service.ReplyService
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class ReplyController(
    private val replyService: ReplyService,
) {
    @GetMapping("/api/v1/me/reply")
    @Operation(
        summary = "본인의 댓글 조회",
        description = "본인이 작성한 댓글들을 조회할 수 있습니다."
    )
    fun getRepliesByUser(
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<GetReplyResponse>{
        val replies = replyService.getRepliesByUser(userDetails.getUserId())
        return ResponseEntity.ok(replies)
    }

    @GetMapping("/api/v1/review/{reviewId}/reply")
    @Operation(
        summary = "리뷰의 댓글 조회",
        description = "특정 리뷰에 달린 댓글들을 조회할 수 있습니다."
    )
    fun getReplies(
        @PathVariable reviewId: String,
    ): ResponseEntity<GetReplyResponse>{
        val replies = replyService.getReplies(reviewId)
        return ResponseEntity.ok(replies)
    }

    @GetMapping("/api/v2/review/{reviewId}/reply")
    @Operation(
        summary = "페이지네이션이 적용된 리뷰의 댓글 조회",
        description = "특정 리뷰에 달린 댓글들을 조회할 수 있습니다."
    )
    fun getCursorReplies(
        @PathVariable reviewId: String,
        @RequestParam cursor: String?,
    ): ResponseEntity<GetCursorReplyResponse>{
        val cursorPageable= CursorPageable(sortFieldName = "createdAt", cursor = cursor)
        val reviews = replyService.getRepliesWithCursor(reviewId, cursorPageable)
        return ResponseEntity.ok(reviews)
    }

    @PostMapping("/api/v1/review/{reviewId}/reply")
    @Operation(
        summary = "댓글 작성",
    )
    fun createReply(
        @RequestBody request: CreateReplyRequest,
        @PathVariable reviewId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<CreateReplyResponse> {
        val reply = replyService.createReply(userDetails.getUserId(), reviewId, request.content)
        return ResponseEntity.status(201).body(reply)
    }

    @PutMapping("/api/v1/reply/{replyId}")
    @Operation(
        summary = "댓글 수정",
    )
    fun editReply(
        @RequestBody request: EditReplyRequest,
        @PathVariable replyId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<EditReplyResponse> {
        val reply = replyService.editReply(userDetails.getUserId(), replyId, request.content)
        return ResponseEntity.ok(reply)
    }

    @DeleteMapping("/api/v1/reply/{replyId}")
    @Operation(
        summary = "댓글 삭제",
    )
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
