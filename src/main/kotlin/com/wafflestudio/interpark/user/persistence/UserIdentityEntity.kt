package com.wafflestudio.interpark.user.persistence

import com.wafflestudio.interpark.review.persistence.ReviewEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.OneToMany
import org.springframework.security.core.GrantedAuthority

@Entity
class UserIdentityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @OneToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity,
    @Column(name = "role", nullable = false)
    var role: UserRole = UserRole.USER,
    @Column(name = "hashed_password", nullable = false)
    val hashedPassword: String,

//    @OneToMany(mappedBy = "userIdentity", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
//    val socialAccounts: MutableList<SocialAccountEntity> = mutableListOf(),
    @OneToMany(mappedBy = "userIdentity", cascade = [CascadeType.ALL], orphanRemoval = true)
    var socialAccounts: MutableSet<SocialAccountEntity> = mutableSetOf(),
)

enum class UserRole : GrantedAuthority {
    USER, ADMIN;

    override fun getAuthority(): String {
        return "ROLE_$name" // Spring Security에서 권장하는 ROLE_ 접두사
    }
}
