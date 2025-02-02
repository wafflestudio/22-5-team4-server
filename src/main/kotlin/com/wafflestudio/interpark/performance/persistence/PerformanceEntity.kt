package com.wafflestudio.interpark.performance.persistence

import com.wafflestudio.interpark.review.persistence.ReplyEntity
import com.wafflestudio.interpark.review.persistence.ReviewEntity
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "performance")
class PerformanceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "detail", nullable = false)
    val detail: String,
  
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var category: PerformanceCategory,

    @Column(name = "poster_uri", nullable = false)
    val posterUri: String,

    @Column(name = "backdrop_image_uri", nullable = false)
    val backdropImageUri: String,

    @OneToMany(mappedBy = "performance", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reviews: MutableSet<ReviewEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "performance", cascade = [CascadeType.ALL], orphanRemoval = true)
    var performanceEvents: MutableSet<PerformanceEventEntity> = mutableSetOf(),
)

enum class PerformanceCategory {
    MUSICAL,
    CONCERT,
    CLASSIC,
    PLAY,
}
