package br.com.diogozarpelao.leiloesretrogames.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.diogozarpelao.leiloesretrogames.model.Auction

@Database(
    entities = [Auction::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun auctionDao(): AuctionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "leiloes_retro_games_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}