package br.com.diogozarpelao.leiloesretrogames.model

import org.junit.Assert.assertEquals
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
            endTimeMillis = 1_800_000_000_000,
            initialBidInCents = 500,
            bidIncrementInCents = 500
        )

        assertEquals(0, auction.id)
        assertEquals("", auction.notes)
        assertNull(auction.buyoutPriceInCents)
        assertNull(auction.finalPriceInCents)
        assertEquals(ItemCondition.NOT_INFORMED, auction.condition)
        assertEquals(AuctionStatus.ACTIVE, auction.status)
        assertTrue(auction.alertsEnabled)
    }
}