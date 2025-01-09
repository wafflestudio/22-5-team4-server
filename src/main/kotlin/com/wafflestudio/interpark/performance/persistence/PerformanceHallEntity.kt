package com.wafflestudio.interpark.performance.persistence

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "performance_hall")
data class PerformanceHallEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "address", nullable = false)
    val address: String,

    @Column(name = "max_audience", nullable = false)
    val maxAudience: Int,
)
