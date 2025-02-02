package com.wafflestudio.interpark.performance.persistence

import com.wafflestudio.interpark.review.persistence.ReviewEntity
import com.wafflestudio.interpark.seat.persistence.SeatEntity
import jakarta.persistence.*
import java.time.Instant

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

    @OneToMany(mappedBy = "performanceHall", cascade = [CascadeType.ALL], orphanRemoval = true)
    var performanceEvents: MutableSet<PerformanceEventEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "performanceHall", cascade = [CascadeType.ALL], orphanRemoval = true)
    var seats: MutableSet<SeatEntity> = mutableSetOf(),
)
