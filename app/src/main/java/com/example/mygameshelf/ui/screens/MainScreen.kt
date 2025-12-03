package com.example.mygameshelf.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mygameshelf.ui.screens.HomeScreen.HomeScreen

@Composable
fun MainScreen(
    navController: NavController,
    contentPadding: PaddingValues
) {
    HomeScreen(
        navController = navController
    )
}
