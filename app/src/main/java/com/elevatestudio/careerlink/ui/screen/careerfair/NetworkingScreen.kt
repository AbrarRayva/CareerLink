package com.elevatestudio.careerlink.ui.screen.careerfair

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

/* ================================
   DATA MODEL
   ================================ */
data class NetworkContact(
    val name: String,
    val role: String,
    val company: String,
    val email: String,
    val phone: String
)

/* ================================
   SCREEN
   ================================ */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkingScreen(navController: NavController) {

    val contacts = listOf(
        NetworkContact(
            "Andi Pratama",
            "HR Recruiter",
            "PT TechnoNusa",
            "andi@technonusa.co.id",
            "+62 812-3456-7890"
        ),
        NetworkContact(
            "Salsa Putri",
            "UI/UX Designer",
            "Startup Kreatif",
            "salsa@kreatif.id",
            "+62 813-2222-3344"
        ),
        NetworkContact(
            "Rizky Maulana",
            "Backend Engineer",
            "Fintech.id",
            "rizky@fintech.id",
            "+62 851-7788-9900"
        )
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Networking", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(contacts) { contact ->
                NetworkingCard(contact)
            }
        }
    }
}

/* ================================
   CARD ITEM
   ================================ */
@Composable
fun NetworkingCard(contact: NetworkContact) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            SecondaryGreen.copy(alpha = 0.80f)
                        )
                    )
                )
                .padding(16.dp)
        ) {

            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {


                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(PrimaryGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            contact.name,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                        Text(
                            contact.role,
                            color = Color.DarkGray
                        )
                        Text(
                            contact.company,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Divider(color = Color.LightGray)

                Spacer(Modifier.height(10.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(contact.email, color = Color.DarkGray)
                }

                Spacer(Modifier.height(6.dp))

                // Phone
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(contact.phone, color = Color.DarkGray)
                }
            }
        }
    }
}
