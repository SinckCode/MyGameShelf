package com.example.mygameshelf.ui.screens.HomeScreen.Detail.CompanyDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.mygameshelf.ui.screens.HomeScreen.Detail.DetailGame
import com.example.mygameshelf.ui.screens.HomeScreen.components.Header
import com.example.mygameshelf.ui.screens.HomeScreen.components.MyBottomBar
import com.example.mygameshelf.ui.viewmodels.CompaniesViewModel

@Composable
fun DetailCompany(
    companyId: Int?,
    navController: NavHostController,
    companiesViewModel: CompaniesViewModel = viewModel()
) {
    val companiesState by companiesViewModel.uiState.collectAsState()

    val company = companiesState.companies.find { it.id == companyId }

    Scaffold(
        bottomBar = {
            MyBottomBar(navController = navController)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 20.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MyGameShelf",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            // ✅ BODY
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 12.dp)
            ) {

                Text(
                    text = company?.nombre ?: "Cargando...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color.LightGray)
                ) {
                    company?.let {
                        AsyncImage(
                            model = it.imagenURL,
                            contentDescription = it.nombre,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Text("Fundacion: ${company?.fundacion}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 3.dp)
                    )

                    Text("Historia:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(top = 1.dp, bottom = 3.dp)
                    )

                    Text(
                        text = company?.historia ?: "",
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 3.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailCompanyPreview() {
    DetailCompany(5,
        navController = rememberNavController())
}

