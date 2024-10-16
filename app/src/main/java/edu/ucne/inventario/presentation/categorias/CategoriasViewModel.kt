package edu.ucne.inventario.presentation.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.inventario.data.remote.dto.CategoriaDto
import edu.ucne.inventario.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                categoriaRepository.saveCategoria(state.toEntity())
                _uiState.update {
                    it.copy(
                        nombreErrors = null,
                        descripcionErrors = null
                    )
                }
                nuevo()
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
            if (categoriaId > 0){
                val categoria = categoriaRepository.getCategoriaById(categoriaId)
                _uiState.update {
                    it.copy(
                        categoriaId = categoria.categoriaId,
                        nombre = categoria.nombre,
                        descripcion = categoria.descripcion
                    )
                }
            }
        }
    }

    fun delete(){
        viewModelScope.launch {
            val response = categoriaRepository.deleteCategoria(uiState.value.categoriaId!!)
            if (response.isSuccessful) {
                nuevo()
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

    class EmptyListException(message: String) : Exception(message)

    private fun getCategorias() {
        viewModelScope.launch {
            try {
                val categorias = categoriaRepository.getCategorias()
                if (categorias.isEmpty()) {
                    throw EmptyListException("No se encontraron categorías")
                }
                _uiState.update {
                    it.copy(categorias = categorias)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message)
                }
            }
        }
    }

    data class UiState(
        val categoriaId: Int? = null,
        val nombre: String = "",
        val descripcion: String = "",
        val nombreErrors: String? = null,
        val descripcionErrors: String? = null,
        val errorMessage: String? = null,
        val categorias: List<CategoriaDto> = emptyList()
    )

    fun UiState.toEntity() = CategoriaDto(
        categoriaId = categoriaId,
        nombre = nombre,
        descripcion = descripcion
    )
}
