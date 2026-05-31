package pumpspick.hobbystore.reserve

import android.app.Application
import pumpspick.hobbystore.reserve.di.dataModule
import pumpspick.hobbystore.reserve.di.dispatcherModule
import pumpspick.hobbystore.reserve.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class FECTBApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val appModules = dataModule + viewModule + dispatcherModule

        startKoin {
            androidLogger()
            androidContext(this@FECTBApplication)
            modules(appModules)
        }
    }
}