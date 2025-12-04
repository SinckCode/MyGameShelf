package com.example.mygameshelf.data.services

import com.example.mygameshelf.domain.dtos.playlist.CreatePlaylistRequest
import com.example.mygameshelf.domain.dtos.playlist.PlaylistDetailDto
import com.example.mygameshelf.domain.dtos.playlist.PlaylistDto
import com.example.mygameshelf.domain.dtos.playlist.UpdatePlaylistGamesRequest
import de.jensklingenberg.ktorfit.http.*

interface PlaylistService {

    // GET /api/playlists/my?userId=...
    @GET("api/playlists/my")
    suspend fun getMyPlaylists(
        @Query("userId") userId: String
    ): List<PlaylistDto>

    // POST /api/playlists
    @POST("api/playlists")
    suspend fun createPlaylist(
        @Body body: CreatePlaylistRequest
    ): PlaylistDto

    // GET /api/playlists/{id}?userId=...
    @GET("api/playlists/{id}")
    suspend fun getPlaylistDetail(
        @Path("id") playlistId: String,
        @Query("userId") userId: String
    ): PlaylistDetailDto

    // PUT /api/playlists/{id}/games
    @PUT("api/playlists/{id}/games")
    suspend fun setPlaylistGames(
        @Path("id") playlistId: String,
        @Body body: UpdatePlaylistGamesRequest
    )

    // DELETE /api/playlists/{id}?userId=...
    @DELETE("api/playlists/{id}")
    suspend fun deletePlaylist(
        @Path("id") playlistId: String,
        @Query("userId") userId: String
    )
}
