package com.wafflestudio.interpark.performance.controller

import com.wafflestudio.interpark.pagination.CursorPageable
import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
import io.swagger.v3.oas.annotations.Operation
import com.wafflestudio.interpark.performance.service.PerformanceService
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
        @RequestParam cursor: String?,
    ): ResponseEntity<SearchPerformanceResponse> {
        // @RequestParam(defaultValue) 대신 내부적으로 기본값 처리
        val sortField: String? = null
        val isDescending = false
        val size = 5

        val cursorPageable= CursorPageable(cursor, sortField, isDescending, size)

        val queriedPerformances = performanceService.searchPerformance(title, category, cursorPageable)
        return ResponseEntity.ok(queriedPerformances)
    }

    // WARN: THIS IS FOR ADMIN.
    // TODO: SEPERATE THIS TO OTHER APPLICATION
    @PostMapping("/admin/v1/performance")
    fun createPerformance(
        @Valid @RequestBody request: CreatePerformanceRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
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
        return ResponseEntity.status(HttpStatus.CREATED).body(newPerformance)
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
        @PathVariable performanceId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): ResponseEntity<String> {
        performanceService.deletePerformance(performanceId)
        return ResponseEntity.noContent().build()
    }

}

typealias SearchPerformanceResponse = List<BriefPerformanceDetail>

data class BriefPerformanceDetail(
    val id: String,
    val title: String,
    val hallName: String,
    val performanceDuration: PerformanceDuration,
    val posterUri: String,
    // 추후 제공 예정
    // val ratingAvg: Double,
    // val reviewCount: Int,
)

data class CreatePerformanceRequest(
    @field:NotBlank(message = "Title must not be blank")
    val title: String,
    @field:NotBlank(message = "Detail must not be blank")
    val detail: String,
    @field:NotNull(message = "Category must not be null")
    val category: PerformanceCategory,
    @field:NotBlank(message = "Poster URI must not be blank")
    val posterUri: String,
    @field:NotBlank(message = "Backdrop Image URI must not be blank")
    val backdropImageUri: String
)

typealias CreatePerformanceResponse = Performance

typealias GetPerformanceDetailResponse = Performance
