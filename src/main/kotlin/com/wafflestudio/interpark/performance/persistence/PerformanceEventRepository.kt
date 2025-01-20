package com.wafflestudio.interpark.performance.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface PerformanceEventRepository : JpaRepository<PerformanceEventEntity, String> {
    fun findAllByPerformanceId(performanceId: String): List<PerformanceEventEntity>

    @Query(
        """
    SELECT e 
    FROM PerformanceEventEntity e
    WHERE e.performance.id = :performanceId
      AND e.startAt >= :startTime
      AND e.startAt < :endTime
    """
    )
    fun findByPerformanceIdAndDate(performanceId: String, startTime: Instant, endTime: Instant): List<PerformanceEventEntity>
}
