package pumpspick.hobbystore.reserve.ui.composable.approot

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pumpspick.hobbystore.reserve.R
import pumpspick.hobbystore.reserve.ui.composable.navigation.AppNavHost
import pumpspick.hobbystore.reserve.ui.composable.navigation.NavRoute
import pumpspick.hobbystore.reserve.ui.state.DataUiState
import pumpspick.hobbystore.reserve.ui.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.KClass

private val navigationItems: List<BottomNavItem> = listOf(
    BottomNavItem(
        titleRes = R.string.home,
        icon = Icons.Default.Home,
        route = NavRoute.Home,
    ),
    BottomNavItem(
        titleRes = R.string.cart,
        icon = Icons.Default.ShoppingCart,
        route = NavRoute.Cart,
    ),
    BottomNavItem(
        titleRes = R.string.orders,
        icon = Icons.Default.CalendarToday,
        route = NavRoute.Orders,
    ),
    BottomNavItem(
        titleRes = R.string.settings,
        icon = Icons.Default.Settings,
        route = NavRoute.Settings,
    ),
)

private val topBarHiddenScreens: List<KClass<out NavRoute>> = listOf(
    NavRoute.Splash::class,
    NavRoute.Onboarding::class,
)

private val bottomBarHiddenScreens: List<KClass<out NavRoute>> = listOf(
    NavRoute.Splash::class,
    NavRoute.Onboarding::class,
    NavRoute.ProductDetails::class,
    NavRoute.Checkout::class,
)

@Composable
fun AppRoot(
    viewModel: AppViewModel = koinViewModel()
) {
    val cartPopulatedState by viewModel.cartPopulatedState.collectAsState()
    val itemsInCartState by viewModel.itemsInCartState.collectAsState()

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    var shouldShowClearCartDialog by remember { mutableStateOf(false) }

    val shouldShowBottomBar = !currentDestination.matchesAnyRoute(bottomBarHiddenScreens)
    val shouldShowTopBar = !currentDestination.matchesAnyRoute(topBarHiddenScreens)

    val onNavigateToRoute = { item: BottomNavItem ->
        navController.navigate(item.route) {
            popUpTo(NavRoute.Home) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    AppRootContent(
        navController = navController,
        currentDestination = currentDestination,
        itemsInCart = itemsInCartState,
        isCartNotEmpty = cartPopulatedState is DataUiState.Populated,
        shouldShowTopBar = shouldShowTopBar,
        shouldShowBottomBar = shouldShowBottomBar,
        onClearCartIconClick = {
            viewModel.clearCart()
            shouldShowClearCartDialog = true },
        onNavigateToRoute = onNavigateToRoute,
        onNavigateBack = { navController.popBackStack() }
    )

    if (shouldShowClearCartDialog) {
        ClearCartDialog(
            onDismiss = { shouldShowClearCartDialog = false },
            onConfirm = {
                viewModel.clearCart()
                shouldShowClearCartDialog = false
            }
        )
    }
}

@Composable
private fun AppRootContent(
    navController: NavHostController,
    currentDestination: NavDestination?,
    itemsInCart: Int,
    isCartNotEmpty: Boolean,
    shouldShowTopBar: Boolean,
    shouldShowBottomBar: Boolean,
    onClearCartIconClick: () -> Unit,
    onNavigateToRoute: (BottomNavItem) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            if (shouldShowTopBar) {
                AppTopBar(
                    currentDestination = currentDestination,
                    isCartNotEmpty = isCartNotEmpty,
                    onClearCartIconClick = onClearCartIconClick,
                    onNavigateBack = onNavigateBack,
                )
            }
        },

        bottomBar = {
            if (shouldShowBottomBar) {
                AppBottomBar(
                    itemsInCart = itemsInCart,
                    currentDestination = currentDestination,
                    navigationItems = navigationItems,
                    onNavigateToRoute = onNavigateToRoute,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        )
    }
}

fun NavDestination?.matchesAnyRoute(routes: List<KClass<out NavRoute>>): Boolean {
    return this?.let { destination ->
        routes.any { route -> destination.hasRoute(route) }
    } == true
}