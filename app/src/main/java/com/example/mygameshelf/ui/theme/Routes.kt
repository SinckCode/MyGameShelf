package com.example.mygameshelf.ui.theme

import kotlinx.serialization.Serializable

@Serializable
object LoginScreenRoute

@Serializable
object RegisterScreenRoute

@Serializable
object HomeScreenRoute

@Serializable
object MainScreenRoute

@Serializable
object MainScreenGraph

@Serializable
object SearchScreenRoute

@Serializable
object ListViewRoute


@Serializable
object FavoritesScreenRoute

@Serializable
data class DetailGameRoute(val gameId: Int)

@Serializable
data class DetailCompanyRoute(val companyId: Int)

@Serializable
object CreateListRoute

@Serializable
object ListDetailRoute

@Serializable
object AddListRoute

@Serializable
object UserViewRoute
