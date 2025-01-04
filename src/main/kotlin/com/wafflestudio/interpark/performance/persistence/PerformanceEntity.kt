package com.wafflestudio.interpark.performance.persistence

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDate

@Entity(name = "performances")
class PerformanceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    var hall: PerformanceHallEntity,
    @Column(nullable = false)
    var title: String,
    @Column(columnDefinition = "TEXT")
    var detail: String? = null,
    @Column(nullable = false)
    var genre: String,
    @Column(nullable = false)
    var sales: Int = 0,
    @ElementCollection
    @CollectionTable(name = "performance_dates", joinColumns = [JoinColumn(name = "performance_id")])
    @Column(name = "date", nullable = false)
    var dates: List<LocalDate> = mutableListOf(),
    @Column(name = "poster_url", nullable = false)
    var posterUrl: String,
    @Column(name = "backdrop_url", nullable = false)
    var backdropUrl: String,
    @ElementCollection
    @CollectionTable(name = "performance_seats", joinColumns = [JoinColumn(name = "performance_id")])
    @Column(name = "seat_id", nullable = false)
    var seatIds: List<String> = mutableListOf(),
    @ElementCollection
    @CollectionTable(name = "performance_reviews", joinColumns = [JoinColumn(name = "performance_id")])
    @Column(name = "review_id", nullable = false)
    var reviewIds: List<String> = mutableListOf(),
)
