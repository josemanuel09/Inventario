package edu.ucne.inventario.presentation.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.inventario.data.remote.dto.CategoriaDto
import edu.ucne.inventario.data.remote.dto.ProductoDto
import edu.ucne.inventario.data.remote.dto.ProovedorDto
import edu.ucne.inventario.data.repository.CategoriaRepository
import edu.ucne.inventario.data.repository.ProductoRepository
import edu.ucne.inventario.data.repository.ProovedorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            if (_uiState.value.nombre.isBlank() || _uiState.value.descripcion.isBlank() ||
                _uiState.value.precio <= 0 || _uiState.value.cantidad <= 0 ||
                _uiState.value.categoriaId == null || _uiState.value.proovedorId == null
            ) {
                _uiState.update { it.copy(errorMessage = "Todos los campos son obligatorios y válidos.") }
                return@launch
            }


                productosRepository.saveProducto(_uiState.value.toEntity())
                nuevo()

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
                errorMessage = null
            )
        }
    }

    fun selectProducto(productoId: Int) {
        viewModelScope.launch {
            if (productoId > 0) {
                val producto = productosRepository.getProductoById(productoId)
                producto?.let {
                    _uiState.update {
                        it.copy(
                            productoId = producto.productoId,
                            nombre = producto.nombre,
                            descripcion = producto.descripcion,
                            precio = producto.precio,
                            cantidad = producto.cantidad,
                            categoriaId = producto.categoriaId,
                            proovedorId = producto.proovedorId
                        )
                    }
                }
            }
        }
    }

    fun delete(){
            viewModelScope.launch {

                if (_uiState.value.productoId != null && _uiState.value.productoId!! > 0) {
                    val response = productosRepository.deleteProducto(_uiState.value.productoId!!)
                    if (response.isSuccessful) {
                        nuevo()
                        getProductos()
                    } else {
                        _uiState.update { it.copy(errorMessage = "Error al eliminar el producto") }
                    }
                } else {
                    _uiState.update { it.copy(errorMessage = "ID de producto no válido") }
                }
            }
    }

    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre) }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion) }
    }

    fun onPrecioChange(precio: Double) {
        _uiState.update { it.copy(precio = precio) }
    }

    fun onCantidadChange(cantidad: Int) {
        _uiState.update { it.copy(cantidad = cantidad) }
    }

    fun onCategoriaChange(categoriaId: Int) {
        _uiState.update { it.copy(categoriaId = categoriaId) }
    }

    fun onProveedorChange(proveedorId: Int) {
        _uiState.update { it.copy(proovedorId = proveedorId) }
    }



    private fun getProductos() {
        viewModelScope.launch {
            val productos = productosRepository.getProductos()
            _uiState.update { it.copy(productos = productos) }
        }
    }

    private fun getCategorias() {
        viewModelScope.launch {
            try {
                val categorias = categoriaRepository.getCategorias()
                _uiState.update { it.copy(categorias = categorias) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al cargar categorías: ${e.message}") }
            }
        }
    }

    private fun getProveedores() {
        viewModelScope.launch {
            try {
                val proveedores = proveedorRepository.getProovedores()
                _uiState.update { it.copy(proveedores = proveedores) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al cargar proveedores: ${e.message}") }
            }
        }
    }

    data class UiState(
        val productoId: Int? = null,
        val nombre: String = "",
        val descripcion: String = "",
        val precio: Double = 0.0,
        val cantidad: Int = 0,
        val categoriaId: Int? = null,
        val proovedorId: Int? = null,
        val errorMessage: String? = null,
        val productos: List<ProductoDto> = emptyList(),
        val categorias: List<CategoriaDto> = emptyList(),
        val proveedores: List<ProovedorDto> = emptyList()
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
