package edu.ucne.inventario.presentation.Proovedores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.inventario.data.remote.dto.ProovedorDto
import edu.ucne.inventario.data.repository.ProovedorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProovedoresViewModel @Inject constructor(
    private val proovedoRepository: ProovedorRepository

): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    init{
        getProovedores()
    }

    fun  save(){
        viewModelScope.launch {
            val state = uiState.value
            var hasError = false

            if (state.nombre.isBlank()){
                _uiState.update { it.copy(nombreErrors = "El Nombre no puede estar vacio") }
                hasError = true

            }

            if (state.contacto.isBlank()) {
                _uiState.update { it.copy(contactoErrors = "El Contacto no puede estar vacio") }
                hasError = true
            }
            if (state.direccion.isBlank()) {
                _uiState.update { it.copy(direccionErrors = "La Direccion no puede estar vacio") }
                hasError = true
            }

            if (!hasError){
                   proovedoRepository.saveProovedor(state.toEntity())
                    _uiState.update {
                        it.copy(
                            nombreErrors = null,
                            contactoErrors = null,
                            direccionErrors = null
                        )
                    }
                    nuevo()

            }
        }

    }
    fun nuevo(){
        _uiState.update {
            it.copy(
                proovedorId = null,
                nombre = "",
                contacto = "",
                direccion = "",
                nombreErrors = null,
                contactoErrors = null,
                direccionErrors = null
            )
        }
    }

    fun delete(){
        viewModelScope.launch {
            val response = proovedoRepository.deleteProovedor(uiState.value.proovedorId!!)
            if (response.isSuccessful){
                nuevo()
            }
        }
    }
    fun SelectProovedor(proovedorId: Int){
        viewModelScope.launch {
            if (proovedorId > 0){
                val proovedor = proovedoRepository.getProovedorById(proovedorId)
                _uiState.update {
                    it.copy(
                        proovedorId = proovedor.proovedorId,
                        nombre = proovedor.nombre,
                        contacto = proovedor.contacto,
                        direccion = proovedor.direccion,
                    )
                }

            }
        }
    }

    fun onNombreChange(nombre: String){
        _uiState.update {
            it.copy(nombre = nombre, nombreErrors = null)
        }
    }

    fun onContactoChange(contacto: String){
        _uiState.update {
            it.copy(contacto = contacto, contactoErrors = null)
        }

    }
    fun onDireccionChange(direccion: String){
        _uiState.update {
            it.copy(direccion = direccion, direccionErrors = null)
        }
    }
    class EmptyListException(message: String) : Exception(message)

    private fun getProovedores(){
        viewModelScope.launch {
            try{
                val proovedores = proovedoRepository.getProovedores()
                if (proovedores.isEmpty()){
                    throw EmptyListException("No se encontraron Proovedores")
                }
                _uiState.update {
                    it.copy(proovedores = proovedores)
                }

            }catch (e: Exception){
                _uiState.update {
                    it.copy(errorMessage = e.message)
                }
            }
        }
    }

    data class UiState(
        val proovedorId: Int? = null,
        val nombre: String = "",
        val contacto: String = "",
        val direccion: String = "",
        val nombreErrors: String? = null,
        val contactoErrors: String? = null,
        val direccionErrors: String? = null,
        val errorMessage: String? = null,
        val proovedores: List<ProovedorDto> = emptyList()

    )

    fun UiState.toEntity() = ProovedorDto(
        proovedorId = proovedorId,
        nombre = nombre,
        contacto = contacto,
        direccion = direccion
    )
}