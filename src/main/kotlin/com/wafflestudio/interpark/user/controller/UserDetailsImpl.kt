package com.wafflestudio.interpark.user.controller

import com.wafflestudio.interpark.user.persistence.UserIdentityEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl (
    private val userIdentityEntity: UserIdentityEntity
) : UserDetails {
    override fun getUsername(): String {
        return userIdentityEntity.user.username
    }

    override fun getPassword(): String {
        return userIdentityEntity.hashedPassword
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(userIdentityEntity.role)
    }

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

    // 필요하면 convenience 메서드
    fun getUserId(): String = userIdentityEntity.user.id!!
    fun getNickname(): String = userIdentityEntity.user.nickname
    fun getEmail(): String = userIdentityEntity.user.email
    fun getAddress(): String? = userIdentityEntity.user.address
    fun getPhoneNumber(): String = userIdentityEntity.user.phoneNumber
}
