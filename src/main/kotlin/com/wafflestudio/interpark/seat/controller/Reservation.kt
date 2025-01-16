package com.wafflestudio.interpark.seat.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceEntity
import com.wafflestudio.interpark.performance.persistence.PerformanceEventEntity
import com.wafflestudio.interpark.performance.persistence.PerformanceHallEntity
import com.wafflestudio.interpark.seat.persistence.ReservationEntity
import com.wafflestudio.interpark.seat.persistence.SeatEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class Reservation(
    val id: String,
    val performanceTitle: String,
    val posterUri: String,
    val performanceHallName: String,
    val seat: Seat,
    val performanceStartAt: Instant,
    val performanceEndAt: Instant,
    val reservationDate: LocalDate,
) {
    companion object {
        fun fromEntity(
            reservationEntity: ReservationEntity,
            performanceEntity: PerformanceEntity,
            performanceHallEntity: PerformanceHallEntity,
            seatEntity: SeatEntity,
            performanceEventEntity: PerformanceEventEntity,
        ): Reservation {
            return Reservation(
                id = reservationEntity.id!!,
                performanceTitle = performanceEntity.title,
                posterUri = performanceEntity.posterUri,
                performanceHallName = performanceHallEntity.name,
                seat = Seat.fromEntity(seatEntity),
                performanceStartAt = performanceEventEntity.startAt,
                performanceEndAt = performanceEventEntity.endAt,
                reservationDate = reservationEntity.reservationDate!!,
            )
        }

        fun fromEntityToBriefDetails(
            reservationEntity: ReservationEntity,
            performanceEntity: PerformanceEntity,
            performanceEventEntity: PerformanceEventEntity,
        ): BriefReservation {
            return BriefReservation(
                id = reservationEntity.id!!,
                performanceTitle = performanceEntity.title,
                posterUri = performanceEntity.posterUri,
                performanceDate = performanceEventEntity.startAt.atZone(ZoneId.of("Asia/Seoul")).toLocalDate(),
                reservationDate = reservationEntity.reservationDate!!,
            )
        }
    }
}
