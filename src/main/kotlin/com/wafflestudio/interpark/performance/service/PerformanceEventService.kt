package com.wafflestudio.interpark.performance.service

import com.wafflestudio.interpark.performance.*
import com.wafflestudio.interpark.performance.controller.*
import com.wafflestudio.interpark.performance.persistence.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class PerformanceEventService(
    private val performanceRepository: PerformanceRepository,
    private val performanceHallRepository: PerformanceHallRepository,
    private val performanceEventRepository: PerformanceEventRepository,
) {
    fun getAllPerformanceEvent(): List<PerformanceEvent> {
        return performanceEventRepository
            .findAll()
            .map { PerformanceEvent.fromEntity(it) };
    }

    fun parseKoreanTimeToInstant(koreanTime: String): Instant {
        val koreanZone = ZoneId.of("Asia/Seoul")
        return LocalDateTime.parse(koreanTime).atZone(koreanZone).toInstant()
    }

    fun createPerformanceEvent(
        performanceId: String,
        performanceHallId: String,
        startAt: String,
        endAt: String,
    ): PerformanceEvent {
        val performanceEntity: PerformanceEntity = performanceRepository.findByIdOrNull(performanceId)
                                                                        ?: throw PerformanceNotFoundException()
        val performanceHallEntity: PerformanceHallEntity = performanceHallRepository.findByIdOrNull(performanceHallId)
                                                                        ?: throw PerformanceHallNotFoundException()
        val newPerformanceEventEntity: PerformanceEventEntity = PerformanceEventEntity(
            id = "",
            performance = performanceEntity,
            performanceHall = performanceHallEntity,
            startAt = parseKoreanTimeToInstant(startAt),
            endAt = parseKoreanTimeToInstant(endAt),
        ).let{
            performanceEventRepository.save(it)
        }
        return PerformanceEvent.fromEntity(newPerformanceEventEntity)
    }

    fun deletePerformanceEvent(performanceEventId: String) {
        val deletePerformanceEventEntity: PerformanceEventEntity =
            performanceEventRepository.findByIdOrNull(performanceEventId) ?: throw PerformanceEventNotFoundException()
        performanceEventRepository.delete(deletePerformanceEventEntity)
    }
}
