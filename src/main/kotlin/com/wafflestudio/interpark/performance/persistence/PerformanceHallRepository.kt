package com.wafflestudio.interpark.performance.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface PerformanceHallRepository : JpaRepository<PerformanceHallEntity, String> {
    
}
