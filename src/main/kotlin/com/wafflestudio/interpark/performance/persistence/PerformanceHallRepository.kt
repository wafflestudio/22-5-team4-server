package com.wafflestudio.interpark.performance.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface PerformanceHallRepository : JpaRepository<PerformanceHallEntity, String> {
    fun findByName(name: String): PerformanceHallEntity?

    fun existsByName(name: String): Boolean
}
