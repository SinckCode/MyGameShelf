package com.example.mygameshelf.domain.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse(
    val message : String,
    @SerialName("isLogged") val islogged : Boolean,
    //val isLogged : Boolean,
    val userId: String? = null,
    val name: String? = null
)
