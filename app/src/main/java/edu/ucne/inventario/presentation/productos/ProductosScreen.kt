package edu.ucne.inventario.presentation.productos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProductoScreen(viewModel: ProductosViewModel = hiltViewModel(), goBack: () -> Unit){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    ProductoBodyScreen(
        uiState = uiState.value,
        onNombreChange = viewModel::onNombreChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onPrecioChange = viewModel::onPrecioChange,
        onCantidadChange = viewModel::onCantidadChange,
        onCategoriaChange = viewModel::onCategoriaChange,
        onProveedorChange = viewModel::onProveedorChange,
        onSave = viewModel::saveProducto,
        onNuevo = viewModel::nuevo,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoBodyScreen(
    uiState: ProductosViewModel.UiState,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrecioChange: (Double) -> Unit,
    onCantidadChange: (Int) -> Unit,
    onCategoriaChange: (Int) -> Unit,
    onProveedorChange: (Int) -> Unit,
    onSave: () -> Unit,
    onNuevo: () -> Unit,
    goBack: () -> Unit
) {
    var categoriaExpanded by remember { mutableStateOf(false) }
    var proveedorExpanded by remember { mutableStateOf(false) }

    var selectedCategoriaText by remember { mutableStateOf("Selecciona una categoria") }
    var selectedProveedorText by remember { mutableStateOf("Selecciona un proveedor") }

    val categorias = uiState.categorias
    val proveedores = uiState.proveedores

    if (uiState.categoriaId == null) selectedCategoriaText = "Seleccionar Categoria"
    if (uiState.proovedorId == null) selectedProveedorText = "Seleccionar Proveedor"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registro de Productos",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Nombre") },
                value = uiState.nombre,
                onValueChange = onNombreChange
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Descripcion") },
                value = uiState.descripcion,
                onValueChange = onDescripcionChange
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Precio") },
                value = uiState.precio.toString(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    val newValue = it.toDoubleOrNull() ?: 0.0
                    onPrecioChange(newValue)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Cantidad") },
                value = uiState.cantidad.toString(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    val newValue = newValue.toIntOrNull() ?: 0
                    onCantidadChange(newValue)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { categoriaExpanded = true },
                label = { Text(text = "Categoria") },
                value = selectedCategoriaText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Menu desplegable",
                        modifier = Modifier.clickable { categoriaExpanded = !categoriaExpanded }
                    )
                }
            )
            DropdownMenu(
                expanded = categoriaExpanded,
                onDismissRequest = { categoriaExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                categorias.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.nombre) },
                        onClick = {
                            onCategoriaChange(it.categoriaId ?: 0)
                            selectedCategoriaText = it.nombre
                            categoriaExpanded = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { proveedorExpanded = true },
                label = { Text(text = "Proveedor") },
                value = selectedProveedorText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Menu desplegable",
                        modifier = Modifier.clickable { proveedorExpanded = !proveedorExpanded }
                    )
                }
            )
            DropdownMenu(
                expanded = proveedorExpanded,
                onDismissRequest = { proveedorExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                proveedores.forEach { proveedor ->
                    DropdownMenuItem(
                        text = { Text(text = proveedor.nombre) },
                        onClick = {
                            onProveedorChange(proveedor.proovedorId ?: 0)
                            selectedProveedorText = proveedor.nombre
                            proveedorExpanded = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onSave() }
                ) {
                    Text(text = "Guardar")
                    Icon(Icons.Default.Add, contentDescription = "Guardar")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onNuevo()
                        selectedProveedorText = "Seleccionar Proveedor"
                        selectedCategoriaText = "Seleccionar categoria"
                    }
                ) {
                    Text(text = "Nuevo")
                    Icon(Icons.Default.Refresh, contentDescription = "Nuevo")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
