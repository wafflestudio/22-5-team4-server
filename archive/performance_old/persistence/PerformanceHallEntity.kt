package com.wafflestudio.interpark.performance.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "performance_hall")
class PerformanceHallEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var address: String,
)
