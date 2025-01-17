package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.service.PerformanceEventService
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.AuthUser
import com.wafflestudio.interpark.user.UserIdentityNotFoundException
import com.wafflestudio.interpark.user.persistence.UserRole
import com.wafflestudio.interpark.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PerformanceEventController(
    private val performanceEventService: PerformanceEventService,
    private val userService: UserService
) {
    @GetMapping("/api/v1/performance-event")
    fun getPerformanceEvent(
        @AuthUser user: User,
    ): ResponseEntity<GetPerformanceEventResponse> {
        // Currently, no search
        val performanceEventList: List<PerformanceEvent> = performanceEventService
            .getAllPerformanceEvent()
        return ResponseEntity.ok(performanceEventList)
    }
    
    // WARN: THIS IS FOR ADMIN.
    // TODO: SEPERATE THIS TO OTHER APPLICATION
    @PostMapping("/admin/v1/performance-event")
    fun createPerformanceEvent(
        @RequestBody request: CreatePerformanceEventRequest,
        @AuthUser user: User,
    ): ResponseEntity<CreatePerformanceEventResponse> {
        // UserIdentity를 통해 역할(Role) 확인
        val userIdentity = userService.getUserIdentityByUserId(user.id) // user.id를 통해 UserIdentity 조회
            ?: throw UserIdentityNotFoundException()

        if (userIdentity.role != UserRole.ADMIN) { // 역할(Role)이 ADMIN이 아니면 FORBIDDEN 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }

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
    fun deletePerformanceEvent(
        @PathVariable performanceEventId: String,
        @AuthUser user: User
    ): ResponseEntity<String> {
        // UserIdentity를 통해 역할(Role) 확인
        val userIdentity = userService.getUserIdentityByUserId(user.id) // user.id를 통해 UserIdentity 조회
            ?: throw UserIdentityNotFoundException()

        if (userIdentity.role != UserRole.ADMIN) { // 역할(Role)이 ADMIN이 아니면 FORBIDDEN 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }

        performanceEventService.deletePerformanceEvent(performanceEventId)
        return ResponseEntity.noContent().build()
    }

}

typealias GetPerformanceEventResponse = List<PerformanceEvent>

data class CreatePerformanceEventRequest(
    val performanceId: String,
    val performanceHallId: String,
    val startAt: String,
    val endAt: String,
)

typealias CreatePerformanceEventResponse = PerformanceEvent