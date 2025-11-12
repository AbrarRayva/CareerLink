// Lokasi: navigation/AppNavigation.kt
package com.elevatestudio.careerlink.navigation

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
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
// --- IMPORT MODUL CAREER FAIR (DARI TEMANMU) ---
import com.elevatestudio.careerlink.ui.screen.careerfair.BoothDetailScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.CareerFairScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.CheckInScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.EventDetailScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.EventMapScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.NetworkingScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.NotificationScreen
// --- IMPORT MODUL LOWONGAN ---
import com.elevatestudio.careerlink.ui.screen.lowongan.AjukanLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.DaftarLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.DetailLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.NotifikasiScreen
// --- IMPORT MODUL KURSUS (DARI KITA) ---
import com.elevatestudio.careerlink.ui.screen.kursus.BadgeScanScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DaftarKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DashboardKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DetailKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.RegistrationSuccessScreen

// Definisikan rute-rute layarnya biar gak salah ketik
object Routes {
    // --- GRUP AUTH ---
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val SIGN_IN = "signin"
    const val SIGN_UP = "signup"
    const val FORGOT_PASSWORD = "forgot_password"

    // --- GRUP CAREER FAIR (DARI TEMANMU) ---
    // Ini akan jadi "HOME" kita
    const val CAREER_FAIR = "career_fair"
    const val EVENT_DETAIL = "eventDetail/{eventTitle}"
    const val EVENT_MAP = "eventMap"
    const val BOOTH_DETAIL = "boothDetail/{boothId}"
    const val CHECK_IN = "checkIn"
    const val NETWORKING = "networking"
    const val NOTIFICATION = "notification" // Notif Career Fair

    // --- GRUP LOWONGAN ---
    const val DAFTAR_LOWONGAN = "daftar_lowongan"
    const val NOTIFIKASI_LOWONGAN = "notifikasi_lowongan" // Ganti nama agar tidak konflik
    const val DETAIL_LOWONGAN = "detail_lowongan/{lowonganId}"
    const val AJUKAN_LOWONGAN = "ajukan_lowongan/{lowonganId}"
    fun detailLowongan(lowonganId: String) = "detail_lowongan/$lowonganId"
    fun ajukanLowongan(lowonganId: String) = "ajukan_lowongan/$lowonganId"

    // --- GRUP KURSUS (DARI KITA) ---
    const val KURSUS_DASHBOARD = "kursus_dashboard"
    const val DAFTAR_KURSUS = "daftar_kursus"
    const val REGISTRATION_SUCCESS = "registration_success"
    const val BADGE_SCAN = "badge_scan"
    const val DETAIL_KURSUS = "detail_kursus/{kursusId}"
    fun detailKursus(kursusId: String) = "detail_kursus/$kursusId"
}

