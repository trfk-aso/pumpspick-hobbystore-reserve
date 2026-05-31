package pumpspick.hobbystore.reserve.ui.state

data class CartItemUiState(
    val productId: Int,
    val productTitle: String,
    val productPrice: Double,
    val quantity: Int,
    val productImageUrl: String? = null,
)