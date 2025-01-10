package com.wafflestudio.interpark.seat.persistence

import com.wafflestudio.interpark.performance.persistence.PerformanceHallEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "seats")
class SeatEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_hall_id", nullable = false)
    val performanceHall: PerformanceHallEntity,
    @Column(name = "seat_number", nullable = false)
    val seatNumber: Pair<Int, Int>,
    @Column(name = "price")
    var price: Int = 10000,
)