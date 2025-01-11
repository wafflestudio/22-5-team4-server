package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
import com.wafflestudio.interpark.performance.persistence.PerformanceEntity
import com.wafflestudio.interpark.performance.persistence.PerformanceEventEntity
import com.wafflestudio.interpark.performance.persistence.PerformanceHallEntity
import java.time.LocalDate

data class Performance(
    val id: String,
    val title: String,
    val hallName: String,
    val performanceDates: List<LocalDate>,
    val detail: String,
    val category: PerformanceCategory,
    val posterUri: String,
    val backdropImageUri: String,
    // 추후 제공 예정
    // val performanceHallList: List<PerformanceHall>,
    // val performanceEventList: List<PerformanceEvent>,
) {
    companion object {
        fun fromEntity(
            performanceEntity: PerformanceEntity,
            performanceEventEntities: List<PerformanceEventEntity>,
            performanceHallEntity: PerformanceHallEntity,
        ): Performance {
            return Performance(
                id = performanceEntity.id!!,
                title = performanceEntity.title,
                hallName = performanceHallEntity.name,
                performanceDates = performanceEventEntities.map {
                    it.startAt.atZone(java.time.ZoneId.of("Asia/Seoul")).toLocalDate()
                }.distinct(),
                detail = performanceEntity.detail,
                category = performanceEntity.category,
                posterUri = performanceEntity.posterUri,
                backdropImageUri = performanceEntity.backdropImageUri
            )
        }
    }
}
