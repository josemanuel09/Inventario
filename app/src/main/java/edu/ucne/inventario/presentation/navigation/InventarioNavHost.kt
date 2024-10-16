package edu.ucne.inventario.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import edu.ucne.inventario.NavigationItem
import edu.ucne.inventario.presentation.Proovedores.EditProveedoresScreen
import edu.ucne.inventario.presentation.Proovedores.ProovedoresListScreen
import edu.ucne.inventario.presentation.Proovedores.ProovedoresScreen
import edu.ucne.inventario.presentation.categorias.CategoriasListScreen
import edu.ucne.inventario.presentation.categorias.CategoriasScreen
import edu.ucne.inventario.presentation.categorias.DeleteCategoriaScreen
import edu.ucne.inventario.presentation.categorias.EditCategoriasScreen
import edu.ucne.inventario.presentation.productos.DeleteProductoScreen
import edu.ucne.inventario.presentation.productos.EditProductosScreen
import edu.ucne.inventario.presentation.productos.ProductoListScreen
import edu.ucne.inventario.presentation.productos.ProductoScreen
import edu.ucne.inventario.presentation.proveedores.DeleteProovedorScreen

import kotlinx.coroutines.launch

@Composable
fun InventarioNavHost(
    items: List<NavigationItem>,
    navHostController: NavHostController = rememberNavController(),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.background(Color(0xFFF5F5F5))
            ) {
                DrawerHeader(
                    modifier = Modifier.padding(16.dp),
                    text = "Inventario"
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                items.forEachIndexed { index, navigationItem ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = navigationItem.title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (index == selectedItem) Color.Blue else Color.Black
                            )
                        },
                        selected = index == selectedItem,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem = index
                            when (navigationItem.title) {
                                "Productos" -> navHostController.navigate(Screen.ProductosList)
                                "Categorias" -> navHostController.navigate(Screen.CategoriasList)
                                "Proveedores" -> navHostController.navigate(Screen.ProovedoresList)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            selectedContainerColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.ProductosList
        ) {
            composable<Screen.ProovedoresList> {
                ProovedoresListScreen(
                    drawerState = drawerState,
                    scope = scope,
                    create = {
                        navHostController.navigate(Screen.Proovedores(0))
                    },
                    onDelete = { proovedor ->
                        navHostController.navigate(Screen.DeleteProovedor(proovedor))
                    },
                    onEdit = { proovedor ->
                        navHostController.navigate(Screen.EditProovedor(proovedor))
                    }
                )
            }
            composable<Screen.CategoriasList> {
                CategoriasListScreen(
                    drawerState = drawerState,
                    scope = scope,
                    create = {
                        navHostController.navigate(Screen.Categorias(0))
                    },
                    onDelete = { categoria ->
                        navHostController.navigate(Screen.DeleteCategoria(categoria))
                    },
                    onEdit = { categoria ->
                        navHostController.navigate(Screen.EditCategoria(categoria))
                    }
                )
            }
            composable<Screen.ProductosList> {
                ProductoListScreen(
                    drawerState = drawerState,
                    scope = scope,
                    createProducto = {
                        navHostController.navigate(Screen.Productos(0))
                    },
                    onDelete = { producto ->
                        navHostController.navigate(Screen.DeleteProducto(producto))
                    },
                    onEdit = { producto ->
                        navHostController.navigate(Screen.EditProducto(producto))
                    }
                )
            }
            composable<Screen.DeleteProducto> {
                val args = it.toRoute<Screen.DeleteProducto>()
                DeleteProductoScreen(
                    productoId = args.productoId,
                    goBack = {
                        navHostController.navigateUp()
                    }
                )
            }
            composable<Screen.DeleteProovedor> {
                val args = it.toRoute<Screen.DeleteProovedor>()
                DeleteProovedorScreen(
                    proovedorId = args.proovedorId,
                    goBack = {
                        navHostController.navigateUp()
                    }
                )
            }
            composable<Screen.EditProducto> {
                val args = it.toRoute<Screen.EditProducto>()
                EditProductosScreen(
                    productoId = args.productoId,
                    goBack = {
                        navHostController.navigate(Screen.ProductosList)
                    }
                )
            }
            composable<Screen.EditProovedor> {
                val args = it.toRoute<Screen.EditProovedor>()
                EditProveedoresScreen(
                    proveedorId = args.proovedorId,
                    goBack = {
                        navHostController.navigate(Screen.ProovedoresList)
                    }
                )
            }

            composable<Screen.Proovedores> {
                val args = it.toRoute<Screen.Proovedores>()
                ProovedoresScreen(
                    goBack = {
                        navHostController.navigate(Screen.ProductosList)
                    }
                )
            }
            composable<Screen.Categorias> {
                val args = it.toRoute<Screen.Categorias>()
                CategoriasScreen(
                    goBack = {
                        navHostController.navigate(Screen.ProductosList)
                    }
                )
            }
            composable<Screen.Productos> {
                val args = it.toRoute<Screen.Productos>()
                ProductoScreen(
                    goBack = {
                        navHostController.navigate(Screen.ProductosList)
                    }
                )
            }
            composable<Screen.DeleteCategoria> {
                val args = it.toRoute<Screen.DeleteCategoria>()
                DeleteCategoriaScreen(
                    categoriaId = args.categoriaId,
                    goBack = {
                        navHostController.navigateUp()
                    }
                )
            }
            composable<Screen.EditCategoria> {
                val args = it.toRoute<Screen.EditCategoria>()
                EditCategoriasScreen(
                    categoriaId = args.categoriaId,
                    goBack = {
                        navHostController.navigate(Screen.CategoriasList)
                    }
                )
            }
        }
    }
}

@Composable
fun DrawerHeader(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF2196F3))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = "Icono de inventario",
            tint = Color.White,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFF1976D2))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}
