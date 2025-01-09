package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
import com.wafflestudio.interpark.performance.service.PerformanceService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PerformanceController(
    private val performanceService: PerformanceService,
) {
    @GetMapping("v1/performance/search")
    @Operation(
        summary = "공연 조회",
        description = "제목과 카테고리에 해당하는 공연들의 리스트를 반환합니다."
    )
    fun searchPerformance(
        @RequestBody request: SearchPerformanceRequest
    ): ResponseEntity<SearchPerformanceResponse> {
        val queriedPerformances = performanceService.searchPerformance(request.title, request.category)
        return ResponseEntity.ok(SearchPerformanceResponse(performances = queriedPerformances))
    }
}

data class SearchPerformanceRequest(
    val title: String?,
    val category: PerformanceCategory?,
)

data class SearchPerformanceResponse(
    val performances: List<Performance>
)
