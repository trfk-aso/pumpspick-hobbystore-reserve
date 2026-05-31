package pumpspick.hobbystore.reserve.di

import pumpspick.hobbystore.reserve.data.datastore.FECTBOnboardingPrefs
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
    single { FECTBOnboardingPrefs(androidContext()) }
}