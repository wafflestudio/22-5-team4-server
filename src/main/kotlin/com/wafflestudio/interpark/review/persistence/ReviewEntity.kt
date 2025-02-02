package com.wafflestudio.interpark.review.persistence

import com.wafflestudio.interpark.performance.persistence.PerformanceEntity
import com.wafflestudio.interpark.user.persistence.UserEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "reviews")
class ReviewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val author: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    val performance: PerformanceEntity,

    @Column(name = "rating", nullable = false)
    var rating: Int,

    @Column(name = "title", nullable = false, length = 255)
    var title: String,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),

    @OneToMany(mappedBy = "review")
    var reviewLikes: List<ReviewLikeEntity> = emptyList(),

    @OneToMany(mappedBy = "review", cascade = [CascadeType.ALL], orphanRemoval = true)
    var replies: MutableSet<ReplyEntity> = mutableSetOf(),
)
