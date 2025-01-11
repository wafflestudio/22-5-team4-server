package com.wafflestudio.interpark.seat.service

import com.wafflestudio.interpark.performance.PerformanceEventNotFoundException
import com.wafflestudio.interpark.performance.PerformanceHallNotFoundException
import com.wafflestudio.interpark.performance.persistence.PerformanceEventRepository
import com.wafflestudio.interpark.performance.persistence.PerformanceHallRepository
import com.wafflestudio.interpark.seat.InValidHallTypeException
import com.wafflestudio.interpark.seat.persistence.ReservationEntity
import com.wafflestudio.interpark.seat.persistence.ReservationRepository
import com.wafflestudio.interpark.seat.persistence.SeatEntity
import com.wafflestudio.interpark.seat.persistence.SeatRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SeatCreationService(
    private val seatRepository: SeatRepository,
    private val reservationRepository: ReservationRepository,
    private val performanceHallRepository: PerformanceHallRepository,
    private val performanceEventRepository: PerformanceEventRepository,
) {
    @Transactional
    fun createSeats(
        performanceHallId: String,
        type: String?,
    ) {
        val performanceHallEntity = performanceHallRepository.findByIdOrNull(performanceHallId) ?: throw PerformanceHallNotFoundException()
        when (type) {
            "DEFAULT" -> {
                // 10행 10열의 공연장
                for (row in 1..10) {
                    for (col in 1..10) {
                        seatRepository.save(
                            SeatEntity(
                                performanceHall = performanceHallEntity,
                                seatNumber = Pair(row, col),
                                price = 10000,
                            ),
                        )
                    }
                }
            }
            else -> {
                throw InValidHallTypeException()
            }
        }
    }

    @Transactional
    fun createEmptyReservations(performanceEventId: String) {
        val performanceEventEntity = performanceEventRepository.findByIdOrNull(performanceEventId) ?: throw PerformanceEventNotFoundException()
        val seats = seatRepository.findByPerformanceHall(performanceEventEntity.performanceHall)
        val emptyReservations =
            seats.map {
                ReservationEntity(
                    seat = it,
                    performanceEvent = performanceEventEntity,
                    reservationDate = null,
                )
            }
        reservationRepository.saveAll(emptyReservations)
    }
}
