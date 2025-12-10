package com.example.mygameshelf.ui.screens.HomeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mygameshelf.ui.theme.ListViewRoute
import com.example.mygameshelf.ui.theme.MainScreenGraph
import com.example.mygameshelf.ui.theme.MainScreenRoute
import com.example.mygameshelf.ui.theme.SearchScreenRoute

@Composable
fun MyBottomBar(
    navController: NavHostController
) {
    val colors = MaterialTheme.colorScheme
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Pastilla principal (Home + Listas)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(end = 40.dp),   // espacio para el botón redondo
            shape = RoundedCornerShape(30.dp),
            tonalElevation = 6.dp,
            color = colors.surface
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // HOME
                BottomBarItem(
                    icon = Icons.Outlined.Home,
                    selected = currentDestination.isRoute<MainScreenRoute>(),
                    onClick = {
                        navController.navigate(MainScreenRoute) {
                            // No dupliques Home si ya estás ahí
                            launchSingleTop = true

                            // Rompe todo lo que esté encima de Home
                            popUpTo(MainScreenRoute::class.qualifiedName!!) {
                                inclusive = false
                            }
                        }
                    }
                )

                // LISTAS
                BottomBarItem(
                    icon = Icons.Outlined.LibraryBooks,
                    selected = currentDestination.isRoute<ListViewRoute>(),
                    onClick = {
                        navController.navigate(ListViewRoute) {
                            launchSingleTop = true

                            // Siempre nos aseguramos de “volver” a partir de Home
                            popUpTo(MainScreenRoute::class.qualifiedName!!) {
                                inclusive = false
                            }
                        }
                    }
                )
            }
        }

        // Botón circular de búsqueda
        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(56.dp)
                .clickable {
                    navController.navigate(SearchScreenRoute) {
                        launchSingleTop = true
                        popUpTo(MainScreenRoute::class.qualifiedName!!) {
                            inclusive = false
                        }
                    }
                },
            shape = CircleShape,
            tonalElevation = 10.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF6366F1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Buscar",
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier
            .size(26.dp)
            .clickable(onClick = onClick),
        tint = if (selected) colors.primary else colors.onSurfaceVariant
    )
}

/**
 * Helper propio para rutas tipadas (@Serializable object ...)
 */
private inline fun <reified T> NavDestination?.isRoute(): Boolean =
    this?.route == T::class.qualifiedName
