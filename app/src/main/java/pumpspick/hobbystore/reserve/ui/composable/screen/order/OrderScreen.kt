package pumpspick.hobbystore.reserve.ui.composable.screen.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import pumpspick.hobbystore.reserve.data.entity.OrderEntity
import pumpspick.hobbystore.reserve.ui.state.DataUiState
import pumpspick.hobbystore.reserve.ui.theme.*
import pumpspick.hobbystore.reserve.ui.viewmodel.OrderViewModel

@Composable
fun OrdersScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = koinViewModel(),
) {
    val ordersState by viewModel.ordersState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, color = OnSurface) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        },
        containerColor = Background
    ) { padding ->
        when (val state = ordersState) {
            is DataUiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Filled.Inbox, contentDescription = null, modifier = Modifier.size(64.dp), tint = Border)
                        Text("No orders yet", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Muted)
                        Text("Your completed orders will appear here", fontFamily = BodyFamily, fontSize = 14.sp, color = Muted)
                    }
                }
            }
            is DataUiState.Populated -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.data) { order ->
                        OrderCard(order = order)
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
private fun OrderCard(order: OrderEntity) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Text("Order #${order.orderNumber}", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OnSurface)
                Surface(color = Success.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp)) {
                    Text("Completed", fontFamily = BodyFamily, fontSize = 12.sp, color = Success, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
                }
            }
            Text(order.description, fontFamily = BodyFamily, fontSize = 13.sp, color = Muted, maxLines = 2)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${order.customerFirstName} ${order.customerLastName}", fontFamily = BodyFamily, fontSize = 13.sp, color = Muted)
                Text("£${String.format("%.2f", order.price)}", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Primary)
            }
        }
    }
}
