package pumpspick.hobbystore.reserve.ui.composable.screen.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import pumpspick.hobbystore.reserve.R
import pumpspick.hobbystore.reserve.ui.theme.BodyFamily
import pumpspick.hobbystore.reserve.ui.theme.HeadingFamily
import pumpspick.hobbystore.reserve.ui.theme.Muted
import pumpspick.hobbystore.reserve.ui.theme.OnSurface
import pumpspick.hobbystore.reserve.ui.theme.Surface
import pumpspick.hobbystore.reserve.ui.viewmodel.FECTBSplashVM

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: FECTBSplashVM = koinViewModel()
) {
    val isOnboardingCompleted by viewModel.onboardedState.collectAsStateWithLifecycle()

    var slideUp by remember { mutableStateOf(false) }
    val offsetY by animateFloatAsState(
        targetValue = if (slideUp) 0f else 40f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "slide"
    )
    val alpha by animateFloatAsState(
        targetValue = if (slideUp) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        slideUp = true
        delay(1500)
        if (isOnboardingCompleted == true) onNavigateToHome()
        else onNavigateToOnboarding()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top 55% — brand image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=1200",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0x88000000))
                        )
                    )
            )
        }

        // Bottom 45% — white surface with icon + name
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f)
                .background(Surface),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .offset(y = offsetY.dp)
                    .alpha(alpha),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )
                Text(
                    text = "Pumps Pick",
                    fontFamily = HeadingFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = OnSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Premium Leisure & Hobby Store",
                    fontFamily = BodyFamily,
                    fontSize = 13.sp,
                    color = Muted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
