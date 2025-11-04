package com.example.mygameshelf.domain.dtos.game

import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val rating: Double,
    val plataformas: List<String>,
    val genero: String,
    val precio: Double,
    val imagenURL: String
)