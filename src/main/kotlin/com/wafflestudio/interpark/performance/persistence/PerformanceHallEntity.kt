package com.wafflestudio.interpark.performance.persistence

import jakarta.persistence.*

@Entity
@Table(name = "performance_hall")
class PerformanceHallEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "address", nullable = false)
    val address: String,

    @Column(name = "max_audience", nullable = false)
    val maxAudience: Int,
)
