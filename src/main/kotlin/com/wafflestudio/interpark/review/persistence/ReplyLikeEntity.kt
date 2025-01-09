package com.wafflestudio.interpark.review.persistence

import com.wafflestudio.interpark.user.persistence.UserEntity
import jakarta.persistence.*
import java.time.Instant

@Entity(name = "reply_like")
@Table(
    name = "reply_like",
    uniqueConstraints = [UniqueConstraint(columnNames = ["reply_id", "user_id"])],
    indexes = [
        Index(name = "idx_reply_user", columnList = "reply_id, user_id"),
    ],
)
class ReplyLikeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", nullable = false)
    var reply: ReplyEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,
)
