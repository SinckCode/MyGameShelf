package com.example.mygameshelf.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygameshelf.data.services.KtorfitClient
import com.example.mygameshelf.domain.dtos.company.CompanyDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CompaniesUiState(
    val isLoading: Boolean = false,
    val companies: List<CompanyDto> = emptyList(),
    val error: String? = null
)

class CompaniesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompaniesUiState())
    val uiState: StateFlow<CompaniesUiState> = _uiState

    init {
        loadCompanies()
    }

    fun loadCompanies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val service = KtorfitClient.createCompanyService()
                val result = service.getCompanies()

                _uiState.value = CompaniesUiState(
                    isLoading = false,
                    companies = result,
                    error = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = CompaniesUiState(
                    isLoading = false,
                    companies = emptyList(),
                    error = "Error al cargar compañías"
                )
            }
        }
    }
}
