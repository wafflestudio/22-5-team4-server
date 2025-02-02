package com.wafflestudio.interpark.user.persistence

import com.wafflestudio.interpark.review.persistence.ReplyEntity
import com.wafflestudio.interpark.review.persistence.ReviewEntity
import com.wafflestudio.interpark.seat.persistence.ReservationEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne

@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "username", nullable = false)
    val username: String,
    @Column(name = "nickname", nullable = false)
    val nickname: String,
    @Column(name = "phone_number", nullable = false)
    val phoneNumber: String,
    @Column(name = "email", nullable = false)
    val email: String,
    @Column(name = "address", nullable = true)
    val address: String? = null,

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reviews: MutableSet<ReviewEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], orphanRemoval = true)
    var replies: MutableSet<ReplyEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reservations: MutableSet<ReservationEntity> = mutableSetOf(),

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var userIdentity: UserIdentityEntity? = null,
)
