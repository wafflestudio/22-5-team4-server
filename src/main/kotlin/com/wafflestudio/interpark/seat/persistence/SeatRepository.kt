package com.wafflestudio.interpark.seat.persistence

import com.wafflestudio.interpark.performance.persistence.PerformanceHallEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SeatRepository : JpaRepository<SeatEntity, String> {
    fun findByPerformanceHall(performanceHall: PerformanceHallEntity): List<SeatEntity>
}
