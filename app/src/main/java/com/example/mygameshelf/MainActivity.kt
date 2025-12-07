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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.mygameshelf.ui.screens.Auth.LoginScreen
import com.example.mygameshelf.ui.screens.Auth.RegisterScreen
import com.example.mygameshelf.ui.screens.HomeScreen.Detail.CompanyDetail.DetailCompany
import com.example.mygameshelf.ui.screens.HomeScreen.Detail.GameDetail.DetailGame
import com.example.mygameshelf.ui.screens.HomeScreen.ListView.CreateListView.CreateListView
import com.example.mygameshelf.ui.screens.HomeScreen.ListView.ListView
import com.example.mygameshelf.ui.screens.HomeScreen.ListViewDetail.AddList.AddList
import com.example.mygameshelf.ui.screens.HomeScreen.ListViewDetail.ListViewDetail
import com.example.mygameshelf.ui.screens.HomeScreen.Search.SearchScreen
import com.example.mygameshelf.ui.screens.HomeScreen.UserView.UserView
import com.example.mygameshelf.ui.screens.HomeScreen.components.MyBottomBar
import com.example.mygameshelf.ui.screens.MainScreen
import com.example.mygameshelf.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyGameShelfTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination

                val showBottomBar =
                    currentDestination.isRoute<MainScreenRoute>() ||
                            currentDestination.isRoute<ListViewRoute>()   ||
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
                        // ---------- AUTH ----------
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

                        // ---------- NAVEGACIÓN PRINCIPAL ----------
                        navigation<MainScreenGraph>(
                            startDestination = MainScreenRoute
                        ) {
                            // HOME
                            composable<MainScreenRoute> {
                                MainScreen(
                                    navController = navController,
                                    contentPadding = innerPadding
                                )
                            }

                            // LISTAS (tab central)
                            composable<ListViewRoute> {
                                ListView(
                                    navController = navController,
                                    contentPadding = innerPadding
                                )
                            }

                            // SEARCH (tab derecha – lupa)
                            composable<SearchScreenRoute> {
                                SearchScreen(
                                    navController = navController,
                                    contentPadding = innerPadding
                                )
                            }

                            // USER VIEW (perfil, no tiene tab propia)
                            composable<UserViewRoute> {
                                UserView(
                                    navController = navController,
                                    contentPadding = innerPadding
                                )
                            }

                            // ---------- LISTAS ----------
                            composable<CreateListRoute> {
                                CreateListView(
                                    navController = navController,
                                    contentPadding = innerPadding
                                )
                            }

                            composable<ListDetailRoute> {
                                ListViewDetail(
                                    navController = navController,
                                    contentPadding = innerPadding
                                )
                            }

                            composable<AddListRoute> {
                                AddList(
                                    navController = navController,
                                    contentPadding = innerPadding
                                )
                            }
                            // ---------- DETALLE DE GAME / COMPANY ----------
                            composable<DetailGameRoute> { backStackEntry ->
                                val args: DetailGameRoute = backStackEntry.toRoute()
                                DetailGame(gameId = args.gameId)
                            }

                            composable<DetailCompanyRoute> { backStackEntry ->
                                val args: DetailCompanyRoute = backStackEntry.toRoute()
                                DetailCompany(companyId = args.companyId)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper
private inline fun <reified T> NavDestination?.isRoute(): Boolean =
    this?.route == T::class.qualifiedName


@Composable
fun Greeting(name: String) =
    androidx.compose.material3.Text(text = "Hello $name!")

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyGameShelfTheme {
        Greeting("Android")
    }
}
