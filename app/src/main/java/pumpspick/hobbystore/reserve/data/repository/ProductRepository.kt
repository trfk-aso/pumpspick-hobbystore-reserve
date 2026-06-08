package pumpspick.hobbystore.reserve.data.repository

import pumpspick.hobbystore.reserve.data.model.Product
import pumpspick.hobbystore.reserve.data.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProductRepository {
    private val products: List<Product> = listOf(
        Product(
            id = 1,
            title = "Watercolour Painting Set",
            description = "Professional-grade watercolour set with 36 vibrant pigments, two fine-tip brushes, a mixing palette, and a portable tin case. Perfect for beginners and experienced artists alike.",
            price = 34.99,
            imageUrl = "https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=800",
            category = ProductCategory.STATIONERY_ART
        ),
        Product(
            id = 2,
            title = "DIY Macramé Kit",
            description = "Complete starter kit for macramé enthusiasts. Includes natural cotton rope, wooden dowels, a step-by-step instruction booklet, and enough material to make three wall hangings.",
            price = 28.50,
            imageUrl = "https://images.unsplash.com/photo-1631945788919-24e76faead25?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8TWFjcmFtJUMzJUE5JTIwS2l0fGVufDB8fDB8fHww",
            category = ProductCategory.HOBBIES
        ),
        Product(
            id = 3,
            title = "Premium Scented Candle Collection",
            description = "Set of four hand-poured soy candles in warm seasonal fragrances: amber vanilla, cedar pine, rose sandalwood, and ocean breeze. Burn time 40 hours each.",
            price = 42.00,
            imageUrl = "https://images.unsplash.com/photo-1612293905607-b003de9e54fb?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8U2NlbnRlZCUyMENhbmRsZXxlbnwwfHwwfHx8MA%3D%3D",
            category = ProductCategory.HOME_LIFESTYLE
        ),
        Product(
            id = 4,
            title = "1000-Piece Cityscape Puzzle",
            description = "Beautifully illustrated panoramic cityscape jigsaw puzzle featuring London's iconic skyline at dusk. High-quality thick pieces with a glare-free finish.",
            price = 22.99,
            imageUrl = "https://images.unsplash.com/photo-1715702803426-39aa0d4c39a9?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8MTAwMC1QaWVjZSUyMENpdHlzY2FwZSUyMFB1enpsZXxlbnwwfHwwfHx8MA%3D%3D",
            category = ProductCategory.GAMES_PUZZLES
        ),
        Product(
            id = 5,
            title = "Leather Travel Journal",
            description = "Handcrafted genuine leather journal with 200 pages of acid-free cream paper. Features a magnetic clasp, pen loop, and inner pocket. Ideal for travellers and journalers.",
            price = 38.00,
            imageUrl = "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=800",
            category = ProductCategory.STATIONERY_ART
        ),
        Product(
            id = 6,
            title = "Miniature Terrarium Kit",
            description = "Glass dome terrarium kit with succulents, decorative stones, activated charcoal, and planting soil. A living gift that requires minimal maintenance and lasts for years.",
            price = 31.50,
            imageUrl = "https://images.unsplash.com/photo-1762180581579-2f63d3d06a0d?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8TWluaWF0dXJlJTIwVGVycmFyaXVtfGVufDB8fDB8fHww",
            category = ProductCategory.HOME_LIFESTYLE
        ),
        Product(
            id = 7,
            title = "Vintage Map Art Print Set",
            description = "Set of three A3 framing-ready vintage map prints of classic European cities. Printed on 200gsm matte art paper with warm sepia tones.",
            price = 27.00,
            imageUrl = "https://images.unsplash.com/photo-1723306009175-dca7d26f3350?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTJ8fFZpbnRhZ2UlMjBNYXAlMjBBcnQlMjBQcmludHxlbnwwfHwwfHx8MA%3D%3D",
            category = ProductCategory.GIFTS_SOUVENIRS
        ),
        Product(
            id = 8,
            title = "Card Game: Trivia Nights",
            description = "500-question trivia card game covering history, pop culture, science, food, and sport. For 2–8 players, designed for family game nights and social gatherings.",
            price = 18.99,
            imageUrl = "https://images.unsplash.com/photo-1611996575749-79a3a250f948?w=800",
            category = ProductCategory.GAMES_PUZZLES
        ),
        Product(
            id = 9,
            title = "Personalised Ceramic Mug",
            description = "Handmade ceramic mug with a textured matte glaze finish. Each mug is unique. Microwave and dishwasher safe. Available in dusty rose, sage green, and slate grey.",
            price = 16.50,
            imageUrl = "https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?w=800",
            category = ProductCategory.GIFTS_SOUVENIRS
        ),
        Product(
            id = 10,
            title = "Botanical Pressed Flower Kit",
            description = "Complete flower pressing kit with a wooden press, blotting paper, mounting cards, and a guide to 50 common wild flowers. A timeless hobby for all ages.",
            price = 24.00,
            imageUrl = "https://images.unsplash.com/photo-1750268222879-80c8f47d93a8?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTF8fEJvdGFuaWNhbCUyMFByZXNzZWQlMjBGbG93ZXJ8ZW58MHx8MHx8fDA%3D",
            category = ProductCategory.HOBBIES
        ),
    )

    fun observeById(id: Int): Flow<Product?> {
        val item = products.find { it.id == id }
        return flowOf(item)
    }

    fun getById(id: Int): Product? {
        return products.find { it.id == id }
    }

    fun observeAll(): Flow<List<Product>> {
        return flowOf(products)
    }
}
