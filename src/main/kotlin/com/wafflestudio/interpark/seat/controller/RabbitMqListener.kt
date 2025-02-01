import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class RabbitMqListener {
    @RabbitListener(queues = ["reservation-queue"])
    fun listen(message: String) {
        
    }
}



// fun reserveSeat(
//     @RequestBody request: ReserveSeatRequest,
//     @AuthenticationPrincipal userDetails: UserDetailsImpl
// ): ResponseEntity<ReserveSeatResponse> {
//     val reservationId = seatService.reserveSeat(userDetails.getUserId(), request.performanceEventId, request.seatId)
//     return ResponseEntity.status(201).body(ReserveSeatResponse(reservationId))
// }