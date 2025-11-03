package com.example.mygameshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.mygameshelf.ui.screens.Auth.LoginScreen
import com.example.mygameshelf.ui.screens.Auth.RegisterScreen
import com.example.mygameshelf.ui.screens.MainScren
import com.example.mygameshelf.ui.theme.HomeScreenRoute
import com.example.mygameshelf.ui.theme.LoginScreenRoute
import com.example.mygameshelf.ui.theme.MainScreenGraph
import com.example.mygameshelf.ui.theme.MainScreenRoute
import com.example.mygameshelf.ui.theme.MyGameShelfTheme
import com.example.mygameshelf.ui.theme.RegisterScreenRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyGameShelfTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    bottomBar = {

                    }

                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = LoginScreenRoute
                    ){
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
                        ){
                            composable<MainScreenRoute> {
                                MainScren(
                                    contentPadding = innerPadding
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyGameShelfTheme {
        Greeting("Android")
    }
}