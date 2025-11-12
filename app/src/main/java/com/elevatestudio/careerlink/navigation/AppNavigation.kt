// Lokasi: navigation/AppNavigation.kt
package com.elevatestudio.careerlink.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elevatestudio.careerlink.ui.screen.OnboardingScreen
import com.elevatestudio.careerlink.ui.screen.SplashScreen
import com.elevatestudio.careerlink.ui.screen.auth.ForgotPasswordScreen
import com.elevatestudio.careerlink.ui.screen.auth.SignInScreen
import com.elevatestudio.careerlink.ui.screen.auth.SignUpScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.BoothDetailScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.CareerFairScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.CheckInScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.EventDetailScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.EventMapScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.NetworkingScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.NotificationScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.AjukanLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.DaftarLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.DetailLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.NotifikasiScreen

// Definisikan rute-rute layarnya biar gak salah ketik
object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val SIGN_IN = "signin"
    const val SIGN_UP = "signup"
    const val FORGOT_PASSWORD = "forgot_password"
    const val CAREER_FAIR = "career_fair"
    const val EVENT_DETAIL = "eventDetail/{eventTitle}"
    const val EVENT_MAP = "eventMap"
    const val BOOTH_DETAIL = "boothDetail/{boothId}"
    const val CHECK_IN = "checkIn"
    const val NETWORKING = "networking"
    const val NOTIFICATION = "notification"


    // --- RUTE BARU UNTUK MODUL LOWONGAN ---
    const val DAFTAR_LOWONGAN = "daftar_lowongan"
    const val NOTIFIKASI = "notifikasi_lowongan" // Ganti nama agar tidak konflik
    // Ini butuh argumen (ID lowongan)
    const val DETAIL_LOWONGAN = "detail_lowongan/{lowonganId}"
    // Ini juga butuh argumen (ID lowongan)
    const val AJUKAN_LOWONGAN = "ajukan_lowongan/{lowonganId}"

    // Helper function biar gampang pindah ke detail/ajuan
    fun detailLowongan(lowonganId: String) = "detail_lowongan/$lowonganId"
    fun ajukanLowongan(lowonganId: String) = "ajukan_lowongan/$lowonganId"

}

@Composable
fun AppNavigation() {
    // Controller buat ngatur navigasi
    val navController = rememberNavController()

    // NavHost ini yang nampung semua layar
    NavHost(
        navController = navController,
        startDestination = Routes.CAREER_FAIR // Mulai dari Career Fair
    ) {

        // --- GRUP OTENTIKASI ---

        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onNavigateToSignIn = {
                    navController.navigate(Routes.SIGN_IN) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Routes.SIGN_UP) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SIGN_IN) {
            SignInScreen(
                onNavigateToSignUp = { navController.navigate(Routes.SIGN_UP) },
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
                onSignInClicked = { email, password ->
                    // Nanti di sini logikanya
                    // UNTUK SEKARANG, kita anggap login sukses & lempar ke daftar lowongan
                    navController.navigate(Routes.CAREER_FAIR) {
                        popUpTo(Routes.SIGN_IN) { inclusive = true } // Hapus layar login
                    }
                }
            )
        }

        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onNavigateToSignIn = {
                    navController.navigate(Routes.SIGN_IN) {
                        popUpTo(Routes.SIGN_IN) { inclusive = true }
                    }
                },
                onSignUpClicked = { email, password, confirmPassword ->
                    // Nanti di sini logikanya
                    // Kalo sukses, balik ke Sign In
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onSavePasswordClicked = { email, newPass, confirmPass ->
                    // Nanti di sini logikanya
                    // Kalo sukses, balik ke Sign In
                    navController.navigate(Routes.SIGN_IN) {
                        popUpTo(Routes.SIGN_IN) { inclusive = true }
                    }
                }
            )
        }

        // --- GRUP CAREER FAIR ---
        composable(Routes.CAREER_FAIR) {
            CareerFairScreen(navController)
        }

        // DETAIL EVENT
        composable(
            route = Routes.EVENT_DETAIL,
            arguments = listOf(navArgument("eventTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedTitle = backStackEntry.arguments?.getString("eventTitle") ?: ""
            val decodedTitle = Uri.decode(encodedTitle)
            EventDetailScreen(navController, decodedTitle)
        }

        // EVENT MAP
        composable(Routes.EVENT_MAP) { EventMapScreen(navController) }

        // BOOTH DETAIL
        composable(
            route = Routes.BOOTH_DETAIL,
            arguments = listOf(navArgument("boothId") { type = NavType.StringType })
        ) { backStackEntry ->
            val boothId = backStackEntry.arguments?.getString("boothId")?.toIntOrNull() ?: 0
            BoothDetailScreen(navController, boothId)
        }

        // OTHER SCREENS
        composable(Routes.CHECK_IN) { CheckInScreen(navController) }
        composable(Routes.NETWORKING) { NetworkingScreen(navController) }
        composable(Routes.NOTIFICATION) { NotificationScreen(navController) }


        // --- GRUP MODUL LOWONGAN ---

        composable(Routes.DAFTAR_LOWONGAN) {
            DaftarLowonganScreen(
                onLowonganClick = { lowonganId ->
                    // Pindah ke Detail bawa ID
                    navController.navigate(Routes.detailLowongan(lowonganId))
                },
                onNavigate = { route ->
                    // TODO: Handle navigasi navbar
                    if (route == "lowongan") {
                        // Udah di sini, gak usah ngapain-ngapain
                    } else if(route == "home") {
                         navController.navigate(Routes.CAREER_FAIR)
                    }
                }
            )
        }

        composable(
            route = Routes.DETAIL_LOWONGAN,
            arguments = listOf(navArgument("lowonganId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Ambil ID dari argumen
            val lowonganId = backStackEntry.arguments?.getString("lowonganId") ?: ""
            DetailLowonganScreen(
                lowonganId = lowonganId,
                onDaftarClick = { id ->
                    // Pindah ke Ajukan Lowongan bawa ID
                    navController.navigate(Routes.ajukanLowongan(id))
                },
                onBackClick = { navController.popBackStack() } // Tombol back
            )
        }

        composable(
            route = Routes.AJUKAN_LOWONGAN,
            arguments = listOf(navArgument("lowonganId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lowonganId = backStackEntry.arguments?.getString("lowonganId") ?: ""
            AjukanLowonganScreen(
                lowonganId = lowonganId,
                onBackClick = { navController.popBackStack() }, // Tombol back
                onGoToHome = {
                    // Kalo sukses submit, balik ke layar daftar lowongan
                    navController.navigate(Routes.DAFTAR_LOWONGAN) {
                        // Hapus semua layar di atasnya (detail, ajukan)
                        popUpTo(Routes.DAFTAR_LOWONGAN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.NOTIFIKASI) {
            NotifikasiScreen(
                onBackClick = { navController.popBackStack() },
                onLihatClick = { notifId ->
                    // TODO: Tentukan mau ke mana kalo notif di-klik
                    // Cth: navController.navigate(Routes.detailLowongan(notifId))
                }
            )
        }
    }
}
