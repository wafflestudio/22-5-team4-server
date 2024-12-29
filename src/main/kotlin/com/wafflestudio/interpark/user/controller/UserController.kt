package org.example.com.wafflestudio.interpark.user.controller

import com.wafflestudio.interpark.user.*
import org.example.com.wafflestudio.interpark.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService,
) {

}