package com.wafflestudio.interpark.seat.service

import com.wafflestudio.interpark.performance.persistence.PerformanceRepository
import com.wafflestudio.interpark.seat.MultipleHallException
import com.wafflestudio.interpark.seat.PerformanceEventNotFoundException
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
    private val performanceEventRepository: PerformanceEventRepository
) {
    @Transactional
    fun getAvailableSeats(
        performanceEventId: String,
    ): List<Seat> {
        val targetPerformanceEvent = performanceEventRepository.findById(performanceEventId) ?: throw PerformanceEventNotFoundException()
        val availableSeats = reservationRepository.findByPerformanceEvent(targetPerformanceEvent)
            .

        return availableSeats
    }

    @Transactional
    fun reserveSeat(
        user: User,
        seatId: String,
    ):
}