package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import kotlinx.coroutines.delay

@Composable
internal fun EndedAuctionFilters(
    selectedFilter: EndedAuctionFilter,
    onFilterSelected: (EndedAuctionFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {
        FilterChip(
            selected =
                selectedFilter ==
                    EndedAuctionFilter.ALL,
            onClick = {
                onFilterSelected(
                    EndedAuctionFilter.ALL
                )
            },
            modifier = Modifier.weight(0.8f),
            label = {
                AuctionFilterLabel("Todos")
            }
        )

        FilterChip(
            selected =
                selectedFilter ==
                    EndedAuctionFilter.PENDING,
            onClick = {
                onFilterSelected(
                    EndedAuctionFilter.PENDING
                )
            },
            modifier = Modifier.weight(1.2f),
            label = {
                AuctionFilterLabel("Pendentes")
            }
        )

        FilterChip(
            selected =
                selectedFilter ==
                    EndedAuctionFilter.WON,
            onClick = {
                onFilterSelected(
                    EndedAuctionFilter.WON
                )
            },
            modifier = Modifier.weight(1f),
            label = {
                AuctionFilterLabel("Ganhos")
            }
        )

        FilterChip(
            selected =
                selectedFilter ==
                    EndedAuctionFilter.NOT_WON,
            onClick = {
                onFilterSelected(
                    EndedAuctionFilter.NOT_WON
                )
            },
            modifier = Modifier.weight(1.3f),
            label = {
                AuctionFilterLabel(
                    "Não ganhos"
                )
            }
        )
    }
}

@Composable
internal fun WonAuctionFilters(
    selectedFilter: WonAuctionFilter,
    onFilterSelected: (WonAuctionFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected =
                selectedFilter ==
                    WonAuctionFilter.ALL,
            onClick = {
                onFilterSelected(
                    WonAuctionFilter.ALL
                )
            },
            modifier = Modifier.weight(1f),
            label = {
                Text("Todos")
            }
        )

        FilterChip(
            selected =
                selectedFilter ==
                    WonAuctionFilter
                        .PENDING_PAYMENT,
            onClick = {
                onFilterSelected(
                    WonAuctionFilter
                        .PENDING_PAYMENT
                )
            },
            modifier = Modifier.weight(1f),
            label = {
                Text("A pagar")
            }
        )

        FilterChip(
            selected =
                selectedFilter ==
                    WonAuctionFilter.PAID,
            onClick = {
                onFilterSelected(
                    WonAuctionFilter.PAID
                )
            },
            modifier = Modifier.weight(1f),
            label = {
                Text("Pagos")
            }
        )
    }
}

@Composable
private fun AuctionFilterLabel(
    text: String
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Clip,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
internal fun AuctionSummaryCard(
    title: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected) {
                    MaterialTheme
                        .colorScheme
                        .primaryContainer
                } else {
                    MaterialTheme
                        .colorScheme
                        .surface
                }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation =
                if (selected) {
                    4.dp
                } else {
                    2.dp
                }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = value,
                style =
                    MaterialTheme
                        .typography
                        .headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = title,
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

@Composable
internal fun EmptyAuctionsMessage(
    text: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme
                    .colorScheme
                    .surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Nada por aqui",
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text = text,
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

@Composable
internal fun AuctionCard(
    auction: Auction,
    onClick: () -> Unit
) {
    val currentTime =
        rememberAuctionCardTime(
            endTimeMillis =
                auction.endTimeMillis
        )

    val remainingTime =
        calculateAuctionRemainingTime(
            endTimeMillis =
                auction.endTimeMillis,
            currentTimeMillis =
                currentTime
        )

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape =
                        RoundedCornerShape(8.dp),
                    color =
                        platformBackgroundColor(
                            auction.platform
                        )
                ) {
                    Text(
                        text =
                            platformBadge(
                                auction.platform
                            ),
                        modifier =
                            Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            ),
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            platformTextColor(
                                auction.platform
                            )
                    )
                }

                Text(
                    text = auction.title,
                    modifier = Modifier.weight(1f),
                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            AuctionStatusBadge(
                auction = auction,
                currentTime = currentTime
            )

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(5.dp)
            ) {
                AuctionInfoLine(
                    label = "Encerramento",
                    value =
                        formatAuctionCardDate(
                            auction.endTimeMillis
                        )
                )

                AuctionInfoLine(
                    label = "Lance inicial",
                    value =
                        formatAuctionCurrency(
                            auction.initialBidInCents
                        )
                )

                if (
                    auction.endTimeMillis >
                        currentTime
                ) {
                    AuctionInfoLine(
                        label = "Tempo restante",
                        value = remainingTime
                    )
                } else {
                    auction.finalPriceInCents
                        ?.let { finalPrice ->
                            AuctionInfoLine(
                                label = "Valor final",
                                value =
                                    formatAuctionCurrency(
                                        finalPrice
                                    )
                            )
                        }
                }
            }
        }
    }
}

@Composable
private fun rememberAuctionCardTime(
    endTimeMillis: Long
): Long {
    val currentTime by produceState(
        initialValue =
            System.currentTimeMillis(),
        key1 = endTimeMillis
    ) {
        while (value < endTimeMillis) {
            delay(1_000)

            value =
                System.currentTimeMillis()
        }
    }

    return currentTime
}

@Composable
private fun AuctionStatusBadge(
    auction: Auction,
    currentTime: Long
) {
    val text =
        when {
            auction.endTimeMillis >
                currentTime ->
                "Em andamento"

            isResultPending(auction) ->
                "Resultado pendente"

            auction.status ==
                AuctionStatus.NOT_WON ->
                "Não ganho"

            auction.status ==
                AuctionStatus
                    .WON_PENDING_PAYMENT ->
                "A pagar"

            auction.status ==
                AuctionStatus.WON_PAID ->
                "Pago"

            else ->
                "Encerrado"
        }

    val backgroundColor =
        when {
            auction.endTimeMillis >
                currentTime ->
                MaterialTheme
                    .colorScheme
                    .primaryContainer

            isResultPending(auction) ->
                Color(0xFF7C2D12)

            auction.status ==
                AuctionStatus.NOT_WON ->
                MaterialTheme
                    .colorScheme
                    .errorContainer

            auction.status ==
                AuctionStatus
                    .WON_PENDING_PAYMENT ->
                Color(0xFF78350F)

            auction.status ==
                AuctionStatus.WON_PAID ->
                Color(0xFF14532D)

            else ->
                MaterialTheme
                    .colorScheme
                    .secondaryContainer
        }

    val textColor =
        when {
            auction.endTimeMillis >
                currentTime ->
                MaterialTheme
                    .colorScheme
                    .onPrimaryContainer

            isResultPending(auction) ->
                Color(0xFFFED7AA)

            auction.status ==
                AuctionStatus.NOT_WON ->
                MaterialTheme
                    .colorScheme
                    .onErrorContainer

            auction.status ==
                AuctionStatus
                    .WON_PENDING_PAYMENT ->
                Color(0xFFFDE68A)

            auction.status ==
                AuctionStatus.WON_PAID ->
                Color(0xFFBBF7D0)

            else ->
                MaterialTheme
                    .colorScheme
                    .onSecondaryContainer
        }

    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
            style =
                MaterialTheme
                    .typography
                    .labelLarge,
            fontWeight =
                FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
private fun AuctionInfoLine(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style =
                MaterialTheme
                    .typography
                    .bodyMedium,
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )

        Text(
            text = value,
            style =
                MaterialTheme
                    .typography
                    .bodyMedium,
            fontWeight =
                FontWeight.Medium
        )
    }
}