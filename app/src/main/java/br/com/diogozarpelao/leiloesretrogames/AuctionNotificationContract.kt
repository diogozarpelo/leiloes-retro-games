package br.com.diogozarpelao.leiloesretrogames

internal object AuctionNotificationContract {

    const val CHANNEL_ID =
        "auction_alerts"

    const val EXTRA_AUCTION_ID =
        "auction_id"

    const val EXTRA_AUCTION_TITLE =
        "auction_title"

    const val EXTRA_MINUTES_BEFORE =
        "minutes_before"

    val ALERT_MINUTES =
        listOf(
            60,
            30,
            15,
            10,
            5
        )

    fun eventId(
        auctionId: Long,
        minutesBefore: Int
    ): Int {
        return (auctionId.toInt() * 100) +
            minutesBefore
    }
}