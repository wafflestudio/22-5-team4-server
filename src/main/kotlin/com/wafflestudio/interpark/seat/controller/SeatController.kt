package com.wafflestudio.interpark.seat.controller

import com.wafflestudio.interpark.seat.service.SeatService
import com.wafflestudio.interpark.user.AuthUser
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class SeatController(
    private val seatService: SeatService,
) {
    @GetMapping("/api/v1/seat/{performanceEventId}/available")
    @Operation(
        summary = "예매가능한 좌석 조회",
        description = "구체적인 공연이 주어졌을 때 그 공연에서 예매할 수 있는 좌석들을 알려줍니다."
    )
    fun getAvailableSeats(
        @PathVariable performanceEventId: String,
    ): ResponseEntity<GetAvailableSeatsResponse> {
        val seats = seatService.getAvailableSeats(performanceEventId)
        return ResponseEntity.ok(GetAvailableSeatsResponse(seats))
    }

    @PostMapping("/api/v1/reservation/reserve")
    @Operation(
        summary = "예매",
        description = "좌석을 선택해서 예매할 수 있습니다. 예매에 실패하면 409가 반환됩니다."
    )
    fun reserveSeat(
        @RequestBody request: ReserveSeatRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<ReserveSeatResponse> {
        val reservationId = seatService.reserveSeat(userDetails.getUserId(), request.performanceEventId, request.seatId)
        return ResponseEntity.status(201).body(ReserveSeatResponse(reservationId))
    }

    @GetMapping("/api/v1/me/reservation")
    @Operation(
        summary = "본인의 예매내역 조회",
        description = "본인이 예매한 내역을 예매정보를 간략화 한 배열로 반환합니다.\n더 구체적인 정보가 필요하다면 /api/v1/reservation/detail/{reservationId}를 사용해야 합니다."
    )
    fun getMyReservations(
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<GetMyReservationsResponse> {
        val myReservations = seatService.getMyReservations(userDetails.getUserId())
        return ResponseEntity.ok(GetMyReservationsResponse(myReservations))
    }

    @GetMapping("/api/v1/reservation/detail/{reservationId}")
    @Operation(
        summary = "본인의 예매 자세히 보기",
        description = "본인의 예매 중 하나를 선택해 자세한 정보를 받을 수 있습니다."
    )
    fun getReservedSeatDetail(
        @PathVariable reservationId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<GetReservedSeatDetailResponse> {
        val reservationDetail = seatService.getReservedSeatDetail(userDetails.getUserId(), reservationId)
        return ResponseEntity.status(200).body(GetReservedSeatDetailResponse(reservationDetail))
    }

    @DeleteMapping("/api/v1/reservation/{reservationId}")
    @Operation(
        summary = "예매 취소",
        description = "본인의 예매 중 하나를 선택해 취소할 수 있습니다."
    )
    fun cancelReservedSeat(
        @PathVariable reservationId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<Void> {
        seatService.cancelReservedSeat(userDetails.getUserId(), reservationId)
        return ResponseEntity.noContent().build()
    }
}

data class BriefReservation(
    val id: String,
    val performanceTitle: String,
    val posterUri: String,
    val performanceDate: LocalDate,
    val reservationDate: LocalDate,
)

data class GetAvailableSeatsResponse(
    val availableSeats: List<Seat>,
)

data class ReserveSeatRequest(
    val performanceEventId: String,
    val seatId: String,
)

data class ReserveSeatResponse(
    val reservationId: String,
)

data class GetMyReservationsResponse(
    val myReservations: List<BriefReservation>,
)

data class GetReservedSeatDetailResponse(
    val reservedSeat: Reservation
)
