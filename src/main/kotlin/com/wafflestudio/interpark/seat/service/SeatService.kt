package com.wafflestudio.interpark.seat.service

import com.wafflestudio.interpark.performance.PerformanceEventNotFoundException
import com.wafflestudio.interpark.performance.persistence.PerformanceEventRepository
import com.wafflestudio.interpark.seat.ReservationNotFoundException
import com.wafflestudio.interpark.seat.ReservationPermissionDeniedException
import com.wafflestudio.interpark.seat.ReservedAlreadyException
import com.wafflestudio.interpark.seat.ReservedYetException
import com.wafflestudio.interpark.seat.SeatNotFoundException
import com.wafflestudio.interpark.seat.WrongSeatException
import com.wafflestudio.interpark.seat.controller.BriefReservation
import com.wafflestudio.interpark.seat.controller.Reservation
import com.wafflestudio.interpark.seat.controller.Seat
import com.wafflestudio.interpark.seat.persistence.ReservationEntity
import com.wafflestudio.interpark.seat.persistence.ReservationRepository
import com.wafflestudio.interpark.seat.persistence.SeatRepository
import com.wafflestudio.interpark.user.AuthenticateException
import com.wafflestudio.interpark.user.persistence.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SeatService(
    private val reservationRepository: ReservationRepository,
    private val seatRepository: SeatRepository,
    private val performanceEventRepository: PerformanceEventRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun getAvailableSeats(performanceEventId: String): List<Seat> {
        val performanceEvent = performanceEventRepository.findByIdOrNull(performanceEventId) ?: throw PerformanceEventNotFoundException()
        val reservedSeats = reservationRepository.findByPerformanceEventId(performanceEventId).map { it.seat }
        val allSeats = seatRepository.findByPerformanceHall(performanceEvent.performanceHall)
        val availableSeats = allSeats.filter { it !in reservedSeats }.map { Seat.fromEntity(it) }
        return availableSeats
    }

    @Transactional
    fun reserveSeat(
        userId: String,
        performanceEventId: String,
        seatId: String,
    ): String {
        val targetUser = userRepository.findByIdOrNull(userId) ?: throw AuthenticateException()
        val targetSeat = seatRepository.findByIdOrNull(seatId) ?: throw SeatNotFoundException()
        val targetPerformanceEvent = performanceEventRepository.findByIdOrNull(performanceEventId) ?: throw PerformanceEventNotFoundException()
        if(targetSeat.performanceHall != targetPerformanceEvent.performanceHall) { throw WrongSeatException() }
        val newReservation = ReservationEntity(
            user = targetUser,
            seat = targetSeat,
            performanceEvent = targetPerformanceEvent,
            reservationDate = LocalDate.now(),
        )

        val savedReservationId = try {
            reservationRepository.saveAndFlush(newReservation).id!!
        } catch (e: DataIntegrityViolationException) {
            throw ReservedAlreadyException()
        }

        return savedReservationId
    }

    @Transactional
    fun getMyReservations(userId: String): List<BriefReservation> {
        userRepository.findByIdOrNull(userId) ?: throw AuthenticateException()
        val myReservations = reservationRepository.findByUserId(userId)

        return myReservations.map { reservationEntity ->
            val performanceEventEntity = reservationEntity.performanceEvent
            val performanceEntity = performanceEventEntity.performance

            Reservation.fromEntityToBriefDetails(
                reservationEntity = reservationEntity,
                performanceEntity = performanceEntity,
                performanceEventEntity = performanceEventEntity
            )
        }
    }

    @Transactional
    fun getReservedSeatDetail(
        userId: String,
        reservationId: String,
    ): Reservation {
        val reservationEntity = reservationRepository.findByIdOrNull(reservationId) ?: throw ReservationNotFoundException()
        val userEntity = userRepository.findByIdOrNull(userId) ?: throw AuthenticateException()
        val reservationUser = reservationEntity.user ?: throw ReservedYetException()
        if (reservationUser.id != userEntity.id) {
            throw ReservationPermissionDeniedException()
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
        userId: String,
        reservationId: String,
    ) {
        val reservationEntity = reservationRepository.findByIdOrNull(reservationId) ?: throw ReservationNotFoundException()
        val userEntity = userRepository.findByIdOrNull(userId) ?: throw AuthenticateException()
        val reservationUser = reservationEntity.user ?: throw ReservedYetException()
        if (reservationUser.id != userEntity.id) {
            throw ReservationPermissionDeniedException()
        }

        reservationRepository.delete(reservationEntity)
    }
}
