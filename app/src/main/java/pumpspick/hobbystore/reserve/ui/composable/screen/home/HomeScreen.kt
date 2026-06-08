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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (Int) -> Unit,
    viewModel: ProductViewModel = koinViewModel()
) {
    val productsState by viewModel.productsState.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val products = when (val s = productsState) {
        is DataUiState.Populated -> s.data
        else -> emptyList()
    }

    val featured = products.take(4)

    val filtered = products.filter { product ->
        val matchesCategory = selectedCategory == null || product.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() || product.title.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

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
        containerColor = Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = {
                        Text("Search hobbies...", color = OnSurface.copy(alpha = 0.5f), fontFamily = BodyFamily)
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = Primary)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Border
                    )
                )
            }

            if (searchQuery.isBlank() && featured.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        HorizontalPager(state = pagerState) { page ->
                            val product = featured[page]
                            Box(modifier = Modifier.fillMaxSize().clickable { onProductClick(product.id) }) {
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = product.title,
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
                                        .padding(bottom = 12.dp)
                                ) {
                                    Text(
                                        text = product.title,
                                        fontFamily = HeadingFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "£${String.format("%.2f", product.price)}",
                                        fontFamily = BodyFamily,
                                        fontSize = 16.sp,
                                        color = Primary
                                    )
                                }
                            }
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

            // Category chips
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        val isSelected = selectedCategory == null
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.dp, if (isSelected) Primary else Border, CircleShape)
                                .background(if (isSelected) Primary.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable { selectedCategory = null }
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "All",
                                fontFamily = BodyFamily,
                                fontSize = 13.sp,
                                color = if (isSelected) Primary else OnSurface
                            )
                        }
                    }
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

            if (filtered.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No products found",
                            color = OnSurface.copy(alpha = 0.5f),
                            fontFamily = BodyFamily
                        )
                    }
                }
            } else {
                // Uniform grid
                val rows = filtered.chunked(2)
                items(rows) { rowProducts ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowProducts.forEach { product ->
                            ProductCard(
                                product = product,
                                cardHeight = 240.dp,
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
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Text(
                    text = product.title,
                    fontFamily = BodyFamily,
                    fontSize = 13.sp,
                    color = OnSurface,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.weight(1f))

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