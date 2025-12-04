package com.example.mygameshelf.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygameshelf.data.services.KtorfitClient
import com.example.mygameshelf.data.services.PlaylistService
import com.example.mygameshelf.data.services.Preferences
import com.example.mygameshelf.domain.dtos.playlist.CreatePlaylistRequest
import com.example.mygameshelf.domain.dtos.playlist.PlaylistDetailDto
import com.example.mygameshelf.domain.dtos.playlist.PlaylistDto
import com.example.mygameshelf.domain.dtos.playlist.UpdatePlaylistGamesRequest
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
            // empezamos cargando
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

    // ------- CREAR PLAYLIST -------

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            // marcamos loading y limpiamos error
            _playlistsState.value = _playlistsState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                // 1) crear playlist
                playlistService.createPlaylist(
                    CreatePlaylistRequest(
                        name = name,
                        userId = userId
                    )
                )

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

    // ------- ASIGNAR JUEGOS A PLAYLIST -------

    fun setPlaylistGames(
        playlistId: String,
        gameIds: List<Int>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                playlistService.setPlaylistGames(
                    playlistId,
                    UpdatePlaylistGamesRequest(
                        userId = userId,
                        gameIds = gameIds
                    )
                )
                loadPlaylistDetail(playlistId)
                onSuccess()
            } catch (_: Exception) {
                // si quieres, podrías propagar un error en _detailState
            }
        }
    }

    // ------- ELIMINAR PLAYLIST -------

    fun deletePlaylist(playlistId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                playlistService.deletePlaylist(playlistId, userId)
                loadPlaylists()
                onSuccess()
            } catch (_: Exception) {
                // igual, podrías setear error en _playlistsState si lo necesitas
            }
        }
    }
}
