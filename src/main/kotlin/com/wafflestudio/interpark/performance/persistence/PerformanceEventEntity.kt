package com.wafflestudio.interpark.performance.persistence

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "performance_event")
data class PerformanceEventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,

    @ManyToOne
    @JoinColumn(name = "performance_id", nullable = false)
    val performance: PerformanceEntity,

    @ManyToOne
    @JoinColumn(name = "performance_hall_id", nullable = false)
    val performanceHall: PerformanceHallEntity,

    @Column(name = "start_at", nullable = false)
    val startAt: Instant,

    @Column(name = "end_at", nullable = false)
    val endAt: Instant,
)
