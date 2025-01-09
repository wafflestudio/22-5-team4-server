package com.wafflestudio.interpark.seat.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository : JpaRepository<ReservationEntity, String> {
    fun findByUserId(userId: String): List<ReservationEntity>

    fun findByPerformanceEventIdAndReservedIsFalse(performanceEventId: String): List<ReservationEntity>
}