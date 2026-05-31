package pumpspick.hobbystore.reserve.di

import pumpspick.hobbystore.reserve.data.repository.CartRepository
import pumpspick.hobbystore.reserve.data.repository.FECTBOnboardingRepo
import pumpspick.hobbystore.reserve.data.repository.OrderRepository
import pumpspick.hobbystore.reserve.data.repository.ProductRepository

import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    includes(databaseModule, dataStoreModule)

    single {
        FECTBOnboardingRepo(
            fectbOnboardingStoreManager = get(),
            coroutineDispatcher = get(named("IO"))
        )
    }

    single { ProductRepository() }

    single {
        CartRepository(
            cartItemDao = get(),
            coroutineDispatcher = get(named("IO"))
        )
    }

    single {
        OrderRepository(
            orderDao = get(),
            coroutineDispatcher = get(named("IO"))
        )
    }
}