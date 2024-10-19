package edu.ucne.inventario.presentation.proveedores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.inventario.data.local.entities.ProovedorEntity
import edu.ucne.inventario.data.remote.Resources
import edu.ucne.inventario.data.remote.dto.ProovedorDto
import edu.ucne.inventario.data.repository.ProovedorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProovedoresViewModel @Inject constructor(
    private val proveedorRepository: ProovedorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getProveedores()
    }

    fun save() {
        viewModelScope.launch {
            val state = uiState.value
            var hasError = false

            if (state.nombre.isBlank()) {
                _uiState.update { it.copy(nombreErrors = "El Nombre no puede estar vacío") }
                hasError = true
            }

            if (state.contacto.isBlank()) {
                _uiState.update { it.copy(contactoErrors = "El Contacto no puede estar vacío") }
                hasError = true
            }

            if (state.direccion.isBlank()) {
                _uiState.update { it.copy(direccionErrors = "La Dirección no puede estar vacía") }
                hasError = true
            }

            if (!hasError) {
                proveedorRepository.saveProovedor(state.toEntity()).collectLatest { resultado ->
                    when (resultado) {
                        is Resources.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                        is Resources.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    nombreErrors = null,
                                    contactoErrors = null,
                                    direccionErrors = null
                                )
                            }
                            nuevo()
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
                proveedorId = null,
                nombre = "",
                contacto = "",
                direccion = "",
                nombreErrors = null,
                contactoErrors = null,
                direccionErrors = null
            )
        }
    }

    fun delete() {
        viewModelScope.launch {
            proveedorRepository.deleteProovedor(uiState.value.proveedorId!!).let { resultado ->
                when (resultado) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resources.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        nuevo()
                    }
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = resultado.message)
                        }
                    }
                }
            }
        }
    }

    fun selectProveedor(proveedorId: Int) {
        viewModelScope.launch {
            proveedorRepository.getProovedorById(proveedorId).collectLatest { resultado ->
                when (resultado) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                proveedorId = resultado.data?.proovedorId,
                                nombre = resultado.data?.nombre ?: "",
                                contacto = resultado.data?.contacto ?: "",
                                direccion = resultado.data?.direccion ?: ""
                            )
                        }
                    }
                    is Resources.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resultado.message,
                                nombreErrors = resultado.message,
                                contactoErrors = resultado.message,
                                direccionErrors = resultado.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, nombreErrors = null) }
    }

    fun onContactoChange(contacto: String) {
        _uiState.update { it.copy(contacto = contacto, contactoErrors = null) }
    }

    fun onDireccionChange(direccion: String) {
        _uiState.update { it.copy(direccion = direccion, direccionErrors = null) }
    }

    private fun getProveedores() {
        viewModelScope.launch {
            proveedorRepository.getProovedores().collectLatest { resultado ->
                when (resultado) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                proveedores = resultado.data ?: emptyList()
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

    data class UiState(
        val isLoading: Boolean = false,
        val proveedorId: Int? = null,
        val nombre: String = "",
        val contacto: String = "",
        val direccion: String = "",
        val nombreErrors: String? = null,
        val contactoErrors: String? = null,
        val direccionErrors: String? = null,
        val errorMessage: String? = null,
        val proveedores: List<ProovedorEntity> = emptyList()
    )

    fun UiState.toEntity() = ProovedorDto(
        proovedorId = proveedorId,
        nombre = nombre,
        contacto = contacto,
        direccion = direccion
    )
}
