package com.example.mygameshelf.data.services

import com.example.mygameshelf.domain.dtos.AuthResponse
import com.example.mygameshelf.domain.dtos.Login
import com.example.mygameshelf.domain.dtos.Register
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface AuthService {


    @POST("auth/register")
    suspend fun register(@Body register: Register) : AuthResponse

    @POST("auth/login")
    suspend fun login(@Body login: Login) : AuthResponse


}