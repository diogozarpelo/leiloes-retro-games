package br.com.diogozarpelao.leiloesretrogames.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.diogozarpelao.leiloesretrogames.model.Auction

@Database(
    entities = [Auction::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun auctionDao(): AuctionDao
}