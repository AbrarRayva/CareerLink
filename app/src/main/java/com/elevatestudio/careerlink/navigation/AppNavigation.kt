package com.elevatestudio.careerlink.navigation

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
import com.elevatestudio.careerlink.ui.mentoring.BookingMentoringScreen
import com.elevatestudio.careerlink.ui.mentoring.CatatanMentoringScreen
import com.elevatestudio.careerlink.ui.mentoring.DetailMentoringScreen
import com.elevatestudio.careerlink.ui.mentoring.JadwalMentoringScreen

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val SIGN_IN = "signin"
    const val SIGN_UP = "signup"
    const val FORGOT_PASSWORD = "forgot_password"
    // const val HOME = "home"
}

@Composable
fun AppNavigation() {
    // Controller buat ngatur navigasi
    val navController = rememberNavController()

    // NavHost ini yang nampung semua layar
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {


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
                    // ...
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
                    // ...
                }
            )
        }

        // Layar Lupa Password
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

    }
}
@Composable
fun CareerLinkNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "jadwal_mentoring") {
        // 1. Daftar Jadwal
        composable("jadwal_mentoring") {
            JadwalMentoringScreen(navController)
        }

        // 2. Detail Jadwal
        composable(
            route = "detail_mentoring/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId")
            DetailMentoringScreen(navController, sessionId)
        }

        // 3. Booking
        composable(
            route = "booking_mentoring/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) {
            BookingMentoringScreen(navController)
        }

        // 4. Catatan
        composable("catatan_mentoring") {
            CatatanMentoringScreen(navController)
        }

        // TODO: Tambahkan NotifikasiScreen dan rute lainnya jika diperlukan
    }
}