package pumpspick.hobbystore.reserve.di

import pumpspick.hobbystore.reserve.ui.viewmodel.AppViewModel
import pumpspick.hobbystore.reserve.ui.viewmodel.CartViewModel
import pumpspick.hobbystore.reserve.ui.viewmodel.CheckoutViewModel
import pumpspick.hobbystore.reserve.ui.viewmodel.FECTBOnboardingVM
import pumpspick.hobbystore.reserve.ui.viewmodel.OrderViewModel
import pumpspick.hobbystore.reserve.ui.viewmodel.ProductDetailsViewModel
import pumpspick.hobbystore.reserve.ui.viewmodel.ProductViewModel
import pumpspick.hobbystore.reserve.ui.viewmodel.FECTBSplashVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel {
        AppViewModel(
            cartRepository = get()
        )
    }

    viewModel {
        FECTBSplashVM(
            onboardingRepository = get()
        )
    }

    viewModel {
        FECTBOnboardingVM(
            onboardingRepository = get()
        )
    }

    viewModel {
        ProductViewModel(
            productRepository = get(),
            cartRepository = get(),
        )
    }

    viewModel {
        ProductDetailsViewModel(
            productRepository = get(),
            cartRepository = get(),
        )
    }

    viewModel {
        CheckoutViewModel(
            cartRepository = get(),
            productRepository = get(),
            orderRepository = get(),
        )
    }

    viewModel {
        CartViewModel(
            cartRepository = get(),
            productRepository = get(),
        )
    }

    viewModel {
        OrderViewModel(
            orderRepository = get(),
        )
    }
}