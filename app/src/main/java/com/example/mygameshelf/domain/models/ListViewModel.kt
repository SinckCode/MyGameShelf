// ui/viewmodels/ListViewModel.kt
package com.example.mygameshelf.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListViewModel : ViewModel() {

    // Solo nombres de listas (lo que usamos en ListView)
    private val _userLists = MutableStateFlow<List<String>>(emptyList())
    val userLists: StateFlow<List<String>> = _userLists.asStateFlow()

    // Mapa lista -> ids de juegos
    private val _listGames = MutableStateFlow<Map<String, List<Int>>>(emptyMap())
    val listGames: StateFlow<Map<String, List<Int>>> = _listGames.asStateFlow()

    fun loadUserLists(userId: String) {
        // TODO: conectar a backend después
        _userLists.value = _userLists.value // por ahora nada
    }

    fun createList(listName: String, userId: String) {
        if (listName.isBlank()) return
        if (_userLists.value.contains(listName)) return

        _userLists.value = _userLists.value + listName
        _listGames.value = _listGames.value + (listName to emptyList())
    }

    fun addGamesToList(listName: String, gameIds: List<Int>) {
        val current = _listGames.value[listName] ?: emptyList()
        val merged = (current + gameIds).distinct()
        _listGames.value = _listGames.value + (listName to merged)
    }

    fun getGamesForList(listName: String): List<Int> {
        return _listGames.value[listName] ?: emptyList()
    }
}
