package com.example.demo.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.ZoneId
import java.time.ZonedDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserAccountDto(
    val id: Long? = null,
    val userId: String? = null,
    val name: String?,
    val email: String?,
    val bornYear: Int?,
    var active: Boolean = true,
    val created: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")),
    val changed: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")),
    var avatar: String? = null
)