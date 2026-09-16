package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import kotlinx.coroutines.delay

@Composable
internal fun AuctionPlatformBadge(
    platform: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color =
            platformBackgroundColor(
                platform
            )
    ) {
        Text(
            text =
                platformBadge(
                    platform
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
                    platform
                )
        )
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
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(18.dp),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {
        Column(
            modifier =
                Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {
                AuctionPlatformBadge(
                    platform =
                        auction.platform
                )

                Text(
                    text =
                        auction.title,
                    modifier =
                        Modifier.weight(1f),
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
                    label =
                        "Encerramento",
                    value =
                        formatAuctionCardDate(
                            auction.endTimeMillis
                        )
                )

                AuctionInfoLine(
                    label =
                        "Lance inicial",
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
                        label =
                            "Tempo restante",
                        value =
                            remainingTime
                    )
                } else {
                    auction.finalPriceInCents
                        ?.let { finalPrice ->
                            AuctionInfoLine(
                                label =
                                    "Valor final",
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
    val currentTime by
        produceState(
            initialValue =
                System.currentTimeMillis(),
            key1 =
                endTimeMillis
        ) {
            while (
                value <
                    endTimeMillis
            ) {
                delay(1_000)

                value =
                    System.currentTimeMillis()
            }
        }

    return currentTime
}

@Composable
internal fun AuctionStatusBadge(
    auction: Auction,
    currentTime: Long
) {
    val isActive =
        auction.endTimeMillis >
            currentTime

    val resultPending =
        isResultPending(
            auction
        )

    val text =
        when {
            isActive ->
                "Em andamento"

            resultPending ->
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
            isActive ->
                MaterialTheme
                    .colorScheme
                    .primaryContainer

            resultPending ->
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
            isActive ->
                MaterialTheme
                    .colorScheme
                    .onPrimaryContainer

            resultPending ->
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
        shape =
            RoundedCornerShape(50),
        color =
            backgroundColor
    ) {
        Text(
            text = text,
            modifier =
                Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                ),
            style =
                MaterialTheme
                    .typography
                    .labelLarge,
            fontWeight =
                FontWeight.SemiBold,
            color =
                textColor
        )
    }
}

@Composable
private fun AuctionInfoLine(
    label: String,
    value: String
) {
    Row(
        modifier =
            Modifier.fillMaxWidth(),
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