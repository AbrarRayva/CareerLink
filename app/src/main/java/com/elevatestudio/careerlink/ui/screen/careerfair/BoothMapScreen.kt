package com.elevatestudio.careerlink.ui.screen.careerfair

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.navigation.Routes
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen


data class BoothMapItem(
    val id: Int,
    val name: String,
    val row: Int,
    val column: Int
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoothMapScreen(
    navController: NavController,
    mode: String?,
    boothId: Int?
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Peta Booth Event",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen
                )
            )
        }
    ) { padding ->

        BoothMapContent(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            navController = navController
        )
    }
}


@Composable
fun BoothMapContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val booths = listOf(
        BoothMapItem(1, "PT Telkom", 0, 0),
        BoothMapItem(2, "Bank BRI", 0, 1),
        BoothMapItem(3, "Shopee", 0, 2),
        BoothMapItem(4, "Traveloka", 1, 0),
        BoothMapItem(5, "Tokopedia", 1, 1),
        BoothMapItem(6, "Bukalapak", 1, 2)
    )

    val boothWidth = 150.dp
    val boothHeight = 100.dp
    val spacing = 40.dp

    Box(
        modifier = modifier
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Box(
            modifier = Modifier
                .width((boothWidth + spacing) * 3)
                .height((boothHeight + spacing) * 3)
        ) {

            booths.forEach { booth ->
                BoothMarker(
                    booth = booth,
                    boothWidth = boothWidth,
                    boothHeight = boothHeight,
                    spacing = spacing,
                    onDetailClick = {
                        navController.navigate("boothDetail/${booth.id}")
                    },
                    onScanClick = {
                        navController.navigate(Routes.CHECK_IN)
                    }
                )
            }
        }
    }
}

/* =======================================================
   BOOTH ITEM
   ======================================================= */

@Composable
fun BoothMarker(
    booth: BoothMapItem,
    boothWidth:  Dp,
    boothHeight: Dp,
    spacing: Dp,
    onDetailClick: () -> Unit,
    onScanClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .offset(
                x = (boothWidth + spacing) * booth.column,
                y = (boothHeight + spacing) * booth.row
            )
            .size(boothWidth, boothHeight),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryGreen)
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = booth.name,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Detail",
                    color = Color.White,
                    modifier = Modifier.clickable { onDetailClick() }
                )

                IconButton(
                    onClick = onScanClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Filled.QrCodeScanner,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}
