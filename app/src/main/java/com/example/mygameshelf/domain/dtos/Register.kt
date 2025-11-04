package com.example.mygameshelf.domain.dtos

import kotlinx.serialization.Serializable

@Serializable
data class Register(
    val name : String,
    val email : String,
    val password : String
)
