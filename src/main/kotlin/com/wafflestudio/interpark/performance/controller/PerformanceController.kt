package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
import io.swagger.v3.oas.annotations.Operation
import com.wafflestudio.interpark.performance.service.PerformanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PerformanceController(
    private val performanceService: PerformanceService,
) {
    @GetMapping("/api/v1/performance/search")
    @Operation(
        summary = "공연 조회",
        description = "제목과 카테고리에 해당하는 공연들의 리스트를 반환합니다."
    )
    fun searchPerformance(
        @RequestParam title: String?,
        @RequestParam category: PerformanceCategory?,
    ): ResponseEntity<SearchPerformanceResponse> {
        val queriedPerformances = performanceService.searchPerformance(title, category)
        return ResponseEntity.ok(queriedPerformances)
    }
    
    // WARN: THIS IS FOR ADMIN.
    // TODO: SEPERATE THIS TO OTHER APPLICATION
    @PostMapping("/admin/v1/performance")
    fun createPerformance(
        @RequestBody request: CreatePerformanceRequest,
    ): ResponseEntity<CreatePerformanceResponse> {
        val newPerformance: Performance =
            performanceService
                .createPerformance(
                    request.title,
                    request.detail,
                    request.category,
                    request.posterUri,
                    request.backdropImageUri
                )
        return ResponseEntity.ok(newPerformance)
    }

    @GetMapping("/api/v1/performance/{performanceId}")
    @Operation(
        summary = "공연 상세정보 반환",
        description = "공연을 선택했을 때 화면에 보여지는 상세 정보를 반환합니다."
    )
    fun getPerformanceDetail(
        @PathVariable performanceId: String,
    ) : ResponseEntity<GetPerformanceDetailResponse> {
        val queriedPerformance = performanceService.getPerformanceDetail(performanceId)
        return ResponseEntity.ok(queriedPerformance)
    }

    @DeleteMapping("/admin/v1/performance/{performanceId}")
    fun deletePerformance(
        @PathVariable performanceId: String
    ): ResponseEntity<String> {
        performanceService.deletePerformance(performanceId)
        return ResponseEntity.noContent().build()
    }

}

typealias SearchPerformanceResponse = List<Performance>

data class CreatePerformanceRequest(
    val title: String,
    val detail: String,
    val category: PerformanceCategory,
    val posterUri: String,
    val backdropImageUri: String,
)

typealias CreatePerformanceResponse = Performance

typealias GetPerformanceDetailResponse = Performance
