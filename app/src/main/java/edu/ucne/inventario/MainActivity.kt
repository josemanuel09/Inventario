package edu.ucne.inventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.inventario.presentation.navigation.InventarioNavHost
import edu.ucne.inventario.ui.theme.InventarioTheme

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventarioTheme {
                val navHost = rememberNavController()
                val items = NavigationItems()
                InventarioNavHost(navHostController = navHost, items = items)

            }
        }
    }
}

fun NavigationItems() : List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = "Proovedores",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,

            ),
        NavigationItem(
            title = "Categorias",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Build,
        ),
        NavigationItem(
            title = "Productos",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Build,
        )
    )
}

@Composable
fun InventarioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}