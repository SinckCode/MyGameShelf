package com.example.mygameshelf.domain.builders

import com.example.mygameshelf.domain.dtos.playlist.CreatePlaylistRequest
import com.example.mygameshelf.domain.dtos.playlist.UpdatePlaylistGamesRequest

/**
 * Builder para construir requests relacionados con playlists.
 *
 * Aplica el patrón Builder porque:
 *  - Permite configurar paso a paso los campos (fluently con apply)
 *  - Evita tener constructores con demasiados parámetros
 *  - Facilita reutilizar la misma base (userId) para distintos requests
 */
class PlaylistRequestBuilder {

    private var name: String = ""
    private var userId: String = ""
    private val gameIds: MutableList<Int> = mutableListOf()

    fun name(value: String) = apply { name = value }

    fun userId(value: String) = apply { userId = value }

    fun addGame(id: Int) = apply { gameIds.add(id) }

    fun addGames(ids: Collection<Int>) = apply { gameIds.addAll(ids) }

    /**
     * Construye el request para crear una playlist.
     */
    fun buildCreateRequest(): CreatePlaylistRequest {
        require(name.isNotBlank()) { "El nombre de la playlist no puede estar vacío" }
        require(userId.isNotBlank()) { "El userId no puede estar vacío" }

        return CreatePlaylistRequest(
            name = name,
            userId = userId
        )
    }

    /**
     * Construye el request para actualizar los juegos de una playlist.
     */
    fun buildUpdateGamesRequest(): UpdatePlaylistGamesRequest {
        require(userId.isNotBlank()) { "El userId no puede estar vacío" }
        return UpdatePlaylistGamesRequest(
            userId = userId,
            gameIds = gameIds.toList()
        )
    }
}
