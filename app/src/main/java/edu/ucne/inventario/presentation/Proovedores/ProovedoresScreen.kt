package edu.ucne.inventario.presentation.Proovedores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProovedoresScreen(viewModel: ProovedoresViewModel = hiltViewModel(), goBack: () -> Unit){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ProovedoresBodyScreen(
        uiState = uiState,
        onNombreChange = viewModel::onNombreChange,
        onContactoChange = viewModel::onContactoChange,
        onDireccionChange = viewModel::onDireccionChange,
        onSave = viewModel::save,
        onNuevo = viewModel::nuevo,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProovedoresBodyScreen(
    uiState: ProovedoresViewModel.UiState,
    onNombreChange: (String) -> Unit,
    onContactoChange: (String) -> Unit,
    onDireccionChange: (String) -> Unit,
    onSave: () -> Unit,
    onNuevo: () -> Unit,
    goBack: () -> Unit

){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registro de Proovedores",
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
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Nombres") },
                value = uiState.nombre,
                onValueChange = onNombreChange,
                isError = uiState.nombreErrors != null
            )
            uiState.nombreErrors?.let{ error ->
                Text(
                    text = error,
                    color = Color.Red
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Contacto") },
                value = uiState.contacto,
                onValueChange = onContactoChange,
                isError = uiState.contactoErrors != null
            )
            uiState.contactoErrors?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = TextStyle(fontSize = 12.sp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Dirección") },
                value = uiState.direccion,
                onValueChange = onDireccionChange,
                isError = uiState.direccionErrors != null
            )
            uiState.direccionErrors?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = TextStyle(fontSize = 12.sp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onSave
                ) {
                    Text(text = "Guardar")
                    Icon(Icons.Filled.Add, contentDescription = "Guardar")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onNuevo
                ) {
                    Text(text = "Nuevo")
                    Icon(Icons.Filled.Refresh, contentDescription = "Nuevo")
                }
            }


        }

    }
}