package com.wafflestudio.interpark.review.persistence

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface ReviewRepository : JpaRepository<ReviewEntity, String> {
    fun findByPerformanceId(performanceId: String): List<ReviewEntity>
    fun findByAuthorId(authorId: String): List<ReviewEntity>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReviewEntity r WHERE r.id = :id")
    fun findByIdWithWriteLock(id: String): ReviewEntity?
}
