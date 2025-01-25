package com.wafflestudio.interpark.seat.controller

import com.wafflestudio.interpark.seat.service.SeatService
import com.wafflestudio.interpark.user.AuthUser
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
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
    fun getAvailableSeats(
        @PathVariable performanceEventId: String,
    ): ResponseEntity<GetAvailableSeatsResponse> {
        val seats = seatService.getAvailableSeats(performanceEventId)
        return ResponseEntity.ok(GetAvailableSeatsResponse(seats))
    }

    @PostMapping("/api/v1/reservation/reserve")
    fun reserveSeat(
        @RequestBody request: ReserveSeatRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<ReserveSeatResponse> {
        val reservationId = seatService.reserveSeat(userDetails.getUserId(), request.performanceEventId, request.seatId)
        return ResponseEntity.status(201).body(ReserveSeatResponse(reservationId))
    }

    @GetMapping("/api/v1/me/reservation")
    fun getMyReservations(
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<GetMyReservationsResponse> {
        val myReservations = seatService.getMyReservations(userDetails.getUserId())
        return ResponseEntity.ok(GetMyReservationsResponse(myReservations))
    }

    @GetMapping("/api/v1/reservation/detail/{reservationId}")
    fun getReservedSeatDetail(
        @PathVariable reservationId: String,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<GetReservedSeatDetailResponse> {
        val reservationDetail = seatService.getReservedSeatDetail(userDetails.getUserId(), reservationId)
        return ResponseEntity.status(200).body(GetReservedSeatDetailResponse(reservationDetail))
    }

    @DeleteMapping("/api/v1/reservation/{reservationId}")
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
