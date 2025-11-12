package com.elevatestudio.careerlink.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

// Daftar Item Navigasi
val navItems = listOf(
    BottomNavItem("mentoring_notes", "Catatan", Icons.AutoMirrored.Filled.List),
    BottomNavItem("mentoring_schedule", "Jadwal", Icons.Filled.CalendarMonth),
    BottomNavItem("home", "Home", Icons.Filled.Home),
    BottomNavItem("learning_module", "Modul", Icons.Filled.Book),
    BottomNavItem("career_jobs", "Karir", Icons.Filled.Work)
)

@Composable
fun CareerLinkBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Warna Hijau Gelap dari Navbar.png (Contoh Hex: #38573E)
    val bottomBarColor = Color(0xFF38573E)
    val selectedItemColor = Color.White
    val unselectedItemColor = Color(0xFFC8C8C8)

    NavigationBar(
        containerColor = bottomBarColor,
        modifier = Modifier.height(60.dp),
    ) {
        navItems.forEach { item ->
            val isSelected = currentRoute?.contains(item.route) == true

            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) selectedItemColor else unselectedItemColor
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (isSelected) selectedItemColor else unselectedItemColor
                    )
                },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}