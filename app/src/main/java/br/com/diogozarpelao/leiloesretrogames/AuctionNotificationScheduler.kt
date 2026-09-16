package br.com.diogozarpelao.leiloesretrogames

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import br.com.diogozarpelao.leiloesretrogames.model.Auction

class AuctionNotificationScheduler(
    context: Context
) {

    private val appContext =
        context.applicationContext

    private val alarmManager =
        appContext.getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

    fun schedule(
        auction: Auction
    ) {
        cancel(auction.id)

        if (!auction.alertsEnabled) {
            return
        }

        AuctionNotificationContract
            .ALERT_MINUTES
            .forEach { minutesBefore ->
                scheduleAlert(
                    auction = auction,
                    minutesBefore =
                        minutesBefore
                )
            }
    }

    fun cancel(
        auctionId: Long
    ) {
        AuctionNotificationContract
            .ALERT_MINUTES
            .forEach { minutesBefore ->
                val pendingIntent =
                    createPendingIntent(
                        auctionId =
                            auctionId,
                        auctionTitle =
                            "",
                        minutesBefore =
                            minutesBefore
                    )

                alarmManager.cancel(
                    pendingIntent
                )

                pendingIntent.cancel()
            }
    }

    private fun scheduleAlert(
        auction: Auction,
        minutesBefore: Int
    ) {
        val triggerTime =
            auction.endTimeMillis -
                (minutesBefore * 60_000L)

        if (
            triggerTime <=
                System.currentTimeMillis()
        ) {
            return
        }

        val pendingIntent =
            createPendingIntent(
                auctionId =
                    auction.id,
                auctionTitle =
                    auction.title,
                minutesBefore =
                    minutesBefore
            )

        val canUseExactAlarm =
            Build.VERSION.SDK_INT <
                Build.VERSION_CODES.S ||
                alarmManager
                    .canScheduleExactAlarms()

        if (canUseExactAlarm) {
            alarmManager
                .setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
        } else {
            alarmManager
                .setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
        }
    }

    private fun createPendingIntent(
        auctionId: Long,
        auctionTitle: String,
        minutesBefore: Int
    ): PendingIntent {
        val intent =
            Intent(
                appContext,
                AuctionNotificationReceiver::class.java
            ).apply {
                putExtra(
                    AuctionNotificationContract
                        .EXTRA_AUCTION_ID,
                    auctionId
                )

                putExtra(
                    AuctionNotificationContract
                        .EXTRA_AUCTION_TITLE,
                    auctionTitle
                )

                putExtra(
                    AuctionNotificationContract
                        .EXTRA_MINUTES_BEFORE,
                    minutesBefore
                )
            }

        return PendingIntent.getBroadcast(
            appContext,
            AuctionNotificationContract.eventId(
                auctionId =
                    auctionId,
                minutesBefore =
                    minutesBefore
            ),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                PendingIntent.FLAG_IMMUTABLE
        )
    }
}