package com.wafflestudio.interpark.seat.persistence

import com.wafflestudio.interpark.performance.persistence.PerformanceHallEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface SeatRepository : JpaRepository<SeatEntity, String> {
    @Query("SELECT s FROM SeatEntity s WHERE s.performanceHall = :performanceHall AND s.performanceDate = :performanceDate AND s.reserved = false")
    fun findAvailableSeats(
        performanceHall: PerformanceHallEntity,
        performanceDate: LocalDate,
    ): List<SeatEntity>
}