package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceEntity
import java.time.LocalDate

data class Performance(
    val id: String,
    val title: String,
    val detail: String,
    val posterUri: String,
    val backdropImageUri: String,
    // 추후 제공 예정
    // val performanceHallList: List<PerformanceHall>,
    // val performanceEventList: List<PerformanceEvent>,
) {
    companion object {
        fun fromEntity(entity: PerformanceEntity): Performance {
            return Performance(
                id = entity.id!!,
                title = entity.title,
                detail = entity.detail,
                posterUri = entity.posterUri,
                backdropImageUri = entity.backdropImageUri
            )
        }
    }
}
