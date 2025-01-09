package com.wafflestudio.interpark.performance.service

import com.wafflestudio.interpark.performance.*
import com.wafflestudio.interpark.performance.controller.*
import com.wafflestudio.interpark.performance.persistence.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

@Service
class PerformanceService(
    private val performanceRepository: PerformanceRepository,
) {
    fun getAllPerformance(): List<Performance> {
        return performanceRepository
            .findAll()
            .map { Performance.fromEntity(it) };
    }
    
    fun createPerformance(
        title: String,
        detail: String,
        posterUri: String,
        backdropImageUri: String,
    ): Performance {
        val newPerformanceEntity: PerformanceEntity = PerformanceEntity(
            id = "",
            title = title,
            detail = detail,
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
