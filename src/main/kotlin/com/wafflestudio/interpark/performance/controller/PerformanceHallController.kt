package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.service.PerformanceHallService
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class PerformanceHallController(
    private val performanceHallService: PerformanceHallService,
) {
    @GetMapping("/api/v1/performance-hall")
    @Operation(
        summary = "전체 공연장 조회",
    )
    fun getPerformanceHall(
    ): ResponseEntity<GetPerformanceHallResponse> {
        // Currently, no search
        val performanceHallList: List<PerformanceHall> = performanceHallService
            .getAllPerformanceHall()
        return ResponseEntity.ok(performanceHallList)
    }
    
    // WARN: THIS IS FOR ADMIN.
    // TODO: SEPERATE THIS TO OTHER APPLICATION
    @PostMapping("/admin/v1/performance-hall")
    @Operation(
        summary = "공연장 생성",
    )
    fun createPerformanceHall(
        @RequestBody request: CreatePerformanceHallRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): ResponseEntity<CreatePerformanceHallResponse> {
        val newPerformanceHall: PerformanceHall =
            performanceHallService
                .createPerformanceHall(
                    request.name,
                    request.address,
                    request.maxAudience,
                )
        return ResponseEntity.status(HttpStatus.CREATED).body(newPerformanceHall)
    }

    @DeleteMapping("/admin/v1/performance-hall/{performanceHallId}")
    @Operation(
        summary = "공연장 삭제",
    )
    fun deletePerformance(
        @PathVariable performanceHallId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): ResponseEntity<String> {
        performanceHallService.deletePerformanceHall(performanceHallId)
        return ResponseEntity.noContent().build()
    }

}

typealias GetPerformanceHallResponse = List<PerformanceHall>

data class CreatePerformanceHallRequest(
    val name: String,
    val address: String,
    val maxAudience: Int,
)

typealias CreatePerformanceHallResponse = PerformanceHall