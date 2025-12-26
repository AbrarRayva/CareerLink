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
// --- IMPORT MODUL CAREER FAIR ---


// --- IMPORT MODUL LOWONGAN ---
import com.elevatestudio.careerlink.ui.screen.lowongan.AjukanLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.DaftarLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.DetailLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.NotifikasiScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.DetailLamaranScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.RiwayatLamaranScreen

// --- IMPORT MODUL MENTORING ---
import com.elevatestudio.careerlink.ui.screen.mentoring.BookingMentoringScreen
import com.elevatestudio.careerlink.ui.screen.mentoring.CatatanMentoringScreen
import com.elevatestudio.careerlink.ui.screen.mentoring.DetailMentoringScreen
import com.elevatestudio.careerlink.ui.screen.mentoring.JadwalMentoringScreen

// --- IMPORT MODUL KURSUS ---
import com.elevatestudio.careerlink.ui.screen.kursus.DashboardKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DaftarKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DetailKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.BadgeScanScreen
import com.elevatestudio.careerlink.ui.screen.kursus.RegistrationSuccessScreen
import com.elevatestudio.careerlink.ui.screen.kursus.StatusKursusScreen

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
    const val DAFTAR_LOWONGAN = "daftar_lowongan"
    const val DETAIL_LOWONGAN = "detail_lowongan/{lowonganId}"
    const val AJUKAN_LOWONGAN = "ajukan_lowongan/{lowonganId}"
    const val RIWAYAT_LAMARAN = "riwayat_lamaran"
    const val DETAIL_LAMARAN = "detail_lamaran/{applicationId}"
    const val NOTIFIKASI_LOWONGAN = "notifikasi_lowongan"
    fun detailLowongan(lowonganId: String) = "detail_lowongan/$lowonganId"
    fun ajukanLowongan(lowonganId: String) = "ajukan_lowongan/$lowonganId"
    fun detailLamaran(id: String) = "detail_lamaran/$id"

   
    const val KURSUS_DASHBOARD = "kursus_dashboard"
    const val DAFTAR_KURSUS = "daftar_kursus"
    const val REGISTRATION_SUCCESS = "registration_success"
    const val BADGE_SCAN = "badge_scan"
    const val DETAIL_KURSUS = "detail_kursus/{kursusId}"
    const val STATUS_KURSUS = "status_kursus/{type}"
    fun detailKursus(kursusId: String) = "detail_kursus/$kursusId"

    const val JADWAL_MENTORING = "jadwal_mentoring"
    const val DETAIL_MENTORING = "detail_mentoring/{sessionId}"
    const val BOOKING_MENTORING = "booking_mentoring/{sessionId}"
    const val CATATAN_MENTORING = "catatan_mentoring/{sessionId}"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(startJobId: String? = null) {
    val navController = rememberNavController()

    val slideIn = slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it })
    val slideOut = slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it })
    val popIn = slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -it })
    val popOut = slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { it })
    val fadeIn = fadeIn(animationSpec = tween(300))
    val fadeOut = fadeOut(animationSpec = tween(300))

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = { slideIn },
        exitTransition = { slideOut },
        popEnterTransition = { popIn },
        popExitTransition = { popOut }
    ) {

        composable(Routes.SPLASH, enterTransition = { fadeIn }, exitTransition = { fadeOut }) {
            SplashScreen(onSplashFinished = {
                if (startJobId != null) {
                    navController.navigate(Routes.DAFTAR_LOWONGAN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                    navController.navigate(Routes.detailLowongan(startJobId))
                } else {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            })
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onNavigateToSignIn = { navController.navigate(Routes.SIGN_IN) },
                onNavigateToSignUp = { navController.navigate(Routes.SIGN_UP) }
            )
        }
        composable(Routes.SIGN_IN) {
            SignInScreen(
                onNavigateToSignUp = { navController.navigate(Routes.SIGN_UP) },
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
                onSignInSuccess = {
                    navController.navigate(Routes.KURSUS_DASHBOARD) {
                        popUpTo(Routes.SIGN_IN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onNavigateToSignIn = { navController.navigate(Routes.SIGN_IN) },
                onSignUpSuccess = { navController.navigate(Routes.SIGN_IN) }
            )
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(onSavePasswordClicked = { _, _, _ -> navController.navigate(Routes.SIGN_IN) })
        }


        composable("daftar_lowongan") {
            DaftarLowonganScreen(
                onBackClick = { navController.popBackStack() },
                onItemClick = { lowonganId ->
                    navController.navigate("detail_lowongan/$lowonganId")
                }
            )
        }

       
        composable(
            route = "detail_lowongan/{lowonganId}",
            arguments = listOf(navArgument("lowonganId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lowonganId = backStackEntry.arguments?.getString("lowonganId") ?: ""
            DetailLowonganScreen(
                lowonganId = lowonganId,
                onBackClick = { navController.popBackStack() },
               
                onAjukanClick = { id ->
                    navController.navigate("ajukan_lamaran/$id")
                }
            )
        }

        composable(route = Routes.AJUKAN_LOWONGAN, arguments = listOf(navArgument("lowonganId") { type = NavType.StringType })) { bse ->
            val lowonganId = bse.arguments?.getString("lowonganId") ?: ""
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

        composable(Routes.RIWAYAT_LAMARAN) {
            RiwayatLamaranScreen(
                onBackClick = { navController.popBackStack() },
                onDetailClick = { appId ->
                    navController.navigate(Routes.detailLamaran(appId))
                }
            )
        }

        composable(
            route = Routes.DETAIL_LAMARAN,
            arguments = listOf(navArgument("applicationId") { type = NavType.StringType })
        ) { bse ->
            val appId = bse.arguments?.getString("applicationId") ?: ""
            DetailLamaranScreen(
                applicationId = appId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.NOTIFIKASI_LOWONGAN) {
            NotifikasiScreen(
                onBackClick = { navController.popBackStack() },
            )
        }

       
       
       

        composable(Routes.KURSUS_DASHBOARD) {
            DashboardKursusScreen(
                onNavigate = { route ->
                    when (route) {
                        "home" -> navController.navigate(Routes.CAREER_FAIR)
                        "lowongan" -> navController.navigate(Routes.DAFTAR_LOWONGAN)
                        "mentor" -> navController.navigate(Routes.JADWAL_MENTORING)
                        "kursus" -> { /* Stay here */ }

                       
                        "status_active" -> navController.navigate("status_kursus/Active")
                        "status_completed" -> navController.navigate("status_kursus/Completed")
                    }
                },
                onNavigateToDaftarKursus = { navController.navigate(Routes.DAFTAR_KURSUS) },
                onNavigateToDetailKursus = { id -> navController.navigate(Routes.detailKursus(id)) },
                onNavigateToBadgeScan = { navController.navigate(Routes.BADGE_SCAN) }
            )
        }

        composable(Routes.DAFTAR_KURSUS) {
            DaftarKursusScreen(
                onBackClick = { navController.popBackStack() },
                onKursusClick = { id -> navController.navigate(Routes.detailKursus(id)) }
            )
        }

        composable(
            route = Routes.DETAIL_KURSUS,
            arguments = listOf(navArgument("kursusId") { type = NavType.StringType })
        ) { bse ->
            val id = bse.arguments?.getString("kursusId") ?: ""
            DetailKursusScreen(
                kursusId = id,
                onBackClick = { navController.popBackStack() },
                onDaftarSuccess = {
                    navController.navigate(Routes.REGISTRATION_SUCCESS) {
                        popUpTo(Routes.DETAIL_KURSUS) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTRATION_SUCCESS) {
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

       
        composable(
            route = "status_kursus/{type}",
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "Active"
            StatusKursusScreen(
                statusType = type,
                onBackClick = { navController.popBackStack() },
                onDetailClick = { id -> navController.navigate(Routes.detailKursus(id)) }
            )
        }

       
       
       
        composable(Routes.JADWAL_MENTORING) {
            JadwalMentoringScreen(navController)
        }
        composable(route = Routes.DETAIL_MENTORING, arguments = listOf(navArgument("sessionId") { type = NavType.StringType })) { bse ->
            DetailMentoringScreen(navController, bse.arguments?.getString("sessionId"))
        }
        composable(route = Routes.BOOKING_MENTORING, arguments = listOf(navArgument("sessionId") { type = NavType.StringType })) {
            BookingMentoringScreen(navController)
        }
        composable(Routes.CATATAN_MENTORING) {
            CatatanMentoringScreen(navController)
        }
    }
}