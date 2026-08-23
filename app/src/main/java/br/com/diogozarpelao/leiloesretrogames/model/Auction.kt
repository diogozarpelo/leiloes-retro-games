package br.com.diogozarpelao.leiloesretrogames.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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

@Entity(tableName = "auctions")
data class Auction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val platform: String,
    val postUrl: String,
    val endTimeMillis: Long,
    val notes: String = "",
    val initialBidInCents: Long,
    val bidIncrementInCents: Long,
    val buyoutPriceInCents: Long? = null,
    val finalPriceInCents: Long? = null,
    val condition: ItemCondition = ItemCondition.NOT_INFORMED,
    val status: AuctionStatus = AuctionStatus.ACTIVE,
    val alertsEnabled: Boolean = true
)
