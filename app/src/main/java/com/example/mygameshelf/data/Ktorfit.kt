package com.example.mygameshelf.data.services

import com.example.mygameshelf.data.services.AuthService
import com.example.mygameshelf.data.services.createAuthService
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// HTTP Client + Ktorfit para MyGameShelf
object KtorfitClient {

    // 👇 Base URL de tu API
    private const val BASE_URL = "https://mygameshelf.angelonesto.com/"

    val httpClient = HttpClient {
        expectSuccess = false

        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    private val ktorfit: Ktorfit = Ktorfit
        .Builder()
        .baseUrl(BASE_URL)
        .httpClient(httpClient)
        .build()


    fun createAuthService(): AuthService{
        return ktorfit.createAuthService()
    }

    fun createGameService(): GameService{
        return ktorfit.createGameService()
    }

    fun createCompanyService(): CompanyService{
        return ktorfit.createCompanyService()
    }
}
