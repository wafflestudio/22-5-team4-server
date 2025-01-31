package com.wafflestudio.interpark.review.controller

import com.wafflestudio.interpark.review.persistence.ReplyEntity
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

data class Reply(
    val id: String,
    val author: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(entity: ReplyEntity): Reply {
            return Reply(
                id = entity.id!!,
                author = entity.author.nickname,
                content = entity.content,
                createdAt = convertInstantToKoreanTime(entity.createdAt),
                updatedAt = convertInstantToKoreanTime(entity.updatedAt),
            )
        }

        private fun convertInstantToKoreanTime(instant: Instant): LocalDateTime {
            return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"))
        }
    }
}