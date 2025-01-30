package com.wafflestudio.interpark

import org.redisson.api.RLock
import java.util.concurrent.TimeUnit

inline fun <T> RLock.withLock(leaseTime: Long, unit: TimeUnit, action: () -> T): T {
    try {
        val isLocked = this.tryLock(10, leaseTime, unit)
        if (!isLocked) {
            throw ConcurrentReservationException("Failed to acquire lock")
        }
        return action()
    } finally {
        this.unlock()
    }
}

class ConcurrentReservationException(message: String) : RuntimeException(message)