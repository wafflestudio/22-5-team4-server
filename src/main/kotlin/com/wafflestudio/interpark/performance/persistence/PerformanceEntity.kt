package com.wafflestudio.interpark.performance.persistence

import jakarta.persistence.*

@Entity(name = "performances")
class PerformanceEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,

    val detail: String? = null
)