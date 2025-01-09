package com.wafflestudio.interpark.performance.persistence

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "performance")
data class PerformanceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "detail", nullable = false)
    val detail: String,

    @Column(name = "poster_uri", nullable = false)
    val posterUri: String,

    @Column(name = "backdrop_image_uri", nullable = false)
    val backdropImageUri: String,
)
