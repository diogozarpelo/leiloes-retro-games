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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        colors =
            CardDefaults.cardColors(
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
        elevation =
            CardDefaults.cardElevation(
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
        colors =
            CardDefaults.cardColors(
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