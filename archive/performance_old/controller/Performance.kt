package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceEntity
import java.time.LocalDate

data class Performance(
    val id: String,
    val title: String,
    val hallName: String,
    val dates: List<LocalDate>,
    val genre: String,
    val detail: String,
    val sales: Int,
    val posterUrl: String,
    val backdropUrl: String,
) {
    companion object {
        fun fromEntity(entity: PerformanceEntity): Performance {
            return Performance(
                id = entity.id ?: "",
                title = entity.title,
                hallName = entity.hall.name,
                dates = entity.dates,
                genre = entity.genre,
                detail = entity.detail ?: "",
                sales = entity.sales,
                posterUrl = entity.posterUrl,
                backdropUrl = entity.backdropUrl,
            )
        }
    }
}
