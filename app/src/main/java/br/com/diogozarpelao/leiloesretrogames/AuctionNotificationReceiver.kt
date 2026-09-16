package br.com.diogozarpelao.leiloesretrogames

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class AuctionNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        if (!canPostNotifications(context)) {
            return
        }

        val auctionId =
            intent.getLongExtra(
                AuctionNotificationContract
                    .EXTRA_AUCTION_ID,
                0L
            )

        val auctionTitle =
            intent.getStringExtra(
                AuctionNotificationContract
                    .EXTRA_AUCTION_TITLE
            ) ?: "Leilão"

        val minutesBefore =
            intent.getIntExtra(
                AuctionNotificationContract
                    .EXTRA_MINUTES_BEFORE,
                0
            )

        val contentIntent =
            createContentIntent(
                context = context,
                auctionId = auctionId
            )

        val notification =
            NotificationCompat.Builder(
                context,
                AuctionNotificationContract.CHANNEL_ID
            )
                .setSmallIcon(
                    R.drawable.ic_launcher_foreground
                )
                .setContentTitle(
                    "Leilão terminando"
                )
                .setContentText(
                    "$auctionTitle termina em ${
                        formatRemainingTime(
                            minutesBefore
                        )
                    }."
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(true)
                .setContentIntent(
                    contentIntent
                )
                .build()

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        notificationManager.notify(
            AuctionNotificationContract.eventId(
                auctionId = auctionId,
                minutesBefore = minutesBefore
            ),
            notification
        )
    }

    private fun canPostNotifications(
        context: Context
    ): Boolean {
        if (
            Build.VERSION.SDK_INT <
                Build.VERSION_CODES.TIRAMISU
        ) {
            return true
        }

        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createContentIntent(
        context: Context,
        auctionId: Long
    ): PendingIntent {
        val openAppIntent =
            Intent(
                context,
                MainActivity::class.java
            ).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

        return PendingIntent.getActivity(
            context,
            auctionId.toInt(),
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                PendingIntent.FLAG_IMMUTABLE
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
}