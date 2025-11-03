// Lokasi: ui/screen/OnboardingScreen.kt
package com.elevatestudio.careerlink.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevatestudio.careerlink.R
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.components.SecondaryButton
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import com.elevatestudio.careerlink.ui.theme.TextBlack
import kotlinx.coroutines.launch

data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

// Ini data buat 3 halaman onboarding
val onboardingPages = listOf(
    OnboardingPage(
        imageRes = R.drawable.img_onboarding_1, // Panggil file img_onboarding_1.xml
        title = "Selamat Datang di CareerLink!",
        description = "Gerbang Anda menuju dunia karir profesional. Temukan peluang, kembangkan diri, dan bangun jaringan, semua dalam satu aplikasi yang terintegrasi untuk mendukung masa depanmu."
    ),
    OnboardingPage(
        imageRes = R.drawable.img_onboarding_2, // Panggil file img_onboarding_2.xml
        title = "Kembangkan Skill Anda",
        description = "Ikuti berbagai kursus dan sertifikasi untuk meningkatkan kompetensi dan daya saing kamu di dunia kerja."
    ),
    OnboardingPage(
        imageRes = R.drawable.img_onboarding_3, // Panggil file img_onboarding_3.xml
        title = "Perluas Jaringan Profesionalmu",
        description = "Jangan lewatkan informasi karir terbaru. Terhubung langsung dengan perusahaan dan para profesional di bidangmu untuk membuka lebih banyak pintu kesempatan."
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    // State buat nginget lagi di halaman ke berapa
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    // Scope ini buat ngejalanin animasi pindah halaman
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Bagian atas (Pager/Geser-geser)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f) // Bikin pager ngisi sisa ruang di atas
        ) { pageIndex ->
            // Manggil composable item buat nampilin data halaman
            OnboardingPageItem(page = onboardingPages[pageIndex])
        }

        // Indikator titik-titik
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = onboardingPages.size,
            activeColor = PrimaryGreen,
            inactiveColor = PrimaryGreen.copy(alpha = 0.3f),
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Bagian bawah (Tombol-tombol)
        // Logika buat ganti tombol di halaman terakhir
        if (pagerState.currentPage == onboardingPages.size - 1) {
            // Ini Halaman Terakhir (halaman ke-2, index-nya)
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                PrimaryButton(text = "Sign In", onClick = onNavigateToSignIn)
                Spacer(modifier = Modifier.height(12.dp))
                SecondaryButton(text = "Sign Up", onClick = onNavigateToSignUp)
            }
        } else {
            // Ini Halaman 1 dan 2
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), // Kasih padding biar rapi
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    text = "Next",
                    onClick = {
                        // Pake 'scope' buat pindah halaman dgn animasi
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SecondaryButton(
                    text = "Skip",
                    onClick = onNavigateToSignIn // Skip langsung ke Sign In
                )
            }
        }
    }
}

// Composable buat nampilin 1 halaman onboarding
@Composable
fun OnboardingPageItem(page: OnboardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(32.dp)) // Kasih jarak
        Text(
            text = page.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextBlack,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp)) // Kasih jarak
        Text(
            text = page.description,
            fontSize = 16.sp,
            color = TextBlack.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}


// --- komponen indikator titik titik ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = PrimaryGreen,
    inactiveColor: Color = SecondaryGreen.copy(alpha = 0.5f),
    indicatorSize: Dp = 8.dp,
    spacing: Dp = 8.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bikin titik-titik sejumlah pageCount
        repeat(pageCount) { iteration ->
            // Cek apakah titik ini yang lagi aktif
            val color = if (pagerState.currentPage == iteration) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .size(indicatorSize)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}