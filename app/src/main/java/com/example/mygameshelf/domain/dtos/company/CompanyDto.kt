package com.example.mygameshelf.domain.dtos.company

import kotlinx.serialization.Serializable

@Serializable
data class CompanyDto(
    val id: Int,
    val nombre: String,
    val fundacion: Int,
    val historia: String,
    val imagenURL: String
)