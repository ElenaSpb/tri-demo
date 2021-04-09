package com.example.demo.service

import com.example.demo.model.UserAccount
import com.example.demo.repo.UserAccountRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
@Profile("dev", "prod")
class InitDataLoader(private val userAccountRepository: UserAccountRepository) {

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationEvent(event: ApplicationReadyEvent) {
        val listOfUsers = mutableListOf(addAdminUser())
        listOfUsers.addAll(addDefaultUsers())
        userAccountRepository.saveAll(listOfUsers)
    }

    private fun addAdminUser() = UserAccount(
        fullName = "admin",
        password = "admin",
        email = "admin@admin.com",
        active = true,
        bornYear = 1999,
    )

    private fun addDefaultUsers(): MutableList<UserAccount> {
        val firstUser = UserAccount(
            fullName = "Name1",
            email = "default@default.com",
            active = true,
            bornYear = 1999,
            password = "password"
        )
        val secondUser = UserAccount(
            fullName = "Name2",
            email = "default2@default.com",
            active = true,
            bornYear = 1999,
            password = "password"
        )
        return mutableListOf(firstUser, secondUser)
    }
}
