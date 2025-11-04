package com.example.mygameshelf.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygameshelf.data.services.KtorfitClient
import com.example.mygameshelf.domain.dtos.game.GameDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class GamesUiState(
    val isLoading: Boolean = false,
    val games: List<GameDto> = emptyList(),
    val error: String? = null
)

class GamesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GamesUiState())
    val uiState: StateFlow<GamesUiState> = _uiState

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val service = KtorfitClient.createGameService()
                val result = service.getGames()

                _uiState.value = GamesUiState(
                    isLoading = false,
                    games = result,
                    error = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = GamesUiState(
                    isLoading = false,
                    games = emptyList(),
                    error = "Error al cargar juegos"
                )
            }
        }
    }
}
