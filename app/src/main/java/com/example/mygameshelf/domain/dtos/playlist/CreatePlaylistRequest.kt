package com.example.mygameshelf.domain.dtos.playlist

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistRequest(
    val name: String,
    val userId: String
)
