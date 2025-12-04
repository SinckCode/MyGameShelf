package com.example.mygameshelf.domain.dtos.playlist

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDetailDto(
    val id: String,
    val name: String,
    val gameIds: List<Int>
)
