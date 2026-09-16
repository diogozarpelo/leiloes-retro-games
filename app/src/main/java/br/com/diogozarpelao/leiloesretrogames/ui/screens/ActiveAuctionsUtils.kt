package br.com.diogozarpelao.leiloesretrogames.ui.screens

import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

internal enum class EndedAuctionFilter {
    ALL,
    PENDING,
    WON,
    NOT_WON
}

internal enum class WonAuctionFilter {
    ALL,
    PENDING_PAYMENT,
    PAID
}

private val auctionCardDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")

internal fun isResultPending(
    auction: Auction
): Boolean {
    return auction.status == AuctionStatus.ACTIVE ||
        auction.status == AuctionStatus.ENDED
}

internal fun filterAuctionsForSection(
    auctions: List<Auction>,
    selectedSection: AuctionSection,
    endedFilter: EndedAuctionFilter,
    wonFilter: WonAuctionFilter,
    currentTime: Long
): List<Auction> {
    val filteredAuctions =
        auctions.filter { auction ->
            when (selectedSection) {
                AuctionSection.ACTIVE ->
                    auction.endTimeMillis > currentTime

                AuctionSection.ENDED -> {
                    val isEnded =
                        auction.endTimeMillis <= currentTime

                    val matchesFilter =
                        when (endedFilter) {
                            EndedAuctionFilter.ALL ->
                                true

                            EndedAuctionFilter.PENDING ->
                                isResultPending(auction)

                            EndedAuctionFilter.WON -> {
                                val isWon =
                                    auction.status ==
                                        AuctionStatus.WON_PENDING_PAYMENT ||
                                        auction.status ==
                                        AuctionStatus.WON_PAID

                                val matchesWonFilter =
                                    when (wonFilter) {
                                        WonAuctionFilter.ALL ->
                                            true

                                        WonAuctionFilter.PENDING_PAYMENT ->
                                            auction.status ==
                                                AuctionStatus.WON_PENDING_PAYMENT

                                        WonAuctionFilter.PAID ->
                                            auction.status ==
                                                AuctionStatus.WON_PAID
                                    }

                                isWon && matchesWonFilter
                            }

                            EndedAuctionFilter.NOT_WON ->
                                auction.status ==
                                    AuctionStatus.NOT_WON
                        }

                    isEnded && matchesFilter
                }
            }
        }

    return if (
        selectedSection == AuctionSection.ENDED
    ) {
        filteredAuctions.sortedWith(
            compareByDescending<Auction> {
                isResultPending(it)
            }.thenByDescending {
                it.endTimeMillis
            }
        )
    } else {
        filteredAuctions
    }
}

internal fun countActiveAuctions(
    auctions: List<Auction>,
    currentTime: Long
): Int {
    return auctions.count {
        it.endTimeMillis > currentTime
    }
}

internal fun countEndedAuctions(
    auctions: List<Auction>,
    currentTime: Long
): Int {
    return auctions.count {
        it.endTimeMillis <= currentTime
    }
}

internal fun calculateAuctionRemainingTime(
    endTimeMillis: Long,
    currentTimeMillis: Long
): String {
    val remainingMillis =
        endTimeMillis - currentTimeMillis

    if (remainingMillis <= 0) {
        return "Encerrado"
    }

    val totalSeconds =
        remainingMillis / 1_000

    val days =
        totalSeconds / 86_400

    val hours =
        (totalSeconds % 86_400) / 3_600

    val minutes =
        (totalSeconds % 3_600) / 60

    val seconds =
        totalSeconds % 60

    return if (days > 0) {
        "${days}d ${hours}h ${minutes}min"
    } else {
        "${hours}h ${minutes}min ${seconds}s"
    }
}

internal fun formatAuctionCardDate(
    value: Long
): String {
    return Instant
        .ofEpochMilli(value)
        .atZone(ZoneId.systemDefault())
        .format(auctionCardDateFormatter)
}

internal fun formatAuctionCurrency(
    valueInCents: Long
): String {
    val value =
        BigDecimal.valueOf(
            valueInCents,
            2
        )

    return NumberFormat
        .getCurrencyInstance(
            Locale("pt", "BR")
        )
        .format(value)
}

internal fun emptyAuctionsMessage(
    selectedSection: AuctionSection,
    endedFilter: EndedAuctionFilter,
    wonFilter: WonAuctionFilter
): String {
    return when {
        selectedSection == AuctionSection.ACTIVE ->
            "Nenhum leilão ativo no momento."

        endedFilter == EndedAuctionFilter.PENDING ->
            "Nenhum resultado pendente."

        endedFilter == EndedAuctionFilter.NOT_WON ->
            "Nenhum leilão não ganho."

        endedFilter == EndedAuctionFilter.WON &&
            wonFilter == WonAuctionFilter.PENDING_PAYMENT ->
            "Nenhum leilão aguardando pagamento."

        endedFilter == EndedAuctionFilter.WON &&
            wonFilter == WonAuctionFilter.PAID ->
            "Nenhum leilão pago."

        endedFilter == EndedAuctionFilter.WON ->
            "Nenhum leilão ganho."

        else ->
            "Nenhum leilão encerrado."
    }
}