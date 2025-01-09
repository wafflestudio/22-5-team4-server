package com.wafflestudio.interpark.seat.controller

import com.wafflestudio.interpark.seat.service.SeatService
import com.wafflestudio.interpark.user.AuthUser
import com.wafflestudio.interpark.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SeatController(
    private val seatService: SeatService,
) {
    @GetMapping("/api/v1/seat/{performanceEventId}/available")
    fun getAvailableSeats(
        @PathVariable performanceEventId: String,
    ): ResponseEntity<GetAvailableSeatsResponse> {
        val seats = seatService.getAvailableSeats(performanceEventId)
        return ResponseEntity.ok(GetAvailableSeatsResponse(seats.map{ AvailableSeat(it.first, it.second) }))
    }

    @PostMapping("/api/v1/reservation/reserve")
    fun reserveSeat(
        @RequestBody request: ReserveSeatRequest,
        @AuthUser user: User,
    ): ResponseEntity<ReserveSeatResponse> {
        val reservation = seatService.reserveSeat(user, request.reservationId)
        return ResponseEntity.status(201).body(ReserveSeatResponse(reservation))
    }

    @PostMapping("/api/v1/reservation/cancel")
    fun cancelReserveSeat(
        @RequestBody request: CancelReserveSeatRequest,
        @AuthUser user: User,
    ): ResponseEntity<Void> {
        seatService.cancelReserveSeat(user, request.reservationId)
        return ResponseEntity.noContent().build()
    }
}

data class AvailableSeat(
    val reservationId: String,
    val seat: Seat,
)
data class GetAvailableSeatsResponse(
    val availableSeats: List<AvailableSeat>
)
data class ReserveSeatRequest(
    val reservationId: String,
)
data class ReserveSeatResponse(
    val reservation: Reservation,
)
data class CancelReserveSeatRequest(
    val reservationId: String,
)