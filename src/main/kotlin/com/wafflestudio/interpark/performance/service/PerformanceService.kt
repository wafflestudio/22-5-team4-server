package com.wafflestudio.interpark.performance.service

import com.wafflestudio.interpark.performance.controller.Performance
import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
import com.wafflestudio.interpark.performance.persistence.PerformanceEntity
import com.wafflestudio.interpark.performance.persistence.PerformanceRepository
import com.wafflestudio.interpark.performance.persistence.PerformanceSpecifications
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class PerformanceService(
    private val performanceRepository: PerformanceRepository,
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
        return performanceEntities.map { Performance.fromEntity(it) }
    }

    fun createPerformance(performance: Performance): Performance {
        TODO()
    }

    fun editPerformance(performance: Performance): Performance {
        TODO()
    }

    fun deletePerformance(performance: Performance) {
        TODO()
    }
}
