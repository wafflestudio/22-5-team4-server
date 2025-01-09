package com.wafflestudio.interpark.performance.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PerformanceRepository :
    JpaRepository<PerformanceEntity, String>,
    JpaSpecificationExecutor<PerformanceEntity> {
      
    }
