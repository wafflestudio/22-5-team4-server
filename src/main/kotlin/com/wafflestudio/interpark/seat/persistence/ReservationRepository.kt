package com.wafflestudio.interpark.seat.persistence

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface ReservationRepository : JpaRepository<ReservationEntity, String> {
    @Query("SELECT r FROM ReservationEntity r WHERE r.user.id = :userId ORDER BY r.reservationDate DESC")
    fun findByUserId(userId: String): List<ReservationEntity>

    fun findByPerformanceEventId(performanceEventId: String): List<ReservationEntity>
}
