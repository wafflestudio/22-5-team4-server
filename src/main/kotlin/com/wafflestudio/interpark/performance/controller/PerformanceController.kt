package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.service.PerformanceService
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.AuthUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PerformanceController(
    private val performanceService: PerformanceService,
) {
    @GetMapping("/api/v1/performance")
    fun getPerformance(
        @AuthUser user: User,
    ): ResponseEntity<GetPerformanceResponse> {
        // Currently, no search
        val performanceList: List<Performance> = performanceService
            .getAllPerformance();
        return ResponseEntity.ok(performanceList)
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
                    request.posterUri,
                    request.backdropImageUri
                );
        return ResponseEntity.ok(newPerformance)
    }

    @DeleteMapping("/admin/v1/performance/{performanceId}")
    fun deletePerformance(
        @PathVariable performanceId: String
    ): ResponseEntity<String> {
        performanceService.deletePerformance(performanceId)
        return ResponseEntity.noContent().build()
    }

}

typealias GetPerformanceResponse = List<Performance>

data class CreatePerformanceRequest(
    val title: String,
    val detail: String,
    val posterUri: String,
    val backdropImageUri: String,
)

typealias CreatePerformanceResponse = Performance