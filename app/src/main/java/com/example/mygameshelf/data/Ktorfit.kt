package com.pjasoft.recipeapp.data

import com.pjasoft.recipeapp.data.services.AuthService
import com.pjasoft.recipeapp.data.services.createAuthService
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


//HTTP Client
object KtorfitClient{
    val httpClient = HttpClient{
        expectSuccess = false
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    val baseUrl = "https://recipes.pjasoft.com/api/"
    private val ktorfit = Ktorfit
        .Builder()
        .baseUrl(baseUrl)
        .httpClient(httpClient)
        .build()

    fun createAuthService() : AuthService{
        return ktorfit.createAuthService()
    }
}