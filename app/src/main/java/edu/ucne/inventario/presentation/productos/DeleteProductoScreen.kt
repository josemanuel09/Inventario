package edu.ucne.inventario.presentation.productos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun DeleteProductoScreen(
    viewModel: ProductosViewModel = hiltViewModel(),
    productoId: Int,
    goBack: () -> Unit
){
    LaunchedEffect(productoId) {
        viewModel.selectProducto(productoId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DeleteProductoBodyScreen(
        uiState = uiState,
        onDelete = {
            viewModel.delete()
            goBack()
        },
        goBack = goBack
    )
}

@Composable
fun DeleteProductoBodyScreen(
    uiState: ProductosViewModel.UiState,
    onDelete: (Int) -> Unit,
    goBack: () -> Unit
){
    Scaffold(
        topBar = {
            Text(
                text = "¿Está seguro de eliminar el Producto?",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            )
        }
    ){paddingValues->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ){
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ){
                    Text(
                        text = "Nombre: ${uiState.nombre}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Descripcion: ${uiState.descripcion}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Precio: ${uiState.precio}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Cantidad: ${uiState.cantidad}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    val categoria = uiState.categorias.find { it.categoriaId == uiState.categoriaId }
                    Text(
                        text = "Prioridad: ${categoria?.nombre ?: "Desconocida"}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    val proveedor = uiState.proveedores.find { it.proovedorId == uiState.proovedorId }
                    Text(
                        text = "Prioridad: ${proveedor?.nombre ?: "Desconocida"}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {

                            onDelete(uiState.productoId!!)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ){
                        Text(text = "Eliminar")
                    }

                    Button(
                        onClick = {
                            goBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "Cancelar")
                    }
                }
            }
        }
    }
}