package br.com.diogozarpelao.leiloesretrogames

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class AuctionNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val auctionId =
            intent.getLongExtra(EXTRA_AUCTION_ID, 0L)

        val auctionTitle =
            intent.getStringExtra(EXTRA_AUCTION_TITLE)
                ?: "Leilão"

        val minutesBefore =
            intent.getIntExtra(EXTRA_MINUTES_BEFORE, 0)

        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val openAppIntent = Intent(
            context,
            MainActivity::class.java
        ).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val contentIntent = PendingIntent.getActivity(
            context,
            auctionId.toInt(),
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            context,
            AuctionApplication.AUCTION_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Leilão terminando")
            .setContentText(
                "$auctionTitle termina em ${formatRemainingTime(minutesBefore)}."
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .build()

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        val notificationId =
            (auctionId.toInt() * 100) + minutesBefore

        notificationManager.notify(
            notificationId,
            notification
        )
    }

    private fun formatRemainingTime(
        minutes: Int
    ): String {
        return when (minutes) {
            60 -> "1 hora"
            30 -> "30 minutos"
            15 -> "15 minutos"
            10 -> "10 minutos"
            5 -> "5 minutos"
            else -> "$minutes minutos"
        }
    }

    companion object {
        const val EXTRA_AUCTION_ID =
            "auction_id"

        const val EXTRA_AUCTION_TITLE =
            "auction_title"

        const val EXTRA_MINUTES_BEFORE =
            "minutes_before"
    }
}