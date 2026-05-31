package pumpspick.hobbystore.reserve.ui.composable.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import pumpspick.hobbystore.reserve.data.model.Product
import pumpspick.hobbystore.reserve.data.model.ProductCategory
import pumpspick.hobbystore.reserve.ui.state.DataUiState
import pumpspick.hobbystore.reserve.ui.theme.*
import pumpspick.hobbystore.reserve.ui.viewmodel.ProductViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onProductClick: (Int) -> Unit,
    viewModel: ProductViewModel = koinViewModel()
) {
    val productsState by viewModel.productsState.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }

    val products = when (val s = productsState) {
        is DataUiState.Populated -> s.data
        else -> emptyList()
    }

    val featured = products.take(4)
    val filtered = if (selectedCategory == null) products else products.filter { it.category == selectedCategory }

    val pagerState = rememberPagerState(pageCount = { if (featured.isEmpty()) 1 else featured.size })
    LaunchedEffect(featured.size) {
        if (featured.size > 1) {
            while (true) {
                delay(4000)
                val next = (pagerState.currentPage + 1) % featured.size
                pagerState.animateScrollToPage(next)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pumps Pick",
                        fontFamily = HeadingFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = OnSurface
                    )
                },
                actions = {
                    IconButton(onClick = { selectedCategory = null }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = OnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        },
        containerColor = Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Hero card — first featured product
            if (featured.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onProductClick(featured[0].id) }
                    ) {
                        AsyncImage(
                            model = featured[0].imageUrl,
                            contentDescription = featured[0].title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xBB000000))))
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = featured[0].title,
                                fontFamily = HeadingFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                            Text(
                                text = "£${String.format("%.2f", featured[0].price)}",
                                fontFamily = BodyFamily,
                                fontSize = 16.sp,
                                color = Primary
                            )
                        }
                        // Pager dots
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            repeat(featured.size) { i ->
                                Box(
                                    modifier = Modifier
                                        .size(if (pagerState.currentPage == i) 16.dp else 6.dp, 6.dp)
                                        .clip(CircleShape)
                                        .background(if (pagerState.currentPage == i) Primary else Color.White.copy(alpha = 0.5f))
                                )
                            }
                        }
                    }
                }
            }

            // Category chips — pill-shaped with border, no fill
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ProductCategory.values().toList()) { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.dp, if (isSelected) Primary else Border, CircleShape)
                                .background(if (isSelected) Primary.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable { selectedCategory = if (selectedCategory == cat) null else cat }
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat.name.replace("_", " ").lowercase().split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } },
                                fontFamily = BodyFamily,
                                fontSize = 13.sp,
                                color = if (isSelected) Primary else OnSurface
                            )
                        }
                    }
                }
            }

            // Staggered 2-col grid
            val rows = filtered.chunked(2)
            items(rows) { rowProducts ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowProducts.forEachIndexed { idx, product ->
                        val cardHeight = if ((rows.indexOf(rowProducts) + idx) % 2 == 0) 240.dp else 180.dp
                        ProductCard(
                            product = product,
                            cardHeight = cardHeight,
                            onClick = { onProductClick(product.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    cardHeight: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(cardHeight)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = product.title,
                    fontFamily = BodyFamily,
                    fontSize = 13.sp,
                    color = OnSurface,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "£${String.format("%.2f", product.price)}",
                        fontFamily = BodyFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Primary
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ChipBackground)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = product.category.name.replace("_", " ").split(" ").first(),
                            fontFamily = BodyFamily,
                            fontSize = 10.sp,
                            color = ChipContent
                        )
                    }
                }
            }
        }
    }
}
