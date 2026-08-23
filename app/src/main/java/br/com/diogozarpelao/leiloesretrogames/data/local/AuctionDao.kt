package br.com.diogozarpelao.leiloesretrogames.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.diogozarpelao.leiloesretrogames.model.Auction

@Dao
interface AuctionDao {

    @Insert
    suspend fun insert(auction: Auction): Long

    @Query("SELECT * FROM auctions ORDER BY endTimeMillis ASC")
    suspend fun getAll(): List<Auction>
}