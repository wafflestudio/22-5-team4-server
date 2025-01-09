package com.wafflestudio.interpark.seat.service

import com.wafflestudio.interpark.performance.persistence.PerformanceRepository
import com.wafflestudio.interpark.seat.PerformanceNotFoundException
import com.wafflestudio.interpark.seat.controller.Seat
import com.wafflestudio.interpark.seat.persistence.ReservationRepository
import com.wafflestudio.interpark.seat.persistence.SeatRepository
import com.wafflestudio.interpark.user.controller.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SeatService(
    private val reservationRepository: ReservationRepository,
    private val seatRepository: SeatRepository,
    private val performanceRepository: PerformanceRepository,
) {
    @Transactional
    fun getAvailableSeats(
        performanceId: String,
        performanceDate: LocalDate,
    ): List<Seat> {
        val targetPerformance = performanceRepository.findByIdOrNull(performanceId) ?: throw PerformanceNotFoundException()
        val performanceHallId = targetPerformance.hall
        val availableSeats: List<Seat> =
            seatRepository
                .findAvailableSeats(performanceHall = performanceHallId, performanceDate = performanceDate)
                .map {Seat.fromEntity(it)}
        return availableSeats
    }

    @Transactional
    fun reserveSeat(
        user: User,
        seatId: String,
    ):
}