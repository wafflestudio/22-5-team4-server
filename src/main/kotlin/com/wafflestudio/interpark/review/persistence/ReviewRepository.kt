package com.wafflestudio.interpark.review.persistence

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface ReviewRepository : JpaRepository<ReviewEntity, String>,
    JpaSpecificationExecutor<ReviewEntity> {
    @Query("SELECT r FROM ReviewEntity r WHERE r.performance.id = :performanceId ORDER BY r.createdAt DESC")
    fun findByPerformanceId(performanceId: String): List<ReviewEntity>
    @Query("SELECT r FROM ReviewEntity r WHERE r.author.id = :authorId ORDER BY r.createdAt DESC")
    fun findByAuthorId(authorId: String): List<ReviewEntity>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReviewEntity r WHERE r.id = :id")
    fun findByIdWithWriteLock(id: String): ReviewEntity?
}
