package pumpspick.hobbystore.reserve.ui.composable.screen.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import pumpspick.hobbystore.reserve.data.entity.OrderEntity
import pumpspick.hobbystore.reserve.ui.composable.screen.checkout.CheckoutDialog
import pumpspick.hobbystore.reserve.ui.state.DataUiState
import pumpspick.hobbystore.reserve.ui.theme.*
import pumpspick.hobbystore.reserve.ui.viewmodel.CheckoutViewModel

@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    viewModel: CheckoutViewModel = koinViewModel(),
    onNavigateToOrdersScreen: () -> Unit,
) {
    val orderState by viewModel.orderState.collectAsStateWithLifecycle()
    val emailInvalid by viewModel.emailInvalidState.collectAsStateWithLifecycle()

    val isFormComplete = viewModel.customerFirstName.isNotBlank() &&
            viewModel.customerLastName.isNotBlank() &&
            viewModel.customerEmail.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, color = OnSurface) },
                navigationIcon = {
                    IconButton(onClick = onNavigateToOrdersScreen) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = OnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Contact Details", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OnSurface)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.customerFirstName,
                    onValueChange = { viewModel.updateCustomerFirstName(it) },
                    label = { Text("First Name", fontFamily = BodyFamily) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = viewModel.customerLastName,
                    onValueChange = { viewModel.updateCustomerLastName(it) },
                    label = { Text("Last Name", fontFamily = BodyFamily) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            OutlinedTextField(
                value = viewModel.customerEmail,
                onValueChange = { viewModel.updateCustomerEmail(it) },
                label = { Text("Email Address", fontFamily = BodyFamily) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = emailInvalid,
                supportingText = if (emailInvalid) {{ Text("Please enter a valid email", color = MaterialTheme.colorScheme.error, fontFamily = BodyFamily) }} else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(8.dp)
            )

            Button(
                onClick = { viewModel.placeOrder() },
                enabled = isFormComplete,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OnSurface,
                    disabledContainerColor = Border
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Place Order",
                    fontFamily = HeadingFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isFormComplete) Surface else Muted
                )
            }
        }
    }

    if (orderState is DataUiState.Populated) {
        val order = (orderState as DataUiState.Populated<OrderEntity>).data
        CheckoutDialog(
            orderNumber = order.orderNumber,
            onDismiss = onNavigateToOrdersScreen
        )
    }
}
