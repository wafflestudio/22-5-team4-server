package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.service.PerformanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class PerformanceController(
    private val performanceService: PerformanceService,
) {
    @GetMapping("v1/performance/search")
    fun searchPerformance(
        @RequestParam keyword: String,
        @RequestParam sortType: Int,
        @RequestParam(required = false) date: LocalDate?,
        @RequestParam(required = false) region: String?,
        @RequestParam(required = false) genre: String?,
    ): ResponseEntity<List<Performance>> {
        val queriedPerformances = performanceService.searchPerformance(keyword, sortType, date, region, genre)
        return ResponseEntity.ok(queriedPerformances)
    }
}
