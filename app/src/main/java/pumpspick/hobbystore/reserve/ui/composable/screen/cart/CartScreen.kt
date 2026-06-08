package pumpspick.hobbystore.reserve.ui.composable.screen.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel
import pumpspick.hobbystore.reserve.ui.state.CartItemUiState
import pumpspick.hobbystore.reserve.ui.state.DataUiState
import pumpspick.hobbystore.reserve.ui.theme.*
import pumpspick.hobbystore.reserve.ui.viewmodel.CartViewModel

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = koinViewModel(),
    onNavigateToCheckoutScreen: () -> Unit,
) {
    val cartItemsState by viewModel.cartItemsState.collectAsStateWithLifecycle()
    val totalPrice by viewModel.totalPrice.collectAsStateWithLifecycle()

    Scaffold(

        containerColor = Background
    ) { padding ->
        when (val state = cartItemsState) {
            is DataUiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Filled.ShoppingBag, contentDescription = null, modifier = Modifier.size(64.dp), tint = Border)
                        Text("Your cart is empty", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Muted)
                        Text("Start Shopping", fontFamily = BodyFamily, fontSize = 14.sp, color = Primary)
                    }
                }
            }
            is DataUiState.Populated -> {
                val items = state.data
                Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items) { item ->
                            CartItemCard(
                                item = item,
                                onPlus = { viewModel.incrementProductInCart(item.productId) },
                                onMinus = {
                                    if (item.quantity == 1) viewModel.deleteFromCart(item.productId)
                                    else viewModel.decrementItemInCart(item.productId)
                                },
                                onDelete = { viewModel.deleteFromCart(item.productId) }
                            )
                        }
                    }
                    // Summary card
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Subtotal", fontFamily = BodyFamily, fontSize = 14.sp, color = Muted)
                                Text("£${String.format("%.2f", totalPrice)}", fontFamily = BodyFamily, fontSize = 14.sp, color = Muted)
                            }
                            Divider(color = Border)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = OnSurface)
                                Text("£${String.format("%.2f", totalPrice)}", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Primary)
                            }
                            Button(
                                onClick = onNavigateToCheckoutScreen,
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = OnSurface),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Proceed to Checkout", fontFamily = BodyFamily, fontWeight = FontWeight.Medium, color = Surface)
                            }
                        }
                    }
                }
            }
            else -> Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItemUiState,
    onPlus: () -> Unit,
    onMinus: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.productImageUrl,
                contentDescription = item.productTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.productTitle, fontFamily = BodyFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = OnSurface, maxLines = 1)
                Text("£${String.format("%.2f", item.productPrice)}", fontFamily = BodyFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Primary)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMinus, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp), tint = OnSurface)
                }
                Text("${item.quantity}", fontFamily = BodyFamily,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OnSurface, modifier = Modifier.width(24.dp))
                IconButton(onClick = onPlus, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp), tint = OnSurface)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Delete, contentDescription = "Remove", modifier = Modifier.size(16.dp), tint = Muted)
                }
            }
        }
    }
}
