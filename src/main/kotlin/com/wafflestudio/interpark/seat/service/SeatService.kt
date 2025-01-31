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
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.CacheEvict
import org.redisson.api.RedissonClient
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@Service
class SeatService(
    private val reservationRepository: ReservationRepository,
    private val seatRepository: SeatRepository,
    private val performanceEventRepository: PerformanceEventRepository,
    private val userRepository: UserRepository,
    private val redissonClient: RedissonClient,
) {
    @Transactional(readOnly = true)
    @Cacheable(value = ["availableSeats"], key = "#performanceEventId")
    fun getAvailableSeats(performanceEventId: String): List<Seat> {
        val performanceEvent = performanceEventRepository.findByIdOrNull(performanceEventId) ?: throw PerformanceEventNotFoundException()
        val reservedSeats = reservationRepository.findByPerformanceEventId(performanceEventId).map { it.seat }
        val allSeats = seatRepository.findByPerformanceHall(performanceEvent.performanceHall)
        val availableSeats = allSeats.filter { it !in reservedSeats }.map { Seat.fromEntity(it) }
        return availableSeats
    }

    @Transactional
    @CacheEvict(value = ["availableSeats"], key = "#performanceEventId")
    fun reserveSeat(
        userId: String,
        performanceEventId: String,
        seatId: String,
    ): String {
        val lockKey = "reservation_lock:$performanceEventId:$seatId"
        val valueKey = "reservation_status:$performanceEventId:$seatId"
        val lock = redissonClient.getLock(lockKey)
        val bucket = redissonClient.getBucket<String>(valueKey)
        var isLocked = false // 🔹 락 획득 여부 추적
        try {
            // 락 획득 시도 (최대 10초 대기, 30초 후 자동 해제)
            // 만에 하나 좌석의 lock을 점유한 채로 이 인스턴스가 죽어도, redis에서 자체적으로 락을 해제함
            // 일반적으로는 노드 롤링 등의 이슈로는 graceful shutdown을 지원하기에 문제가 발생할 일은 없을 것으로 보임
            isLocked = lock.tryLock(0, 30, TimeUnit.SECONDS)
            if (!isLocked || bucket.isExists) {
                // TODO: Redis 전용 예외 구현, 전역핸들러에 등록
                throw ReservedAlreadyException()
            }

            // 락이 걸린 상태에서 값을 설정
            val targetUser = userRepository.findByIdOrNull(userId) ?: throw AuthenticateException()
            val targetSeat = seatRepository.findByIdOrNull(seatId) ?: throw SeatNotFoundException()
            val targetPerformanceEvent = performanceEventRepository.findByIdOrNull(performanceEventId)
                ?: throw PerformanceEventNotFoundException()

            if (targetSeat.performanceHall != targetPerformanceEvent.performanceHall) {
                throw WrongSeatException()
            }

            val newReservation = ReservationEntity(
                user = targetUser,
                seat = targetSeat,
                performanceEvent = targetPerformanceEvent,
                reservationDate = LocalDate.now(),
            )

            val savedReservationId = try {
                reservationRepository.save(newReservation).id!!
            } catch (e: DataIntegrityViolationException) {
                throw ReservedAlreadyException()
            }

            // 예약 성공 시 Redis에 상태 저장 (TTL: 1시간)
            bucket.set("RESERVED", 1, TimeUnit.HOURS)

            return savedReservationId

        } finally {
            if (isLocked) { // 🔹 락을 획득한 경우에만 해제
                lock.unlock()
            }
        }
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
        val performanceEventId = reservationEntity.performanceEvent.id!!
        val seatId = reservationEntity.seat.id!!
        val valueKey = "reservation_status:$performanceEventId:$seatId"

        reservationRepository.delete(reservationEntity)
        redissonClient.getBucket<String>(valueKey).delete()
    }
}
