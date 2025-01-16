package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.performance.service.PerformanceHallService
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.AuthUser
import com.wafflestudio.interpark.user.persistence.UserRole
import com.wafflestudio.interpark.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PerformanceHallController(
    private val performanceHallService: PerformanceHallService,
    private val userService: UserService
) {
    @GetMapping("/api/v1/performance-hall")
    fun getPerformanceHall(
        @AuthUser user: User,
    ): ResponseEntity<GetPerformanceHallResponse> {
        // Currently, no search
        val performanceHallList: List<PerformanceHall> = performanceHallService
            .getAllPerformanceHall()
        return ResponseEntity.ok(performanceHallList)
    }
    
    // WARN: THIS IS FOR ADMIN.
    // TODO: SEPERATE THIS TO OTHER APPLICATION
    @PostMapping("/admin/v1/performance-hall")
    fun createPerformanceHall(
        @RequestBody request: CreatePerformanceHallRequest,
        @AuthUser user: User
    ): ResponseEntity<CreatePerformanceHallResponse> {
        // UserIdentity를 통해 역할(Role) 확인
        val userIdentity = userService.getUserIdentityByUserId(user.id) // user.id를 통해 UserIdentity 조회
            ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null) // UserIdentity가 없으면 FORBIDDEN 반환

        if (userIdentity.role != UserRole.ADMIN) { // 역할(Role)이 ADMIN이 아니면 FORBIDDEN 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }

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
    fun deletePerformance(
        @PathVariable performanceHallId: String,
        @AuthUser user: User
    ): ResponseEntity<String> {
        // UserIdentity를 통해 역할(Role) 확인
        val userIdentity = userService.getUserIdentityByUserId(user.id) // user.id를 통해 UserIdentity 조회
            ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null) // UserIdentity가 없으면 FORBIDDEN 반환

        if (userIdentity.role != UserRole.ADMIN) { // 역할(Role)이 ADMIN이 아니면 FORBIDDEN 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }

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