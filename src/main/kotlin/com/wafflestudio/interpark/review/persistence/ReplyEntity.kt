package com.wafflestudio.interpark.review.persistence

import com.wafflestudio.interpark.user.persistence.UserEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "reply")
data class ReplyEntity(
    @Id
    val id: String,

    @Column(name = "review_id", nullable = false)
    val reviewId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val author: UserEntity,

    @Column(name = "content", nullable = false)
    val content: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "modified_at", nullable = false)
    val modifiedAt: Instant
)