@OptIn(ExperimentalAnimationApi::class) // <-- Aktifkan Animasi
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // --- FUNGSI ANIMASI ---
    val slideIn = slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it })
    val slideOut = slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it })
    val popIn = slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -it })
    val popOut = slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { it })
    val fadeIn = fadeIn(animationSpec = tween(300))
    val fadeOut = fadeOut(animationSpec = tween(300))
    // --- SELESAI FUNGSI ANIMASI ---

    NavHost(
        navController = navController,
        startDestination = Routes.KURSUS_DASHBOARD, // <-- Dikembalikan ke SPLASH
        // Terapkan animasi default ke SEMUA layar
        enterTransition = { slideIn },
        exitTransition = { slideOut },
        popEnterTransition = { popIn },
        popExitTransition = { popOut }
    ) {

        // --- GRUP OTENTIKASI ---
        composable(
            Routes.SPLASH,
            enterTransition = { fadeIn },
            exitTransition = { fadeOut }
        ) {
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
                    // Login sukses, lempar ke "HOME" (Career Fair)
                    navController.navigate(Routes.CAREER_FAIR) {
                        popUpTo(Routes.SIGN_IN) { inclusive = true }
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
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onSavePasswordClicked = { email, newPass, confirmPass ->
                    navController.navigate(Routes.SIGN_IN) {
                        popUpTo(Routes.SIGN_IN) { inclusive = true }
                    }
                }
            )
        }

        // --- GRUP CAREER FAIR (DARI TEMANMU) ---
        composable(Routes.CAREER_FAIR) {
            // TODO: Update CareerFairScreen punya temanmu
            // 1. Tambah parameter onNavigate: (String) -> Unit
            // 2. Pasang AppBottomNavBar di Scaffold-nya
            // 3. Panggil onNavigate(route) di bottom bar
            CareerFairScreen(navController)
        }

        composable(
            route = Routes.EVENT_DETAIL,
            arguments = listOf(navArgument("eventTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedTitle = backStackEntry.arguments?.getString("eventTitle") ?: ""
            val decodedTitle = Uri.decode(encodedTitle)
            EventDetailScreen(navController, decodedTitle)
        }

        composable(Routes.EVENT_MAP) { EventMapScreen(navController) }

        composable(
            route = Routes.BOOTH_DETAIL,
            arguments = listOf(navArgument("boothId") { type = NavType.StringType })
        ) { backStackEntry ->
            val boothId = backStackEntry.arguments?.getString("boothId")?.toIntOrNull() ?: 0
            BoothDetailScreen(navController, boothId)
        }

        composable(Routes.CHECK_IN) { CheckInScreen(navController) }
        composable(Routes.NETWORKING) { NetworkingScreen(navController) }
        composable(Routes.NOTIFICATION) { NotificationScreen(navController) }


        // --- GRUP MODUL LOWONGAN (GABUNGAN) ---
        composable(Routes.DAFTAR_LOWONGAN) {
            DaftarLowonganScreen(
                onLowonganClick = { lowonganId ->
                    navController.navigate(Routes.detailLowongan(lowonganId))
                },
                onNavigate = { route ->
                    when (route) {
                        "home" -> navController.navigate(Routes.CAREER_FAIR) {
                            popUpTo(Routes.DAFTAR_LOWONGAN) { inclusive = true }
                        }
                        "kursus" -> navController.navigate(Routes.KURSUS_DASHBOARD) {
                            popUpTo(Routes.DAFTAR_LOWONGAN) { inclusive = true }
                        }
                        "lowongan" -> { /* Sudah di sini */ }
                        // TODO: Tambah "event" dan "mentor"
                    }
                }
            )
        }

        composable(
            route = Routes.DETAIL_LOWONGAN,
            arguments = listOf(navArgument("lowonganId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lowonganId = backStackEntry.arguments?.getString("lowonganId") ?: ""
            DetailLowonganScreen(
                lowonganId = lowonganId,
                onDaftarClick = { id ->
                    navController.navigate(Routes.ajukanLowongan(id))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.AJUKAN_LOWONGAN,
            arguments = listOf(navArgument("lowonganId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lowonganId = backStackEntry.arguments?.getString("lowonganId") ?: ""
            AjukanLowonganScreen(
                lowonganId = lowonganId,
                onBackClick = { navController.popBackStack() },
                onGoToHome = {
                    navController.navigate(Routes.DAFTAR_LOWONGAN) {
                        popUpTo(Routes.DAFTAR_LOWONGAN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.NOTIFIKASI_LOWONGAN) { // Pakai nama baru
            NotifikasiScreen(
                onBackClick = { navController.popBackStack() },
                onLihatClick = { notifId ->
                    // TODO: Tentukan mau ke mana kalo notif di-klik
                }
            )
        }

        // --- GRUP MODUL KURSUS (DARI KITA) ---
        composable(Routes.KURSUS_DASHBOARD) {
            DashboardKursusScreen(
                onNavigate = { route ->
                    when (route) {
                        "home" -> navController.navigate(Routes.CAREER_FAIR) {
                            popUpTo(Routes.KURSUS_DASHBOARD) { inclusive = true }
                        }
                        "lowongan" -> navController.navigate(Routes.DAFTAR_LOWONGAN) {
                            popUpTo(Routes.KURSUS_DASHBOARD) { inclusive = true }
                        }
                        "kursus" -> { /* Sudah di sini */ }
                        // TODO: Tambah "event" dan "mentor"
                    }
                },
                onNavigateToDaftarKursus = {
                    navController.navigate(Routes.DAFTAR_KURSUS)
                },
                onNavigateToDetailKursus = { kursusId ->
                    navController.navigate(Routes.detailKursus(kursusId))
                },
                onNavigateToBadgeScan = {
                    navController.navigate(Routes.BADGE_SCAN)
                }
            )
        }

        composable(Routes.DAFTAR_KURSUS) {
            DaftarKursusScreen(
                onBackClick = { navController.popBackStack() },
                onKursusClick = { kursusId ->
                    navController.navigate(Routes.detailKursus(kursusId))
                }
            )
        }

        composable(
            route = Routes.DETAIL_KURSUS,
            arguments = listOf(navArgument("kursusId") { type = NavType.StringType })
        ) { bse ->
            DetailKursusScreen(
                kursusId = bse.arguments?.getString("kursusId") ?: "",
                onBackClick = { navController.popBackStack() },
                onDaftarSuccess = {
                    navController.navigate(Routes.REGISTRATION_SUCCESS) {
                        popUpTo(bse.destination.id) { inclusive = true }
                    }
                }
            )
        }

        composable(
            Routes.REGISTRATION_SUCCESS,
            enterTransition = { fadeIn(tween(500)) } // Transisi khusus
        ) {
            RegistrationSuccessScreen(
                onKembaliClick = {
                    navController.navigate(Routes.KURSUS_DASHBOARD) {
                        popUpTo(Routes.KURSUS_DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.BADGE_SCAN) {
            BadgeScanScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}