package com.wafflestudio.interpark.seat.persistence

import com.wafflestudio.interpark.user.persistence.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "reservations")
class ReservationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
    @OneToOne
    @JoinColumn(name = "seat_id", nullable = false)
    val seat: SeatEntity,
    @Column(name = "reservation_date")
    val reservationDate: LocalDate,
)