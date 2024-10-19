package edu.ucne.inventario.presentation.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.inventario.data.local.entities.CategoriaEntity
import edu.ucne.inventario.data.remote.Resources
import edu.ucne.inventario.data.remote.dto.CategoriaDto
import edu.ucne.inventario.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriasViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getCategorias()
    }

    fun save() {
        viewModelScope.launch {
            val state = uiState.value
            var hasError = false

            if (state.nombre.isBlank()) {
                _uiState.update { it.copy(nombreErrors = "El nombre no puede estar vacío") }
                hasError = true
            }

            if (state.descripcion.isBlank()) {
                _uiState.update { it.copy(descripcionErrors = "La descripción no puede estar vacía") }
                hasError = true
            }

            if (!hasError) {
                categoriaRepository.saveCategoria(_uiState.value.toEntity()).collectLatest {resultado ->
                    when (resultado) {
                        is Resources.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }
                        is Resources.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }
                        }
                        is Resources.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = resultado.message
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun nuevo() {
        _uiState.update {
            it.copy(
                categoriaId = null,
                nombre = "",
                descripcion = "",
                nombreErrors = null,
                descripcionErrors = null
            )
        }
    }
    fun select(categoriaId: Int){
        viewModelScope.launch {
            categoriaRepository.getCategoriaById(categoriaId).collect { resultado ->
                when (resultado) {
                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                categoriaId = resultado.data?.categoriaId ?:0,
                                nombre = resultado.data?.nombre ?: "",
                                descripcion = resultado.data?.descripcion ?: "",
                            )
                        }
                    }
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resultado.message,
                                nombreErrors = resultado.message,
                                descripcionErrors = resultado.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun delete(){
        viewModelScope.launch {
            categoriaRepository.deleteCategoria(_uiState.value.categoriaId!!).let { resultado ->
                when(resultado){
                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                    }
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resultado.message
                            )
                        }
                    }
                }
            }
        }

    }

    fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(nombre = nombre, nombreErrors = null)
        }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion, descripcionErrors = null)
        }
    }



    private fun getCategorias() {
        viewModelScope.launch {
            categoriaRepository.getCategorias().collect{resultado ->
                when(resultado){
                    is Resources.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                categorias = resultado.data ?: emptyList()
                            )
                        }
                    }
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resultado.message,
                                nombreErrors = resultado.message,
                                descripcionErrors = resultado.message
                            )
                        }
                    }
                }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val categoriaId: Int? = null,
        val nombre: String = "",
        val descripcion: String = "",
        val nombreErrors: String? = null,
        val descripcionErrors: String? = null,
        val errorMessage: String? = null,
        val categorias: List<CategoriaEntity> = emptyList()
    )

    fun UiState.toEntity() = CategoriaDto(
        categoriaId = categoriaId,
        nombre = nombre,
        descripcion = descripcion
    )
}
