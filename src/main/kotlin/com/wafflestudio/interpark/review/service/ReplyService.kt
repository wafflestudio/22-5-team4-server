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
class ReplyService(
    private val entityManager: EntityManager,
    private val reviewRepository: ReviewRepository,
    private val replyRepository: ReplyRepository,
    private val userRepository: UserRepository,
) {

    fun getRepliesByUser(user: User): List<Reply> {
        val authorId = user.id
        val replies: List<Reply> = 
            replyRepository
                .findByAuthorId(authorId)
                .map { Reply.fromEntity(it) }
        return replies
    }

    fun getReplies(reviewId: String): List<Reply> {
        val replies: List<Reply> =
            replyRepository
                .findByReviewId(reviewId)
                .map { Reply.fromEntity(it) }
        return replies
    }

    fun countReplies(reviewId: String): Int {
        val replyCount = replyRepository.countByReviewId(reviewId)
        return replyCount
    }

    @Transactional
    fun createReply(
        author: User,
        reviewId: String,
        content: String,
    ): Reply {
        validateContent(content)
        val authorEntity = userRepository.findByIdOrNull(author.id) ?: throw AuthenticateException()
        val reviewEntity = reviewRepository.findByIdOrNull(reviewId) ?: throw ReviewNotFoundException()
        val replyEntity =
            ReplyEntity(
                id = "",
                author = authorEntity,
                review = reviewEntity,
                content = content,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            ).let {
                replyRepository.save(it)
            }
        return Reply.fromEntity(replyEntity)
    }

    @Transactional
    fun editReply(
        author: User,
        replyId: String,
        content: String,
    ): Reply {
        content?.let { validateContent(it) }
        val replyEntity = replyRepository.findByIdOrNull(replyId) ?: throw ReplyNotFoundException()
        val authorEntity = userRepository.findByIdOrNull(author.id) ?: throw AuthenticateException()
        if (replyEntity.author.id != authorEntity.id) {
            throw ReplyPermissionDeniedException()
        }
        content?.let { replyEntity.content = it }
        replyEntity.updatedAt = Instant.now()
        replyRepository.save(replyEntity)
        return Reply.fromEntity(replyEntity)
    }

    @Transactional
    fun deleteReply(
        author: User,
        replyId: String,
    ) {
        val replyEntity = replyRepository.findByIdOrNull(replyId) ?: throw ReplyNotFoundException()
        val authorEntity = userRepository.findByIdOrNull(author.id) ?: throw AuthenticateException()
        if (replyEntity.author.id != authorEntity.id) {
            throw ReplyPermissionDeniedException()
        }
        replyRepository.delete(replyEntity)
    }

    private fun validateContent(content: String) {
        if (content.isBlank()) {
            throw ReplyContentLengthOutOfRangeException()
        }
        // 임의 길이제한
        if (content.length > 500) {
            throw ReplyContentLengthOutOfRangeException()
        }
    }

    private fun validateRating(rating: Int) {
        if (rating !in 1..5) {
            throw ReviewRatingOutOfRangeException()
        }
    }
}