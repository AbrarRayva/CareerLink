package com.elevatestudio.careerlink.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elevatestudio.careerlink.ui.screen.OnboardingScreen
import com.elevatestudio.careerlink.ui.screen.SplashScreen
import com.elevatestudio.careerlink.ui.screen.auth.ForgotPasswordScreen
import com.elevatestudio.careerlink.ui.screen.auth.SignInScreen
import com.elevatestudio.careerlink.ui.screen.auth.SignUpScreen

import com.elevatestudio.careerlink.ui.screen.careerfair.BoothDetailScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.CareerFairScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.CheckInScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.EventDetailScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.BoothMapScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.EventMapScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.NetworkingScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.NotificationScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.SavedEventsScreen
import com.elevatestudio.careerlink.ui.screen.careerfair.CareerFairViewModel


import com.elevatestudio.careerlink.ui.screen.lowongan.AjukanLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.DaftarLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.DetailLowonganScreen
import com.elevatestudio.careerlink.ui.screen.lowongan.NotifikasiScreen

import com.elevatestudio.careerlink.ui.screen.mentoring.BookingMentoringScreen
import com.elevatestudio.careerlink.ui.screen.mentoring.CatatanMentoringScreen
import com.elevatestudio.careerlink.ui.screen.mentoring.DetailMentoringScreen
import com.elevatestudio.careerlink.ui.screen.mentoring.JadwalMentoringScreen

import com.elevatestudio.careerlink.ui.screen.kursus.BadgeScanScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DaftarKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DashboardKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.DetailKursusScreen
import com.elevatestudio.careerlink.ui.screen.kursus.RegistrationSuccessScreen


object Routes {

    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val SIGN_IN = "signin"
    const val SIGN_UP = "signup"
    const val FORGOT_PASSWORD = "forgot_password"


    const val CAREER_FAIR = "career_fair"
    const val EVENT_DETAIL = "eventDetail/{eventTitle}"
    const val BOOTH_MAP = "boothMap"
    const val EVENT_MAP = "eventMap"
    const val BOOTH_DETAIL = "boothDetail/{boothId}"
    const val CHECK_IN = "checkIn"
    const val NETWORKING = "networking"
    const val SAVED_EVENTS = "savedEvents"
    const val NOTIFICATION = "notification"


    const val DAFTAR_LOWONGAN = "daftar_lowongan"
    const val NOTIFIKASI_LOWONGAN = "notifikasi_lowongan"
    const val DETAIL_LOWONGAN = "detail_lowongan/{lowonganId}"
    const val AJUKAN_LOWONGAN = "ajukan_lowongan/{lowonganId}"
    fun detailLowongan(lowonganId: String) = "detail_lowongan/$lowonganId"
    fun ajukanLowongan(lowonganId: String) = "ajukan_lowongan/$lowonganId"


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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val careerFairViewModel: CareerFairViewModel = viewModel()


    val slideIn = slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it })
    val slideOut = slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it })
    val popIn = slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -it })
    val popOut = slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { it })
    val fadeIn = fadeIn(animationSpec = tween(300))
    val fadeOut = fadeOut(animationSpec = tween(300))


    NavHost(
        navController = navController,
        startDestination = Routes.DAFTAR_KURSUS,

        enterTransition = { slideIn },
        exitTransition = { slideOut },
        popEnterTransition = { popIn },
        popExitTransition = { popOut }
    ) {


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

                    navController.navigate(Routes.JADWAL_MENTORING) {
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


        composable(Routes.CAREER_FAIR) {
            CareerFairScreen(navController)
        }

        composable(
            route = Routes.EVENT_DETAIL,
            arguments = listOf(
                navArgument("eventTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val title = Uri.decode(
                backStackEntry.arguments?.getString("eventTitle") ?: ""
            )

            EventDetailScreen(
                navController = navController,
                eventTitle = title,
                viewModel = careerFairViewModel
            )
        }

        composable(Routes.EVENT_MAP) {
            EventMapScreen(navController)
        }

        composable(
            route = Routes.BOOTH_MAP,
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = "booth"
                },
                navArgument("boothId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->

            val mode = backStackEntry.arguments?.getString("mode")
            val boothId = backStackEntry.arguments?.getInt("boothId")

            BoothMapScreen(
                navController = navController,
                mode = mode,
                boothId = boothId
            )
        }

        composable(
            route = Routes.BOOTH_DETAIL,
            arguments = listOf(
                navArgument("boothId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val boothId = backStackEntry.arguments
                ?.getString("boothId")
                ?.toIntOrNull() ?: 0

            BoothDetailScreen(navController, boothId)
        }

        composable(Routes.CHECK_IN) {
            CheckInScreen(
                navController = navController,
                viewModel = careerFairViewModel
            )
        }

        composable(Routes.NETWORKING) {
            NetworkingScreen(navController)
        }

        composable(Routes.NOTIFICATION) {
            NotificationScreen(navController)
        }

        composable(Routes.SAVED_EVENTS) {
            SavedEventsScreen(
                navController = navController,
                viewModel = careerFairViewModel
            )
        }



        composable(Routes.DAFTAR_LOWONGAN) {
            DaftarLowonganScreen(
                onLowonganClick = { lowonganId ->
                    navController.navigate(Routes.detailLowongan(lowonganId))
                },
                onNavigate = { route ->
                    when (route) {
                        "event" -> navController.navigate(Routes.CAREER_FAIR) {
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

        composable(Routes.NOTIFIKASI_LOWONGAN) {
            NotifikasiScreen(
                onBackClick = { navController.popBackStack() },
                onLihatClick = { notifId ->
                    // TODO: Tentukan mau ke mana kalo notif di-klik
                }
            )
        }


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
            enterTransition = { fadeIn(tween(500)) }
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


        composable(Routes.JADWAL_MENTORING) {
            JadwalMentoringScreen(navController)
        }


        composable(
            route = Routes.DETAIL_MENTORING,
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId")
            DetailMentoringScreen(navController, sessionId)
        }


        composable(
            route = Routes.BOOKING_MENTORING,
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) {
            BookingMentoringScreen(navController)}


        composable(Routes.CATATAN_MENTORING) {
            CatatanMentoringScreen(navController)
            }
        }
    }

