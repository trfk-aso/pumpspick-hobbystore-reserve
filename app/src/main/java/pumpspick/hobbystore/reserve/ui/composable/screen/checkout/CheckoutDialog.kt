package pumpspick.hobbystore.reserve.ui.composable.screen.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pumpspick.hobbystore.reserve.data.entity.OrderEntity
import pumpspick.hobbystore.reserve.ui.theme.*

@Composable
fun CheckoutDialog(
    orderNumber: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        shape = RoundedCornerShape(16.dp),
        icon = {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(48.dp), tint = Primary)
        },
        title = {
            Text(
                "Order Confirmed",
                fontFamily = HeadingFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                color = OnSurface
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Order #$orderNumber",
                    fontFamily = HeadingFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Primary,
                    textAlign = TextAlign.Center
                )
                Surface(color = ChipBackground, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Your order is confirmed. Please collect it from our fulfilment centre within 24 hours.",
                        fontFamily = BodyFamily,
                        fontSize = 13.sp,
                        color = ChipContent,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = OnSurface),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View My Orders", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, color = Surface)
            }
        }
    )
}
