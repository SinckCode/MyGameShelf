package com.example.mygameshelf.data.services

import com.example.mygameshelf.domain.dtos.game.GameDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path


interface GameService {

    @GET("api/games")
    suspend fun getGames(): List<GameDto>

    @GET("api/games/{id}")
    suspend fun getGameById(
        @Path("id") id: Int
    ): GameDto
}
