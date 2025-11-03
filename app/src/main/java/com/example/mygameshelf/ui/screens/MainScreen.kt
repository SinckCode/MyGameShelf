package com.example.mygameshelf.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mygameshelf.ui.screens.HomeScreen.HomeScreen

@Composable
fun MainScren(
    contentPadding: PaddingValues){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeScreenRoute
    ){

        composable<HomeScreenRoute> {
            HomeScreen()
        }

    }
}