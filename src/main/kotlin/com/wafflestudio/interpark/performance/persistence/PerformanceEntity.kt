package com.wafflestudio.interpark.performance.persistence

import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "performance")
class PerformanceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,

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
)

enum class PerformanceCategory {
    MUSICAL,
    CONCERT,
    CLASSIC,
    PLAY,
}
