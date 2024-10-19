package edu.ucne.inventario.data.remote

import edu.ucne.inventario.data.remote.dto.CategoriaDto
import edu.ucne.inventario.data.remote.dto.ProductoDto
import edu.ucne.inventario.data.remote.dto.ProovedorDto
import edu.ucne.inventario.data.remote.interfaces.CategoriaApi
import edu.ucne.inventario.data.remote.interfaces.ProductoApi
import edu.ucne.inventario.data.remote.interfaces.ProovedorApi
import javax.inject.Inject

class RemoteDataSource @Inject  constructor(
    private val productoApi: ProductoApi,
    private val categoriaApi: CategoriaApi,
    private val proovedorApi: ProovedorApi
){
    suspend fun getCategorias() = categoriaApi.getCategorias()
    suspend fun getCategoria(categoriaId: Int) = categoriaApi.getCategoriaById(categoriaId)
    suspend fun saveCategoria(categoria: CategoriaDto) = categoriaApi.saveCategoria(categoria)
    suspend fun deleteCategoria(categoriaId: Int) = categoriaApi.deleteCategoria(categoriaId)

    suspend fun getProovedores() = proovedorApi.getProovedores()
    suspend fun getProovedor(proovedorId: Int) = proovedorApi.getProovedorById(proovedorId)
    suspend fun saveProovedor(proovedor: ProovedorDto) = proovedorApi.saveProovedor(proovedor)
    suspend fun deleteProovedor(proovedorId: Int) = proovedorApi.deleteProovedor(proovedorId)

    suspend fun getProductos() = productoApi.getProductos()
    suspend fun getProducto(productoId: Int) = productoApi.getProductoById(productoId)
    suspend fun saveProducto(producto: ProductoDto) = productoApi.saveProducto(producto)
    suspend fun deleteProducto(productoId: Int) = productoApi.deleteProducto(productoId)

}