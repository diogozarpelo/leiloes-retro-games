package br.com.diogozarpelao.leiloesretrogames.model

enum class ItemCondition {
    EXCELLENT,
    GOOD,
    AVERAGE,
    POOR,
    VERY_POOR,
    NOT_INFORMED
}

enum class AuctionStatus {
    ACTIVE,
    ENDED,
    NOT_WON,
    WON_PENDING_PAYMENT,
    WON_PAID
}

data class Auction(
    val id: Long = 0,
    val title: String,
    val platform: String,
    val postUrl: String,
    val endTimeMillis: Long,
    val notes: String = "",
    val initialBidInCents: Long,
    val bidIncrementInCents: Long,
    val buyoutPriceInCents: Long? = null
)
