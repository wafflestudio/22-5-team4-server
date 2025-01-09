package com.wafflestudio.interpark.seat.controller

import com.wafflestudio.interpark.seat.persistence.ReservationEntity

data class Reservation(
    val id: String,
    val user: String,
    val seatNumber: Int,
) {
    companion object {
        fun fromEntity(entity: ReservationEntity): Reservation {

        }
    }
}