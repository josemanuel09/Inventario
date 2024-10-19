package edu.ucne.inventario.presentation.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.inventario.data.local.entities.CategoriaEntity
import edu.ucne.inventario.data.local.entities.ProductoEntity
import edu.ucne.inventario.data.local.entities.ProovedorEntity
import edu.ucne.inventario.data.remote.Resources
import edu.ucne.inventario.data.remote.dto.ProductoDto
import edu.ucne.inventario.data.repository.CategoriaRepository
import edu.ucne.inventario.data.repository.ProductoRepository
import edu.ucne.inventario.data.repository.ProovedorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductosViewModel @Inject constructor(
    private val productosRepository: ProductoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val proveedorRepository: ProovedorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getProductos()
        getCategorias()
        getProveedores()
    }

    fun saveProducto() {
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

            if (state.precio <= 0) {
                _uiState.update { it.copy(precioErrors = "El precio debe ser mayor a 0") }
                hasError = true
            }

            if (state.cantidad <= 0) {
                _uiState.update { it.copy(cantidadErrors = "La cantidad debe ser mayor a 0") }
                hasError = true
            }

            if (state.categoriaId == null) {
                _uiState.update { it.copy(categoriaErrors = "Debe seleccionar una categoría") }
                hasError = true
            }

            if (state.proovedorId == null) {
                _uiState.update { it.copy(proveedorErrors = "Debe seleccionar un proveedor") }
                hasError = true
            }

            if (!hasError) {
                productosRepository.saveProducto(state.toEntity()).collectLatest { resultado ->
                    when (resultado) {
                        is Resources.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                        is Resources.Success -> {
                            _uiState.update { it.copy(isLoading = false) }
                            nuevo()
                            getProductos()
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
                productoId = 0,
                nombre = "",
                descripcion = "",
                precio = 0.0,
                cantidad = 0,
                categoriaId = null,
                proovedorId = null,
                nombreErrors = null,
                descripcionErrors = null,
                precioErrors = null,
                cantidadErrors = null,
                categoriaErrors = null,
                proveedorErrors = null,
                errorMessage = null
            )
        }
    }

    fun selectProducto(productoId: Int) {
        viewModelScope.launch {
            productosRepository.getProductoById(productoId).collectLatest { resultado ->
                when (resultado) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                productoId = resultado.data?.productoId ?: 0,
                                nombre = resultado.data?.nombre ?: "",
                                descripcion = resultado.data?.descripcion ?: "",
                                precio = resultado.data?.precio ?: 0.0,
                                cantidad = resultado.data?.cantidad ?: 0,
                                categoriaId = resultado.data?.categoriaId,
                                proovedorId = resultado.data?.proovedorId
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

    fun delete() {
        viewModelScope.launch {
            productosRepository.deleteProducto(_uiState.value.productoId!!).let { resultado ->
                when (resultado) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resources.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        nuevo()
                        getProductos()
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
        _uiState.update { it.copy(nombre = nombre, nombreErrors = null) }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion, descripcionErrors = null) }
    }

    fun onPrecioChange(precio: Double) {
        _uiState.update { it.copy(precio = precio, precioErrors = null) }
    }

    fun onCantidadChange(cantidad: Int) {
        _uiState.update { it.copy(cantidad = cantidad, cantidadErrors = null) }
    }

    fun onCategoriaChange(categoriaId: Int) {
        _uiState.update { it.copy(categoriaId = categoriaId, categoriaErrors = null) }
    }

    fun onProveedorChange(proveedorId: Int) {
        _uiState.update { it.copy(proovedorId = proveedorId, proveedorErrors = null) }
    }

    private fun getProductos() {
        viewModelScope.launch {
            productosRepository.getProductos().collectLatest { resultado ->
                when (resultado) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, productos = resultado.data ?: emptyList())
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

    private fun getCategorias() {
        viewModelScope.launch {
            categoriaRepository.getCategorias().collectLatest { resultado ->
                when (resultado) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, categorias = resultado.data ?: emptyList())
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

    private fun getProveedores() {
        viewModelScope.launch {
            proveedorRepository.getProovedores().collectLatest { resultado ->
                when (resultado) {
                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resources.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, proveedores = resultado.data?: emptyList())
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
        val productoId: Int? = null,
        val nombre: String = "",
        val descripcion: String = "",
        val precio: Double = 0.0,
        val cantidad: Int = 0,
        val categoriaId: Int? = null,
        val proovedorId: Int? = null,
        val nombreErrors: String? = null,
        val descripcionErrors: String? = null,
        val precioErrors: String? = null,
        val cantidadErrors: String? = null,
        val categoriaErrors: String? = null,
        val proveedorErrors: String? = null,
        val errorMessage: String? = null,
        val productos: List<ProductoEntity> = emptyList(),
        val categorias: List<CategoriaEntity> = emptyList(),
        val proveedores: List<ProovedorEntity> = emptyList()
    )

    fun UiState.toEntity() = ProductoDto(
        productoId = productoId,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        cantidad = cantidad,
        categoriaId = categoriaId ?: 0,
        proovedorId = proovedorId ?: 0
    )
}
