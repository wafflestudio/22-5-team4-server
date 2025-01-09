package com.wafflestudio.interpark.performance.service

import com.wafflestudio.interpark.performance.*
import com.wafflestudio.interpark.performance.controller.*
import com.wafflestudio.interpark.performance.persistence.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

import java.time.Instant

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
    
    fun createPerformanceEvent(
        performanceId: String,
        performanceHallId: String,
        startAt: Instant,
        endAt: Instant,
    ): PerformanceEvent {
        val performanceEntity: PerformanceEntity = performanceRepository.findByIdOrNull(performanceId) ?: throw PerformanceNotFoundException()
        val performanceHallEntity: PerformanceHallEntity = performanceHallRepository.findByIdOrNull(performanceHallId) ?: throw PerformanceHallNotFoundException()
        val newPerformanceEventEntity: PerformanceEventEntity = PerformanceEventEntity(
            id = "",
            performance = performanceEntity,
            performanceHall = performanceHallEntity,
            startAt = startAt,
            endAt = endAt,
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
