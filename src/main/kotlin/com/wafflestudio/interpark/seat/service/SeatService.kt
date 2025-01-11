package com.wafflestudio.interpark.seat.service

import com.wafflestudio.interpark.seat.ReservationNotFoundException
import com.wafflestudio.interpark.seat.ReservedAlreadyException
import com.wafflestudio.interpark.seat.ReservedYetException
import com.wafflestudio.interpark.seat.controller.Reservation
import com.wafflestudio.interpark.seat.controller.Seat
import com.wafflestudio.interpark.seat.persistence.ReservationRepository
import com.wafflestudio.interpark.seat.persistence.SeatRepository
import com.wafflestudio.interpark.user.AuthenticateException
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.persistence.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SeatService(
    private val reservationRepository: ReservationRepository,
    private val seatRepository: SeatRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun getAvailableSeats(performanceEventId: String): List<Pair<String, Seat>> {
        val availableReservations = reservationRepository.findByPerformanceEventIdAndReservedIsFalse(performanceEventId)
        val availableSeats = availableReservations.map { Pair(it.id!!, Seat.fromEntity(it.seat)) }
        return availableSeats
    }

    @Transactional
    fun reserveSeat(
        user: User,
        reservationId: String,
    ): String {
        // TODO: 동시성 처리하기
        val targetUser = userRepository.findByIdOrNull(user.id) ?: throw AuthenticateException()
        val targetReservation = reservationRepository.findByIdOrNull(reservationId) ?: throw ReservationNotFoundException()

        if (targetReservation.reserved) throw ReservedAlreadyException()

        targetReservation.user = targetUser
        targetReservation.reserved = true
        targetReservation.reservationDate = LocalDate.now()
        reservationRepository.save(targetReservation)

        return reservationId
    }

    @Transactional
    fun getReservedSeatDetail(
        user: User,
        reservationId: String,
    ): Reservation {
        val reservationEntity = reservationRepository.findByIdOrNull(reservationId) ?: throw ReservationNotFoundException()
        val userEntity = userRepository.findByIdOrNull(user.id) ?: throw AuthenticateException()
        val reservationUser = reservationEntity.user ?: throw ReservedYetException()
        if (reservationUser.id != userEntity.id) {
            throw AuthenticateException()
        }

        val seatEntity = reservationEntity.seat
        val performanceHallEntity = seatEntity.performanceHall
        val performanceEventEntity = reservationEntity.performanceEvent
        val performanceEntity = performanceEventEntity.performance

        val reservation = Reservation.fromEntity(
            reservationEntity = reservationEntity,
            performanceEntity = performanceEntity,
            performanceHallEntity = performanceHallEntity,
            seatEntity = seatEntity,
            performanceEventEntity = performanceEventEntity,
        )

        return reservation
    }

    @Transactional
    fun cancelReservedSeat(
        user: User,
        reservationId: String,
    ) {
        val reservationEntity = reservationRepository.findByIdOrNull(reservationId) ?: throw ReservationNotFoundException()
        val userEntity = userRepository.findByIdOrNull(user.id) ?: throw AuthenticateException()
        val reservationUser = reservationEntity.user ?: throw ReservedYetException()
        if (reservationUser.id != userEntity.id) {
            throw AuthenticateException()
        }

        reservationEntity.user = null
        reservationEntity.reservationDate = null
        reservationEntity.reserved = false
    }
}
