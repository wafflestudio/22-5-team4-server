package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceHallEntity
import java.time.LocalDate

data class PerformanceHall(
    val id: String,
    val name: String,
    val address: String,
    val maxAudience: Int,
) {
    companion object {
        fun fromEntity(entity: PerformanceHallEntity): PerformanceHall {
            return PerformanceHall(
                id = entity.id!!,
                name = entity.name,
                address = entity.address,
                maxAudience = entity.maxAudience,
            )
        }
    }
}
