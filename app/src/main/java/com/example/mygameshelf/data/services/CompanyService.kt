package com.example.mygameshelf.data.services

import com.example.mygameshelf.domain.dtos.company.CompanyDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path


interface CompanyService {

    @GET("api/companies")
    suspend fun getCompanies(): List<CompanyDto>

    @GET("api/companies/{id}")
    suspend fun getCompanyById(
        @Path("id") id: Int
    ): CompanyDto
}