package com.wafflestudio.interpark.review.persistence

import com.wafflestudio.interpark.user.persistence.UserEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "reply")
data class ReplyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    val review: ReviewEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val author: UserEntity,

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "modified_at", nullable = false)
    var updatedAt: Instant
)
