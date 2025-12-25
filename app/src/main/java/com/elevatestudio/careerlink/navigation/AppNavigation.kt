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
// --- IMPORT MODUL MENTORING ---
import com.elevatestudio.careerlink.ui.screen.mentoring.BookingMentoringScreen
import com.elevatestudio.careerlink.ui.screen.mentoring.CatatanMentoringScreen
import com.elevatestudio.careerlink.ui.screen.mentoring.DetailMentoringScreen
import com.elevatestudio.careerlink.ui.screen.mentoring.JadwalMentoringScreen
// --- IMPORT MODUL KURSUS ---
import com.elevatestudio.careerlink.ui.screen.kursus.BadgeScanScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DaftarKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DashboardKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DetailKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.RegistrationSuccessScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.DetailLamaranScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.RiwayatLamaranScreen

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

    // Animasi
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
                    navController.navigate(Routes.DAFTAR_LOWONGAN) {
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

        composable(Routes.CAREER_FAIR) { CareerFairScreen(navController) }
        composable(route = Routes.EVENT_DETAIL, arguments = listOf(navArgument("eventTitle") { type = NavType.StringType })) { bse ->
            val decodedTitle = Uri.decode(bse.arguments?.getString("eventTitle") ?: "")
            EventDetailScreen(navController, decodedTitle)
        }
        composable(Routes.EVENT_MAP) { EventMapScreen(navController) }
        composable(route = Routes.BOOTH_DETAIL, arguments = listOf(navArgument("boothId") { type = NavType.StringType })) { bse ->
            val boothId = bse.arguments?.getString("boothId")?.toIntOrNull() ?: 0
            BoothDetailScreen(navController, boothId)
        }
        composable(Routes.CHECK_IN) { CheckInScreen(navController) }
        composable(Routes.NETWORKING) { NetworkingScreen(navController) }
        composable(Routes.NOTIFICATION) { NotificationScreen(navController) }


        composable(Routes.DAFTAR_LOWONGAN) {
            DaftarLowonganScreen(
                onLowonganClick = { lowonganId ->
                    navController.navigate(Routes.detailLowongan(lowonganId))
                },
                onNavigate = { route ->
                    when (route) {
                        "home" -> navController.navigate(Routes.CAREER_FAIR)
                        "event" -> navController.navigate(Routes.CAREER_FAIR)
                        "kursus" -> navController.navigate(Routes.KURSUS_DASHBOARD)
                        "mentor" -> navController.navigate(Routes.JADWAL_MENTORING)
                        "lowongan" -> { /* Stay here */ }
                        "riwayat_lamaran" -> navController.navigate(Routes.RIWAYAT_LAMARAN)
                        "notifikasi" -> navController.navigate(Routes.NOTIFIKASI_LOWONGAN)
                    }
                }
            )
        }

        composable(route = Routes.DETAIL_LOWONGAN, arguments = listOf(navArgument("lowonganId") { type = NavType.StringType })) { bse ->
            val lowonganId = bse.arguments?.getString("lowonganId") ?: ""
            DetailLowonganScreen(
                lowonganId = lowonganId,
                onDaftarClick = { id -> navController.navigate(Routes.ajukanLowongan(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Routes.AJUKAN_LOWONGAN, arguments = listOf(navArgument("lowonganId") { type = NavType.StringType })) { bse ->
            val lowonganId = bse.arguments?.getString("lowonganId") ?: ""
            // Disini ViewModel akan di-init otomatis oleh 'viewModel()' di dalam AjukanLowonganScreen
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
                    navController.navigate(Routes.detailLamaran(appId)) // Pindah ke Detail
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
                onItemClick = { jobId ->
                    navController.navigate(Routes.detailLowongan(jobId))
                }
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

        composable(route = Routes.DETAIL_KURSUS, arguments = listOf(navArgument("kursusId") { type = NavType.StringType })) { bse ->
            DetailKursusScreen(
                kursusId = bse.arguments?.getString("kursusId") ?: "",
                onBackClick = { navController.popBackStack() },
                onDaftarSuccess = {
                    navController.navigate(Routes.REGISTRATION_SUCCESS) { popUpTo(bse.destination.id) { inclusive = true } }
                }
            )
        }

        composable(Routes.REGISTRATION_SUCCESS, enterTransition = { fadeIn(tween(500)) }) {
            RegistrationSuccessScreen(onKembaliClick = { navController.navigate(Routes.KURSUS_DASHBOARD) })
        }

        composable(Routes.BADGE_SCAN) {
            BadgeScanScreen(onBackClick = { navController.popBackStack() })
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