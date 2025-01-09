package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceEventEntity
import java.time.Instant

data class PerformanceEvent(
    val id: String,
    val performanceId: String,
    val performanceHallId: String,
    val startAt: Instant,
    val endAt: Instant,
) {

    companion object {
        fun fromEntity(entity: PerformanceEventEntity): PerformanceEvent {
            return PerformanceEvent(
                id = entity.id!!,
                performanceId = entity.performance.id!!,
                performanceHallId = entity.performanceHall.id!!,
                startAt = entity.startAt,
                endAt = entity.endAt,
            )
        }
    }
}
