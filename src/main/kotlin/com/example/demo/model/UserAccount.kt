package com.example.demo.model

import com.example.demo.dto.UserAccountDto
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class UserAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val userId: String? = null,
    @Column(nullable = false, unique = true)
    val email: String?,
    var fullName: String?,
    val password: String?,
    var bornYear: Int?,
    var active: Boolean = true,
    @Column(nullable = false)
    var created: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")),
    @Column(nullable = false)
    var changed: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")),
    var avatar: String? = null
) {
    override fun toString(): String {
        return "UserAccount(id=$id, userId=$userId, fullName='$fullName', email='$email', bornYear=$bornYear)"
    }

    fun toDto(): UserAccountDto = UserAccountDto(
        id = id,
        userId = userId,
        name = fullName,
        active = active,
        email = email,
        bornYear = bornYear,
        created = created,
        changed = changed,
        avatar = avatar
    )
}
