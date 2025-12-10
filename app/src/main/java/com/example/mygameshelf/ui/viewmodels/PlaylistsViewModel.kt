package com.example.mygameshelf.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygameshelf.data.services.KtorfitClient
import com.example.mygameshelf.data.services.PlaylistService
import com.example.mygameshelf.data.services.Preferences
import com.example.mygameshelf.domain.builders.PlaylistRequestBuilder
import com.example.mygameshelf.domain.dtos.playlist.PlaylistDetailDto
import com.example.mygameshelf.domain.dtos.playlist.PlaylistDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ---------- UI STATE ----------

data class PlaylistsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val playlists: List<PlaylistDto> = emptyList()
)

data class PlaylistDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val detail: PlaylistDetailDto? = null
)

// ---------- VIEWMODEL ----------

class PlaylistsViewModel(
    private val playlistService: PlaylistService = KtorfitClient.createPlaylistService(),
    // Lee el userId guardado después del login
    private val userIdProvider: () -> String = {
        Preferences.getUserId() ?: ""
    }
) : ViewModel() {

    private val _playlistsState = MutableStateFlow(PlaylistsUiState())
    val playlistsState: StateFlow<PlaylistsUiState> = _playlistsState.asStateFlow()

    private val _detailState = MutableStateFlow(PlaylistDetailUiState())
    val detailState: StateFlow<PlaylistDetailUiState> = _detailState.asStateFlow()

    private val userId: String
        get() = userIdProvider()

    // ------- LISTAR PLAYLISTS -------

    fun loadPlaylists() {
        viewModelScope.launch {
            _playlistsState.value = _playlistsState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val data = playlistService.getMyPlaylists(userId)
                _playlistsState.value = _playlistsState.value.copy(
                    isLoading = false,
                    error = null,
                    playlists = data
                )
            } catch (e: Exception) {
                _playlistsState.value = _playlistsState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar playlists"
                )
            }
        }
    }

    // ------- CREAR PLAYLIST (usando Builder) -------

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            _playlistsState.value = _playlistsState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val request = PlaylistRequestBuilder()
                    .name(name)
                    .userId(userId)
                    .buildCreateRequest()

                // 1) crear playlist
                playlistService.createPlaylist(request)

                // 2) recargar listas desde el servidor
                val data = playlistService.getMyPlaylists(userId)

                // 3) actualizar estado final
                _playlistsState.value = _playlistsState.value.copy(
                    isLoading = false,
                    error = null,
                    playlists = data
                )
            } catch (e: Exception) {
                _playlistsState.value = _playlistsState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al crear playlist"
                )
            }
        }
    }

    // ------- DETALLE DE PLAYLIST -------

    fun loadPlaylistDetail(playlistId: String) {
        viewModelScope.launch {
            _detailState.value = _detailState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val detail = playlistService.getPlaylistDetail(playlistId, userId)
                _detailState.value = _detailState.value.copy(
                    isLoading = false,
                    error = null,
                    detail = detail
                )
            } catch (e: Exception) {
                _detailState.value = _detailState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar detalle"
                )
            }
        }
    }

    // ------- ASIGNAR JUEGOS A PLAYLIST (usando Builder) -------

    fun setPlaylistGames(
        playlistId: String,
        gameIds: List<Int>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = PlaylistRequestBuilder()
                    .userId(userId)
                    .addGames(gameIds)
                    .buildUpdateGamesRequest()

                playlistService.setPlaylistGames(
                    playlistId = playlistId,
                    body = request
                )

                loadPlaylistDetail(playlistId)
                onSuccess()
            } catch (_: Exception) {
                // si quieres, podrías propagar un error en _detailState
            }
        }
    }

    // ------- ELIMINAR PLAYLIST -------

    fun deletePlaylist(
        playlistId: String,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            // opcional: mostrar loading mientras elimina
            _playlistsState.value = _playlistsState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                // 1) eliminar en backend
                playlistService.deletePlaylist(playlistId, userId)

                // 2) recargar listas
                val data = playlistService.getMyPlaylists(userId)

                // 3) actualizar estado
                _playlistsState.value = _playlistsState.value.copy(
                    isLoading = false,
                    error = null,
                    playlists = data
                )

                // 4) callback si te interesa hacer algo extra (snackbar, etc.)
                onSuccess?.invoke()
            } catch (e: Exception) {
                _playlistsState.value = _playlistsState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al eliminar playlist"
                )
            }
        }
    }
}
