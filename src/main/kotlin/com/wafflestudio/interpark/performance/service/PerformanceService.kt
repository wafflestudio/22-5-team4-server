package com.wafflestudio.interpark.performance.service

import com.wafflestudio.interpark.performance.PerformanceNotFoundException
import com.wafflestudio.interpark.performance.controller.Performance
import com.wafflestudio.interpark.performance.controller.PerformanceEvent
import com.wafflestudio.interpark.performance.controller.PerformanceHall
import com.wafflestudio.interpark.performance.persistence.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

@Service
class PerformanceService(
    private val performanceRepository: PerformanceRepository,
    private val performanceEventRepository: PerformanceEventRepository,
) {
    fun searchPerformance(
        title: String?,
        category: PerformanceCategory?,
    ): List<Performance> {
        // 시작점: 아무 조건이 없는 스펙
        var spec: Specification<PerformanceEntity> = Specification.where(null)

        // title 조건이 있다면 스펙에 and로 연결
        PerformanceSpecifications.withTitle(title)?.let {
            spec = spec.and(it)
        }

        // category 조건이 있다면 스펙에 and로 연결
        PerformanceSpecifications.withCategory(category)?.let {
            spec = spec.and(it)
        }

        // 스펙이 결국 아무 조건도 없으면 -> 전체 검색
        val performanceEntities = performanceRepository.findAll(spec)

        // DTO 변환
        return performanceEntities.map { performanceEntity ->
            val performanceEventEntities = performanceEventRepository.findAllByPerformanceId(performanceEntity.id!!)
            val performanceEvents = if (performanceEventEntities.isEmpty()) {
                null
            } else {
                performanceEventEntities.map { PerformanceEvent.fromEntity(it) }
            }
            val performanceHall = performanceEventEntities.firstOrNull()?.let {
                PerformanceHall.fromEntity(it.performanceHall)
            }

            Performance.fromEntity(
                performanceEntity = performanceEntity,
                performanceHall = performanceHall,
                performanceEvents = performanceEvents
            )
        }
    }

    fun getAllPerformance(): List<Performance> {
        return performanceRepository
            .findAll()
            .map { performanceEntity ->
                val performanceEventEntities = performanceEventRepository.findAllByPerformanceId(performanceEntity.id!!)
                val performanceEvents = performanceEventEntities.map{ PerformanceEvent.fromEntity(it) }
                val performanceHall = PerformanceHall.fromEntity(performanceEventEntities.first().performanceHall)

                Performance.fromEntity(
                    performanceEntity = performanceEntity,
                    performanceHall = performanceHall,
                    performanceEvents = performanceEvents
                )
            };
    }

    fun getPerformanceDetail(performanceId: String): Performance {
        val performanceEntity: PerformanceEntity = performanceRepository.findByIdOrNull(performanceId) ?: throw PerformanceNotFoundException()
        val performanceEventEntities = performanceEventRepository.findAllByPerformanceId(performanceEntity.id!!)
        val performanceEvents = performanceEventEntities.map{ PerformanceEvent.fromEntity(it) }
        val performanceHall = PerformanceHall.fromEntity(performanceEventEntities.first().performanceHall)

        return Performance.fromEntity(performanceEntity, performanceEvents, performanceHall)
    }
    
    fun createPerformance(
        title: String,
        detail: String,
        category: PerformanceCategory,
        posterUri: String,
        backdropImageUri: String,
    ): Performance {
        val newPerformanceEntity: PerformanceEntity = PerformanceEntity(
            id = "",
            title = title,
            detail = detail,
            category = category,
            posterUri = posterUri,
            backdropImageUri = backdropImageUri,
        ).let{
            performanceRepository.save(it)
        }
        return Performance.fromEntity(newPerformanceEntity, null, null)
    }

    fun deletePerformance(performanceId: String) {
        val deletePerformanceEntity: PerformanceEntity =
            performanceRepository.findByIdOrNull(performanceId) ?: throw PerformanceNotFoundException()
        performanceRepository.delete(deletePerformanceEntity)
    }
}
