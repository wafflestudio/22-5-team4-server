package com.wafflestudio.interpark.performance.service

import com.wafflestudio.interpark.performance.*
import com.wafflestudio.interpark.performance.controller.*
import com.wafflestudio.interpark.performance.persistence.*
import com.wafflestudio.interpark.seat.service.SeatCreationService
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

@Service
class PerformanceHallService(
    private val performanceHallRepository: PerformanceHallRepository,
    private val seatCreationService: SeatCreationService
) {
    fun getAllPerformanceHall(): List<PerformanceHall> {
        return performanceHallRepository
            .findAll()
            .map { PerformanceHall.fromEntity(it) };
    }

    fun createPerformanceHall(
        name: String,
        address: String,
        maxAudience: Int,
    ): PerformanceHall {
        if (performanceHallRepository.existsByName(name)) {
            throw PerformanceHallNameConflictException()
        }

        val newPerformanceHallEntity: PerformanceHallEntity = PerformanceHallEntity(
            id = "",
            name = name,
            address = address,
            maxAudience = maxAudience,
        ).let{
            performanceHallRepository.save(it)
        }
        seatCreationService.createSeats(newPerformanceHallEntity.id!!, "DEFAULT")
        return PerformanceHall.fromEntity(newPerformanceHallEntity)
    }

    fun deletePerformanceHall(performanceHallId: String) {
        val deletePerformanceHallEntity: PerformanceHallEntity =
            performanceHallRepository.findByIdOrNull(performanceHallId) ?: throw PerformanceHallNotFoundException()
        performanceHallRepository.delete(deletePerformanceHallEntity)
    }
}
