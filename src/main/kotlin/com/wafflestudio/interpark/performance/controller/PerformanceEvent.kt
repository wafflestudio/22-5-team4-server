package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceEventEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class PerformanceEvent(
    val id: String,
    val performanceId: String,
    val performanceHallId: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
) {

    companion object {
        fun fromEntity(entity: PerformanceEventEntity): PerformanceEvent {
            return PerformanceEvent(
                id = entity.id,
                performanceId = entity.performance.id!!,
                performanceHallId = entity.performanceHall.id!!,
                startAt = convertInstantToKoreanTime(entity.startAt),
                endAt = convertInstantToKoreanTime(entity.endAt),
            )
        }

        private fun convertInstantToKoreanTime(instant: Instant): LocalDateTime {
            return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"))
        }
    }
}
