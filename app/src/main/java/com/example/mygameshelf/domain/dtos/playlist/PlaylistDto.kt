package com.example.mygameshelf.domain.dtos.playlist

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDto(
    val id: String,      // UUID
    val name: String,
    val gamesCount: Int
)
