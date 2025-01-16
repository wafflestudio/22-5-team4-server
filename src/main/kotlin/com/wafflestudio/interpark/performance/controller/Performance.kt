package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
import com.wafflestudio.interpark.performance.persistence.PerformanceEntity
import java.time.LocalDate
import java.time.ZoneId

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
            performanceEvents: List<PerformanceEvent>?,
            performanceHall: PerformanceHall?,
        ): Performance {
            return Performance(
                id = performanceEntity.id!!,
                title = performanceEntity.title,
                hallName = performanceHall?.name ?: "",
                performanceDates = performanceEvents?.map {
                    it.startAt.atZone(ZoneId.of("Asia/Seoul")).toLocalDate()
                }?.distinct() ?: emptyList(),
                detail = performanceEntity.detail,
                category = performanceEntity.category,
                posterUri = performanceEntity.posterUri,
                backdropImageUri = performanceEntity.backdropImageUri
            )
        }

        fun fromEntityToBriefDetails(
            performanceEntity: PerformanceEntity,
            performanceEvents: List<PerformanceEvent>?,
            performanceHall: PerformanceHall?,
        ): BriefPerformanceDetail {
            return BriefPerformanceDetail(
                id = performanceEntity.id!!,
                title = performanceEntity.title,
                hallName = performanceHall?.name ?: "",
                performanceDuration = if (!performanceEvents.isNullOrEmpty()) {
                    val seoulZone = ZoneId.of("Asia/Seoul")

                    val minDate = performanceEvents.minOf { it.startAt }.atZone(seoulZone).toLocalDate()
                    val maxDate = performanceEvents.maxOf { it.startAt }.atZone(seoulZone).toLocalDate()

                    Pair(minDate, maxDate)
                } else {
                    null
                }
            )
        }
    }
}
