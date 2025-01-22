package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.review.*
import com.wafflestudio.interpark.review.service.ReplyService
import com.wafflestudio.interpark.user.AuthUser
import com.wafflestudio.interpark.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ReplyController(
    private val replyService: ReplyService,
) {
    @GetMapping("/api/v1/me/reply")
    fun getRepliesByUser(
        @AuthUser user: User
    ): ResponseEntity<GetReplyResponse>{
        val replies = replyService.getRepliesByUser(user)
        return ResponseEntity.ok(replies)
    }

    @GetMapping("/api/v1/review/{reviewId}/reply")
    fun getReplies(
        @PathVariable reviewId: String,
    ): ResponseEntity<GetReplyResponse>{
        val replies = replyService.getReplies(reviewId)
        return ResponseEntity.ok(replies)
    }

    @PostMapping("/api/v1/review/{reviewId}/reply")
    fun createReply(
        @RequestBody request: CreateReplyRequest,
        @PathVariable reviewId: String,
        @AuthUser user: User,
    ): ResponseEntity<CreateReplyResponse> {
        val reply = replyService.createReply(user, reviewId, request.content)
        return ResponseEntity.status(201).body(reply)
    }

    @PutMapping("/api/v1/reply/{replyId}")
    fun editReply(
        @RequestBody request: EditReplyRequest,
        @PathVariable replyId: String,
        @AuthUser user: User,
    ): ResponseEntity<EditReplyResponse> {
        val reply = replyService.editReply(user, replyId, request.content)
        return ResponseEntity.ok(reply)
    }

    @DeleteMapping("/api/v1/reply/{replyId}")
    fun deleteReply(
        @PathVariable replyId: String,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        replyService.deleteReply(user, replyId)
        return ResponseEntity.noContent().build()
    }

}


typealias GetReplyResponse = List<Reply>


data class CreateReplyRequest(
    val content: String,
)

typealias CreateReplyResponse = Reply

data class EditReplyRequest(
    val content: String,
)

typealias EditReplyResponse = Reply
