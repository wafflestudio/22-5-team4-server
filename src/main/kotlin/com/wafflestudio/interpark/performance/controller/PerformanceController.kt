package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
import com.wafflestudio.interpark.performance.service.PerformanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PerformanceController(
    private val performanceService: PerformanceService,
) {
    @GetMapping("v1/performance/search")
    fun searchPerformance(
        @RequestParam title: String?,
        @RequestParam category: PerformanceCategory?,
    ): ResponseEntity<SearchPerformanceResponse> {
        val queriedPerformances = performanceService.searchPerformance(title, category)
        return ResponseEntity.ok(SearchPerformanceResponse(performances = queriedPerformances))
    }
}

data class SearchPerformanceResponse(
    val performances: List<Performance>
)
