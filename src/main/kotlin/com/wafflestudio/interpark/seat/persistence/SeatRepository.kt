package com.wafflestudio.interpark.seat.persistence

import com.wafflestudio.interpark.performance.persistence.PerformanceHallEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface SeatRepository : JpaRepository<SeatEntity, String> {
    fun findByPerformanceHall(performanceHall: PerformanceHallEntity): List<SeatEntity>
}