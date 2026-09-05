package br.com.diogozarpelao.leiloesretrogames.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuctionTest {

    @Test
    fun `new auction uses expected default values`() {
        val auction = Auction(
            title = "Resident Evil 2",
            platform = "PlayStation",
            postUrl = "https://facebook.com/example",
            endTimeMillis = 1_800_000_000_000L,
            initialBidInCents = 500L,
            bidIncrementInCents = 500L
        )

        assertEquals(0L, auction.id)
        assertEquals("", auction.notes)
        assertNull(auction.buyoutPriceInCents)
        assertNull(auction.finalPriceInCents)
        assertEquals(ItemCondition.NOT_INFORMED, auction.condition)
        assertEquals(AuctionStatus.ACTIVE, auction.status)
        assertTrue(auction.alertsEnabled)
    }

    @Test
    fun `auction stores custom values correctly`() {
        val auction = Auction(
            id = 10L,
            title = "Metal Gear Solid",
            platform = "PlayStation",
            postUrl = "https://facebook.com/example",
            endTimeMillis = 1_900_000_000_000L,
            notes = "Completo",
            initialBidInCents = 1_000L,
            bidIncrementInCents = 500L,
            buyoutPriceInCents = 15_000L,
            finalPriceInCents = 12_500L,
            condition = ItemCondition.EXCELLENT,
            status = AuctionStatus.WON_PAID,
            alertsEnabled = false
        )

        assertEquals(10L, auction.id)
        assertEquals("Metal Gear Solid", auction.title)
        assertEquals("PlayStation", auction.platform)
        assertEquals("https://facebook.com/example", auction.postUrl)
        assertEquals(1_900_000_000_000L, auction.endTimeMillis)
        assertEquals("Completo", auction.notes)
        assertEquals(1_000L, auction.initialBidInCents)
        assertEquals(500L, auction.bidIncrementInCents)
        assertEquals(15_000L, auction.buyoutPriceInCents)
        assertEquals(12_500L, auction.finalPriceInCents)
        assertEquals(ItemCondition.EXCELLENT, auction.condition)
        assertEquals(AuctionStatus.WON_PAID, auction.status)
        assertFalse(auction.alertsEnabled)
    }

    @Test
    fun `auction can be marked as not won`() {
        val auction = createAuction()

        val updatedAuction = auction.copy(
            status = AuctionStatus.NOT_WON,
            finalPriceInCents = null
        )

        assertEquals(AuctionStatus.NOT_WON, updatedAuction.status)
        assertNull(updatedAuction.finalPriceInCents)
    }

    @Test
    fun `auction can be marked as won pending payment`() {
        val auction = createAuction()

        val updatedAuction = auction.copy(
            status = AuctionStatus.WON_PENDING_PAYMENT,
            finalPriceInCents = 7_500L
        )

        assertEquals(
            AuctionStatus.WON_PENDING_PAYMENT,
            updatedAuction.status
        )
        assertEquals(7_500L, updatedAuction.finalPriceInCents)
    }

    @Test
    fun `auction can be marked as paid`() {
        val auction = createAuction().copy(
            status = AuctionStatus.WON_PENDING_PAYMENT,
            finalPriceInCents = 7_500L
        )

        val paidAuction = auction.copy(
            status = AuctionStatus.WON_PAID
        )

        assertEquals(AuctionStatus.WON_PAID, paidAuction.status)
        assertEquals(7_500L, paidAuction.finalPriceInCents)
    }

    @Test
    fun `auction alerts can be disabled`() {
        val auction = createAuction()

        val updatedAuction = auction.copy(
            alertsEnabled = false
        )

        assertFalse(updatedAuction.alertsEnabled)
    }

    private fun createAuction(): Auction {
        return Auction(
            title = "Resident Evil 2",
            platform = "PlayStation",
            postUrl = "https://facebook.com/example",
            endTimeMillis = 1_800_000_000_000L,
            initialBidInCents = 500L,
            bidIncrementInCents = 500L
        )
    }
}