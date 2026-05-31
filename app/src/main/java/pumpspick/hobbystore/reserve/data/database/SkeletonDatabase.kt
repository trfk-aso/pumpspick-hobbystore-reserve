package pumpspick.hobbystore.reserve.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pumpspick.hobbystore.reserve.data.dao.CartItemDao
import pumpspick.hobbystore.reserve.data.dao.OrderDao
import pumpspick.hobbystore.reserve.data.database.converter.Converters
import pumpspick.hobbystore.reserve.data.entity.CartItemEntity
import pumpspick.hobbystore.reserve.data.entity.OrderEntity

@Database(
    entities = [CartItemEntity::class, OrderEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FECTBDatabase : RoomDatabase() {

    abstract fun cartItemDao(): CartItemDao

    abstract fun orderDao(): OrderDao
}