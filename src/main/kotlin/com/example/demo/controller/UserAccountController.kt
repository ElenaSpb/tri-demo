package com.example.demo.controller

import com.example.demo.dto.UserAccountDto
import com.example.demo.service.UserAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserAccountController(@Autowired val userAccountService: UserAccountService) {

    @GetMapping
    fun findAllUsers(): ResponseEntity<List<UserAccountDto>> = ResponseEntity.ok()
        .body(userAccountService.findAllUsers())

    @GetMapping(value = ["/{userId}"])
    fun findUserById(@PathVariable("userId") id: Long): ResponseEntity<UserAccountDto> = ResponseEntity.ok()
            .body(userAccountService.findUserById(id))

}
