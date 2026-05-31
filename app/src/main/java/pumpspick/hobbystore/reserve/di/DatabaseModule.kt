package pumpspick.hobbystore.reserve.di

import androidx.room.Room
import pumpspick.hobbystore.reserve.data.database.FECTBDatabase
import org.koin.dsl.module

private const val DB_NAME = "fectb_db"

val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            klass = FECTBDatabase::class.java,
            name = DB_NAME
        ).build()
    }

    single { get<FECTBDatabase>().cartItemDao() }

    single { get<FECTBDatabase>().orderDao() }
}