package com.example.mygameshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.mygameshelf.ui.screens.Auth.LoginScreen
import com.example.mygameshelf.ui.screens.Auth.RegisterScreen
import com.example.mygameshelf.ui.screens.HomeScreen.components.MyBottomBar
import com.example.mygameshelf.ui.screens.MainScreen
import com.example.mygameshelf.ui.theme.FavoritesScreenRoute
import com.example.mygameshelf.ui.theme.LoginScreenRoute
import com.example.mygameshelf.ui.theme.MainScreenGraph
import com.example.mygameshelf.ui.theme.MainScreenRoute
import com.example.mygameshelf.ui.theme.MyGameShelfTheme
import com.example.mygameshelf.ui.theme.RegisterScreenRoute
import com.example.mygameshelf.ui.theme.SearchScreenRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyGameShelfTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination

                // Bottom bar solo en las pantallas principales
                val showBottomBar =
                    currentDestination.isRoute<MainScreenRoute>() ||
                            currentDestination.isRoute<FavoritesScreenRoute>() ||
                            currentDestination.isRoute<SearchScreenRoute>()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            MyBottomBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = LoginScreenRoute
                    ) {
                        composable<LoginScreenRoute> {
                            LoginScreen(
                                navController = navController,
                                contentPadding = innerPadding
                            )
                        }

                        composable<RegisterScreenRoute> {
                            RegisterScreen(
                                navController = navController,
                                contentPadding = innerPadding
                            )
                        }

                        navigation<MainScreenGraph>(
                            startDestination = MainScreenRoute
                        ) {
                            // HOME (usa tu MainScreen actual, que llama a HomeScreen)
                            composable<MainScreenRoute> {
                                MainScreen(
                                    navController = navController,
                                    contentPadding = innerPadding
                                )
                            }

                            // FAVORITES / LISTAS (por ahora placeholder)
                            composable<FavoritesScreenRoute> {
                                FavoritesPlaceholder()
                            }

                            // SEARCH (por ahora placeholder)
                            composable<SearchScreenRoute> {
                                SearchPlaceholder()
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Helpers y previews ---

// Helper propio para rutas tipadas (@Serializable object ...)
private inline fun <reified T> NavDestination?.isRoute(): Boolean =
    this?.route == T::class.qualifiedName

// Placeholders rápidos hasta que conectes tus pantallas reales
@Composable
private fun FavoritesPlaceholder() {
    androidx.compose.material3.Text("Favorites screen")
}

@Composable
private fun SearchPlaceholder() {
    androidx.compose.material3.Text("Search screen")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) =
    androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyGameShelfTheme {
        Greeting("Android")
    }
}
