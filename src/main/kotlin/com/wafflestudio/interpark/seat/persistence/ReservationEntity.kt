package com.wafflestudio.interpark.seat.persistence

import com.wafflestudio.interpark.user.persistence.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "reservations")
class ReservationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    val seat: SeatEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_event_id")
    val performanceEvent: PerformanceEventEntity,
    @Column(name = "reservation_date")
    val reservationDate: LocalDate,
    @Column(name = "reserved")
    val reserved: Boolean = false,
)