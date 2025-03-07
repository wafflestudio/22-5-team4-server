package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.service.PerformanceEventService
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
class PerformanceEventController(
    private val performanceEventService: PerformanceEventService,
) {
    @GetMapping("/api/v1/performance-event")
    @Operation(
        summary = "전체 공연이벤트 조회",
    )
    fun getPerformanceEvent(
    ): ResponseEntity<GetPerformanceEventResponse> {
        // Currently, no search
        val performanceEventList: List<PerformanceEvent> = performanceEventService
            .getAllPerformanceEvent()
        return ResponseEntity.ok(performanceEventList)
    }

    @GetMapping("/api/v1/performance-event/{performanceId}/{performanceDate}")
    @Operation(
        summary = "특정 공연의 공연이벤트 조회",
        description = "공연와 특정 공연일이 주어졌을 때 실제 이루어지는 공연들의 정보를 반환합니다."
    )
    fun getPerformanceEventFromDate(
        @PathVariable performanceId: String,
        @PathVariable performanceDate: String,
    ): ResponseEntity<GetPerformanceEventResponse> {
        val localPerformanceDate = LocalDate.parse(performanceDate)
        val performanceEventList = performanceEventService.getPerformanceEventFromDate(
            performanceId = performanceId,
            performanceDate = localPerformanceDate,
        )
        return ResponseEntity.ok(performanceEventList)
    }

    // WARN: THIS IS FOR ADMIN.
    // TODO: SEPERATE THIS TO OTHER APPLICATION
    @PostMapping("/admin/v1/performance-event")
    @Operation(
        summary = "공연이벤트 생성",
    )
    fun createPerformanceEvent(
        @RequestBody request: CreatePerformanceEventRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): ResponseEntity<CreatePerformanceEventResponse> {
        val newPerformanceEvent: PerformanceEvent =
            performanceEventService
                .createPerformanceEvent(
                    request.performanceId,
                    request.performanceHallId,
                    request.startAt,
                    request.endAt
                )
        return ResponseEntity.status(HttpStatus.CREATED).body(newPerformanceEvent)
    }

    @DeleteMapping("/admin/v1/performance-event/{performanceEventId}")
    @Operation(
        summary = "공연이벤트 삭제",
    )
    fun deletePerformanceEvent(
        @PathVariable performanceEventId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): ResponseEntity<String> {
        performanceEventService.deletePerformanceEvent(performanceEventId)
        return ResponseEntity.noContent().build()
    }

}

typealias GetPerformanceEventResponse = List<PerformanceEvent>

data class CreatePerformanceEventRequest(
    val performanceId: String,
    val performanceHallId: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
)

typealias CreatePerformanceEventResponse = PerformanceEvent