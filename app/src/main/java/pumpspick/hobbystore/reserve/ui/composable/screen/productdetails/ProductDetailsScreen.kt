package pumpspick.hobbystore.reserve.ui.composable.screen.productdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import pumpspick.hobbystore.reserve.ui.state.DataUiState
import pumpspick.hobbystore.reserve.ui.theme.*
import pumpspick.hobbystore.reserve.ui.viewmodel.ProductDetailsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailsScreen(
    productId: Int,
    onBack: () -> Unit,
    viewModel: ProductDetailsViewModel = koinViewModel()
) {
    val productState by viewModel.productDetailsState.collectAsStateWithLifecycle()
    val product = when (val s = productState) {
        is DataUiState.Populated -> s.data
        else -> null
    }

    LaunchedEffect(productId) { viewModel.observeProductDetails(productId) }

    var cartAdded by remember { mutableStateOf(false) }
    LaunchedEffect(cartAdded) {
        if (cartAdded) {
            delay(2000)
            cartAdded = false
        }
    }

    product ?: return

    // 3 images for the gallery
    val imageUrls = listOf(product.imageUrl, product.imageUrl, product.imageUrl)
    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            // Edge-to-edge gallery (no rounding)
            Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = imageUrls[page],
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Back button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 44.dp, start = 8.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                // Dots
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(imageUrls.size) { i ->
                        Box(
                            modifier = Modifier
                                .size(if (pagerState.currentPage == i) 16.dp else 6.dp, 6.dp)
                                .clip(CircleShape)
                                .background(if (pagerState.currentPage == i) Primary else Color.White.copy(alpha = 0.6f))
                        )
                    }
                }
            }

            // Content
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = product.title,
                    fontFamily = HeadingFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = OnSurface,
                    lineHeight = 30.sp
                )
                // Category chip
                Surface(
                    color = ChipBackground,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = product.category.name.replace("_", " ")
                            .lowercase().split(" ")
                            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } },
                        fontFamily = BodyFamily,
                        fontSize = 12.sp,
                        color = ChipContent,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = product.description,
                    fontFamily = BodyFamily,
                    fontSize = 14.sp,
                    color = Muted,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Sticky bottom bar: price + Add to Cart
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "£${String.format("%.2f", product.price)}",
                    fontFamily = HeadingFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Primary
                )
                Button(
                    onClick = {
                        viewModel.addProductToCart()
                        cartAdded = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OnSurface),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        "Add to Cart",
                        fontFamily = BodyFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = Surface
                    )
                }
            }
        }

        // Add to cart feedback banner (editorial style)
        AnimatedVisibility(
            visible = cartAdded,
            enter = slideInVertically { it },
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface)
                    .padding(top = 1.dp)
                    .background(Border)
                    .background(Surface)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Added to cart",
                        fontFamily = BodyFamily,
                        fontSize = 14.sp,
                        color = OnSurface
                    )
                }
            }
        }
    }
}
