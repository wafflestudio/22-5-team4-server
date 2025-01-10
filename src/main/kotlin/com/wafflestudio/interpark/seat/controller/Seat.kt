package com.wafflestudio.interpark.seat.controller

import com.wafflestudio.interpark.seat.persistence.SeatEntity

data class Seat(
    val id: String,
    val seatNumber: Pair<Int, Int>,
    val price: Int
) {
    companion object {
        fun fromEntity(entity: SeatEntity): Seat {
            return Seat(
                id = entity.id!!,
                seatNumber = entity.seatNumber,
                price = entity.price,
            )
        }
    }
}