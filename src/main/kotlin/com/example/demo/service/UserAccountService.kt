package com.example.demo.service

import com.example.demo.dto.UserAccountDto
import com.example.demo.repo.UserAccountRepository
import org.apache.logging.log4j.kotlin.logger
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class UserAccountService(private val userAccountRepository: UserAccountRepository) {

    private val log = logger()

    fun findUserById(id: Long): UserAccountDto =
        userAccountRepository.findById(id)
            .map { it.toDto() }
            .orElseThrow { EntityNotFoundException("No user was found for id = $id") }

    fun findAllUsers(): List<UserAccountDto> =
        userAccountRepository.findAll().map { it.toDto() }

}
