package br.com.diogozarpelao.leiloesretrogames.data.repository

import br.com.diogozarpelao.leiloesretrogames.data.local.AuctionDao
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import kotlinx.coroutines.flow.Flow

class AuctionRepository(
    private val auctionDao: AuctionDao
) {

    val auctions: Flow<List<Auction>> = auctionDao.observeAll()

    suspend fun insert(auction: Auction): Long {
        return auctionDao.insert(auction)
    }

    suspend fun update(auction: Auction) {
        auctionDao.update(auction)
    }

    suspend fun delete(auction: Auction) {
        auctionDao.delete(auction)
    }

    suspend fun getById(id: Long): Auction? {
        return auctionDao.getById(id)
    }
}