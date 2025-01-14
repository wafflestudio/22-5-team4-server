package com.wafflestudio.interpark.seat.persistence

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface ReservationRepository : JpaRepository<ReservationEntity, String> {
    fun findByUserId(userId: String): List<ReservationEntity>

    fun findByPerformanceEventIdAndReservedIsFalse(performanceEventId: String): List<ReservationEntity>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReservationEntity r WHERE r.id = :id")
    fun findByIdWithWriteLock(id: String): ReservationEntity?
}
