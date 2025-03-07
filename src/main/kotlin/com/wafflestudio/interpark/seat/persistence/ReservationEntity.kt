package com.wafflestudio.interpark.seat.persistence

import com.wafflestudio.interpark.performance.persistence.PerformanceEventEntity
import com.wafflestudio.interpark.user.persistence.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDate

@Entity
@Table(
    name = "reservations",
    uniqueConstraints = [
        UniqueConstraint(name = "UniquePerfEvAndSeat", columnNames = ["performance_event_id", "seat_id"])
    ]
)
class ReservationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    val seat: SeatEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_event_id", nullable = false)
    val performanceEvent: PerformanceEventEntity,
    @Column(name = "reservation_date")
    var reservationDate: LocalDate?,
)
