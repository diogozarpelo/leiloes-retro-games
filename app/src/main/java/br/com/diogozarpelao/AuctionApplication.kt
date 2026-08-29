package br.com.diogozarpelao.leiloesretrogames

import android.app.Application
import br.com.diogozarpelao.leiloesretrogames.data.local.AppDatabase
import br.com.diogozarpelao.leiloesretrogames.data.repository.AuctionRepository

class AuctionApplication : Application() {

    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    val repository by lazy {
        AuctionRepository(database.auctionDao())
    }
}