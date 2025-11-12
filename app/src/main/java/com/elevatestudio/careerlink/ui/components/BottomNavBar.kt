
package com.elevatestudio.careerlink.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color // <-- IMPORT WARNA
import androidx.compose.ui.graphics.vector.ImageVector
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

// Data class buat item di navbar
data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

val navItems = listOf(
    NavItem("Event", Icons.Filled.Event, Icons.Outlined.Event, "event"),
    NavItem("Kursus", Icons.Filled.Book, Icons.Outlined.Book, "kursus"),
    NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, "home"),
    NavItem("Mentor", Icons.Filled.Assignment, Icons.Outlined.Assignment, "mentor"),
    NavItem("Lowongan", Icons.Filled.Work, Icons.Outlined.Work, "lowongan")
)

@Composable
fun AppBottomNavBar(
    currentRoute: String, // Kita butuh ini biar tau lagi di layar mana
    onItemSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White // Ganti warna sesuai desain
    ) {
        navItems.forEach { item ->
            val isSelected = item.route == currentRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryGreen,
                    selectedTextColor = PrimaryGreen,
                    indicatorColor = PrimaryGreen.copy(alpha = 0.1f),
                    unselectedIconColor = SecondaryGreen,
                    unselectedTextColor = SecondaryGreen
                )
            )
        }
    }
}