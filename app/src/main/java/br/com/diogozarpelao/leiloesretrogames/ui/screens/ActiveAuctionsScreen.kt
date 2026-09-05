package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.delay

private enum class AuctionSection {
    ACTIVE,
    ENDED
}

private enum class EndedAuctionFilter {
    ALL,
    NOT_WON,
    PENDING_PAYMENT,
    PAID
}

private val cardDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")

@Composable
fun ActiveAuctionsScreen(
    auctions: List<Auction>,
    onAuctionClick: (Auction) -> Unit,
    onAddAuction: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by rememberSaveable {
        mutableStateOf(AuctionSection.ACTIVE)
    }

    var endedFilter by rememberSaveable {
        mutableStateOf(EndedAuctionFilter.ALL)
    }

    val currentTime by produceState(
        initialValue = System.currentTimeMillis()
    ) {
        while (true) {
            value = System.currentTimeMillis()
            delay(1_000)
        }
    }

    val activeCount = auctions.count {
        it.endTimeMillis > currentTime
    }

    val endedCount = auctions.count {
        it.endTimeMillis <= currentTime
    }

    val displayedAuctions = auctions.filter { auction ->
        when (selectedSection) {
            AuctionSection.ACTIVE -> {
                auction.endTimeMillis > currentTime
            }

            AuctionSection.ENDED -> {
                val isEnded =
                    auction.endTimeMillis <= currentTime

                val matchesFilter = when (endedFilter) {
                    EndedAuctionFilter.ALL -> true

                    EndedAuctionFilter.NOT_WON ->
                        auction.status == AuctionStatus.NOT_WON

                    EndedAuctionFilter.PENDING_PAYMENT ->
                        auction.status ==
                                AuctionStatus.WON_PENDING_PAYMENT

                    EndedAuctionFilter.PAID ->
                        auction.status == AuctionStatus.WON_PAID
                }

                isEnded && matchesFilter
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Leilões RetroGames",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Acompanhe seus leilões em um só lugar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryCard(
                title = "Ativos",
                value = activeCount.toString(),
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = "Encerrados",
                value = endedCount.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected =
                    selectedSection == AuctionSection.ACTIVE,
                onClick = {
                    selectedSection = AuctionSection.ACTIVE
                },
                label = {
                    Text("Ativos")
                }
            )

            FilterChip(
                selected =
                    selectedSection == AuctionSection.ENDED,
                onClick = {
                    selectedSection = AuctionSection.ENDED
                },
                label = {
                    Text("Encerrados")
                }
            )
        }

        if (selectedSection == AuctionSection.ENDED) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected =
                        endedFilter == EndedAuctionFilter.ALL,
                    onClick = {
                        endedFilter = EndedAuctionFilter.ALL
                    },
                    label = {
                        Text("Todos")
                    }
                )

                FilterChip(
                    selected =
                        endedFilter == EndedAuctionFilter.NOT_WON,
                    onClick = {
                        endedFilter = EndedAuctionFilter.NOT_WON
                    },
                    label = {
                        Text("Não ganho")
                    }
                )

                FilterChip(
                    selected =
                        endedFilter ==
                                EndedAuctionFilter.PENDING_PAYMENT,
                    onClick = {
                        endedFilter =
                            EndedAuctionFilter.PENDING_PAYMENT
                    },
                    label = {
                        Text("A pagar")
                    }
                )

                FilterChip(
                    selected =
                        endedFilter == EndedAuctionFilter.PAID,
                    onClick = {
                        endedFilter = EndedAuctionFilter.PAID
                    },
                    label = {
                        Text("Pago")
                    }
                )
            }
        }

        Text(
            text = if (selectedSection == AuctionSection.ACTIVE) {
                "Leilões ativos"
            } else {
                "Leilões encerrados"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        if (displayedAuctions.isEmpty()) {
            EmptyAuctionsMessage(
                text = when {
                    selectedSection == AuctionSection.ACTIVE ->
                        "Nenhum leilão ativo no momento."

                    endedFilter == EndedAuctionFilter.NOT_WON ->
                        "Nenhum leilão não ganho."

                    endedFilter ==
                            EndedAuctionFilter.PENDING_PAYMENT ->
                        "Nenhum leilão aguardando pagamento."

                    endedFilter == EndedAuctionFilter.PAID ->
                        "Nenhum leilão pago."

                    else ->
                        "Nenhum leilão encerrado."
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = displayedAuctions,
                    key = { auction -> auction.id }
                ) { auction ->
                    AuctionCard(
                        auction = auction,
                        currentTime = currentTime,
                        onClick = {
                            onAuctionClick(auction)
                        }
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )
                }
            }
        }

        Button(
            onClick = onAddAuction,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            contentPadding = ButtonDefaults.ContentPadding
        ) {
            Text(
                text = "Cadastrar novo leilão",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyAuctionsMessage(
    text: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Nada por aqui",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AuctionCard(
    auction: Auction,
    currentTime: Long,
    onClick: () -> Unit
) {
    val remainingTime = calculateRemainingTime(
        endTimeMillis = auction.endTimeMillis,
        currentTimeMillis = currentTime
    )

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = auction.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = platformBackgroundColor(auction.platform)
                ) {
                    Text(
                        text = platformBadge(auction.platform),
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = platformTextColor(auction.platform)
                    )
                }
            }

            StatusBadge(
                auction = auction,
                currentTime = currentTime
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                InfoLine(
                    label = "Encerramento",
                    value = formatAuctionDate(
                        auction.endTimeMillis
                    )
                )

                InfoLine(
                    label = "Lance inicial",
                    value = formatMoney(
                        auction.initialBidInCents
                    )
                )

                if (auction.endTimeMillis > currentTime) {
                    InfoLine(
                        label = "Tempo restante",
                        value = remainingTime
                    )
                } else {
                    auction.finalPriceInCents?.let { finalPrice ->
                        InfoLine(
                            label = "Valor final",
                            value = formatMoney(finalPrice)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    auction: Auction,
    currentTime: Long
) {
    val text = when {
        auction.endTimeMillis > currentTime ->
            "Em andamento"

        auction.status == AuctionStatus.NOT_WON ->
            "Não ganho"

        auction.status == AuctionStatus.WON_PENDING_PAYMENT ->
            "A pagar"

        auction.status == AuctionStatus.WON_PAID ->
            "Pago"

        else ->
            "Encerrado"
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun InfoLine(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun calculateRemainingTime(
    endTimeMillis: Long,
    currentTimeMillis: Long
): String {
    val remainingMillis =
        endTimeMillis - currentTimeMillis

    if (remainingMillis <= 0) {
        return "Encerrado"
    }

    val totalSeconds = remainingMillis / 1_000
    val days = totalSeconds / 86_400
    val hours = (totalSeconds % 86_400) / 3_600
    val minutes = (totalSeconds % 3_600) / 60
    val seconds = totalSeconds % 60

    return if (days > 0) {
        "${days}d ${hours}h ${minutes}min"
    } else {
        "${hours}h ${minutes}min ${seconds}s"
    }
}

private fun formatAuctionDate(
    value: Long
): String {
    return Instant.ofEpochMilli(value)
        .atZone(ZoneId.systemDefault())
        .format(cardDateFormatter)
}

private fun formatMoney(
    valueInCents: Long
): String {
    val value =
        BigDecimal.valueOf(valueInCents, 2)

    return NumberFormat
        .getCurrencyInstance(Locale("pt", "BR"))
        .format(value)
}

@Preview(showBackground = true)
@Composable
fun ActiveAuctionsScreenPreview() {
    LeilõesRetroGamesTheme {
        ActiveAuctionsScreen(
            auctions = listOf(
                Auction(
                    id = 1,
                    title = "Resident Evil 2",
                    platform = "PlayStation 2",
                    postUrl = "https://facebook.com",
                    endTimeMillis =
                        System.currentTimeMillis() + 3_600_000,
                    initialBidInCents = 500,
                    bidIncrementInCents = 500
                )
            ),
            onAuctionClick = {},
            onAddAuction = {}
        )
    }
}