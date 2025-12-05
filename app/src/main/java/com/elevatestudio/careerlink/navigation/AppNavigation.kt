// Lokasi: navigation/AppNavigation.kt
package com.elevatestudio.careerlink.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation // Penting untuk nested navigation
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

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val SIGN_IN = "signin"
    const val SIGN_UP = "signup"
    const val FORGOT_PASSWORD = "forgot_password"

    // Nama rute untuk GRUP Career Fair
    const val CAREER_FAIR = "career_fair"

    // --- RUTE MODUL LOWONGAN ---
    const val DAFTAR_LOWONGAN = "daftar_lowongan"
    const val NOTIFIKASI = "notifikasi"
    const val DETAIL_LOWONGAN = "detail_lowongan/{lowonganId}"
    const val AJUKAN_LOWONGAN = "ajukan_lowongan/{lowonganId}"

    fun detailLowongan(lowonganId: String) = "detail_lowongan/$lowonganId"
    fun ajukanLowongan(lowonganId: String) = "ajukan_lowongan/$lowonganId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // Catatan: Jika ingin mulai dari Splash, ganti ini ke Routes.SPLASH
        startDestination = Routes.CAREER_FAIR
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
                    navController.navigate(Routes.DAFTAR_LOWONGAN) {
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

        // --- CAREER FAIR & LOWONGAN NAVIGATION ---
        // Panggil extension function NavGraph di sini langsung
        // (Jangan dibungkus composable lagi)
        NavGraph(navController = navController)
    }
}

// Ubah fungsi ini menjadi extension dari NavGraphBuilder
// Hapus @Composable dan hapus NavHost di dalamnya
fun NavGraphBuilder.NavGraph(navController: NavHostController) {

    // Gunakan navigation() untuk membuat nested graph
    navigation(
        route = Routes.CAREER_FAIR, // Nama grup ("career_fair")
        startDestination = "careerFairHome" // Rute awal di dalam grup ini
    ) {

        // HOME SCREEN (Ubah nama rutenya jadi unik, misal "careerFairHome")
        composable("careerFairHome") {
            CareerFairScreen(navController)
        }

        // DETAIL EVENT
        composable(
            route = "eventDetail/{eventTitle}",
            arguments = listOf(navArgument("eventTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedTitle = backStackEntry.arguments?.getString("eventTitle") ?: ""
            val decodedTitle = Uri.decode(encodedTitle)
            EventDetailScreen(navController, decodedTitle)
        }

        // EVENT MAP (TANPA PARAM)
        composable("eventMap") {
            EventMapScreen(
                navController = navController,
                mode = "event",
                eventId = null
            )
        }

        // EVENT MAP (DENGAN PARAM)
        composable(
            route = "eventMap?mode={mode}&eventId={eventId}",
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = "event"
                },
                navArgument("eventId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode")
            val eventId = backStackEntry.arguments?.getString("eventId")?.toIntOrNull()

            EventMapScreen(
                navController = navController,
                mode = mode,
                eventId = eventId
            )
        }

        // BOOTH DETAIL
        composable(
            route = "boothDetail/{boothId}",
            arguments = listOf(navArgument("boothId") { type = NavType.StringType })
        ) { backStackEntry ->
            val boothId = backStackEntry.arguments?.getString("boothId")?.toIntOrNull() ?: 0
            BoothDetailScreen(navController, boothId)
        }

        // OTHER SCREENS
        composable("checkIn") { CheckInScreen(navController) }
        composable("networking") { NetworkingScreen(navController) }
        composable("notification") { NotificationScreen(navController) }

        // --- GRUP MODUL LOWONGAN ---

        composable(Routes.DAFTAR_LOWONGAN) {
            DaftarLowonganScreen(
                onLowonganClick = { lowonganId ->
                    navController.navigate(Routes.detailLowongan(lowonganId))
                },
                onNavigate = { route ->
                    if (route == "lowongan") {
                        // Do nothing
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

        composable(Routes.NOTIFIKASI) {
            NotifikasiScreen(
                onBackClick = { navController.popBackStack() },
                onLihatClick = { }
            )
        }
    }
}