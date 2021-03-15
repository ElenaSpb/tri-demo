package com.example.demo.repo

import com.example.demo.model.UserAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserAccountRepository : JpaRepository<UserAccount, Long> {
//    fun findByName(fullName: String): Optional<UserAccount>
//    fun findByEmail(email: String): Optional<UserAccount>
}
