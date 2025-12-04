package com.example.mygameshelf.domain.dtos.playlist

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlaylistGamesRequest(
    val userId: String,
    val gameIds: List<Int>
)
