package br.com.diogozarpelao.leiloesretrogames

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import br.com.diogozarpelao.leiloesretrogames.data.local.AppDatabase
import br.com.diogozarpelao.leiloesretrogames.data.repository.AuctionRepository

class AuctionApplication : Application() {

    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    val repository by lazy {
        AuctionRepository(database.auctionDao())
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AUCTION_NOTIFICATION_CHANNEL_ID,
                "Alertas de leilões",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description =
                    "Avisos antes do encerramento dos leilões"
            }

            val notificationManager =
                getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val AUCTION_NOTIFICATION_CHANNEL_ID =
            "auction_alerts"
    }
}