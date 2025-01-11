package com.wafflestudio.interpark.performance.service

import com.wafflestudio.interpark.performance.PerformanceNotFoundException
import com.wafflestudio.interpark.performance.controller.Performance
import com.wafflestudio.interpark.performance.persistence.*
import com.wafflestudio.interpark.user.persistence.UserRepository
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

@Service
class PerformanceService(
    private val performanceRepository: PerformanceRepository,
    private val performanceEventRepository: PerformanceEventRepository,
    private val performanceHallRepository: PerformanceHallRepository,
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
            val performanceEventEntities = performanceEventRepository.findAllByPerformanceId(performanceEntity.id!!) // 관련 이벤트들
            val performanceHallEntity = performanceEventEntities.first().performanceHall // 관련 공연장

            Performance.fromEntity(
                performanceEntity = performanceEntity,
                performanceHallEntity = performanceHallEntity,
                performanceEventEntities = performanceEventEntities
            )
        }
    }

    fun getAllPerformance(): List<Performance> {
        return performanceRepository
            .findAll()
            .map { performanceEntity ->
                val performanceEventEntities = performanceEventRepository.findAllByPerformanceId(performanceEntity.id!!)
                val performanceHallEntity = performanceEventEntities.first().performanceHall

                Performance.fromEntity(
                    performanceEntity = performanceEntity,
                    performanceHallEntity = performanceHallEntity,
                    performanceEventEntities = performanceEventEntities
                )
            };
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
        return Performance.fromEntity(newPerformanceEntity)
    }

    fun deletePerformance(performanceId: String) {
        val deletePerformanceEntity: PerformanceEntity =
            performanceRepository.findByIdOrNull(performanceId) ?: throw PerformanceNotFoundException()
        performanceRepository.delete(deletePerformanceEntity)
    }
}
