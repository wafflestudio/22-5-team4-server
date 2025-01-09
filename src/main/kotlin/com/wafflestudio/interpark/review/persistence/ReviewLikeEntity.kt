package com.wafflestudio.interpark.review.persistence

import com.wafflestudio.interpark.user.persistence.UserEntity
import jakarta.persistence.*
import java.time.Instant

@Entity(name = "review_like")
@Table(
    name = "review_like",
    uniqueConstraints = [UniqueConstraint(columnNames = ["review_id", "user_id"])],
    indexes = [
        Index(name = "idx_review_user", columnList = "review_id, user_id"),
    ],
)
class ReviewLikeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    var review: ReviewEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,
)
