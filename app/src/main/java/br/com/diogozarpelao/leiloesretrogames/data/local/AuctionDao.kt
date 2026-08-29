package br.com.diogozarpelao.leiloesretrogames.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import kotlinx.coroutines.flow.Flow

@Dao
interface AuctionDao {

    @Insert
    suspend fun insert(auction: Auction): Long

    @Update
    suspend fun update(auction: Auction)

    @Delete
    suspend fun delete(auction: Auction)

    @Query("SELECT * FROM auctions ORDER BY endTimeMillis ASC")
    fun observeAll(): Flow<List<Auction>>

    @Query("SELECT * FROM auctions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Auction?
}