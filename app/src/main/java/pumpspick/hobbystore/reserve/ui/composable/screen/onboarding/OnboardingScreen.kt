package pumpspick.hobbystore.reserve.ui.composable.screen.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import pumpspick.hobbystore.reserve.ui.theme.*
import pumpspick.hobbystore.reserve.ui.viewmodel.FECTBOnboardingVM

private data class OnboardingSlide(val imageUrl: String, val title: String, val description: String)

private val slides = listOf(
    OnboardingSlide(
        imageUrl = "https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=800",
        title = "Discover Unique Finds",
        description = "Explore our curated collection of hobby kits, artisan gifts, games, and lifestyle goods — all chosen for their quality and originality."
    ),
    OnboardingSlide(
        imageUrl = "https://images.unsplash.com/photo-1513885535751-8b9238bd345a?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8Z2lmdHN8ZW58MHx8MHx8fDA%3D",
        title = "Gifts They'll Love",
        description = "From personalised keepsakes to creative DIY kits, Pumps Pick makes it easy to find meaningful gifts for every occasion and every person."
    ),
    OnboardingSlide(
        imageUrl = "https://images.unsplash.com/photo-1598305762558-328f599df683?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8U2hvcCUyMFdpdGglMjBFYXNlfGVufDB8fDB8fHww",
        title = "Shop With Ease",
        description = "Browse by category, save favourites, and reserve your order in seconds. Collect within 24 hours from our fulfilment centre."
    ),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: FECTBOnboardingVM = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { slides.size })

    Column(modifier = Modifier.fillMaxSize().background(Surface)) {
        // Top text block (semi-transparent)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f)
                .background(Surface.copy(alpha = 0.96f))
        ) {
            Crossfade(targetState = pagerState.currentPage, label = "text") { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = slides[page].title,
                        fontFamily = HeadingFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = OnSurface,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = slides[page].description,
                        fontFamily = BodyFamily,
                        fontSize = 15.sp,
                        color = Muted,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Bottom image (65%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = slides[page].imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Dots + button overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(slides.size) { i ->
                        Box(
                            modifier = Modifier
                                .size(if (pagerState.currentPage == i) 24.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(if (pagerState.currentPage == i) Primary else Color.White.copy(alpha = 0.6f))
                        )
                    }
                }
                if (pagerState.currentPage == slides.size - 1) {
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.setOnboarded()
                                onFinish()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).height(50.dp)
                    ) {
                        Text("Get Started", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    }
                } else {
                    Button(
                        onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).height(50.dp)
                    ) {
                        Text("Next", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
