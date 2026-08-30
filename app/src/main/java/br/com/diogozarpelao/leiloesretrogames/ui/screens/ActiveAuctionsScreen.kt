package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme
import kotlinx.coroutines.delay

private enum class AuctionSection {
    ACTIVE,
    ENDED
}

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

    val currentTime by produceState(
        initialValue = System.currentTimeMillis()
    ) {
        while (true) {
            value = System.currentTimeMillis()
            delay(1_000)
        }
    }

    val displayedAuctions = auctions.filter { auction ->
        when (selectedSection) {
            AuctionSection.ACTIVE -> {
                auction.endTimeMillis > currentTime
            }

            AuctionSection.ENDED -> {
                auction.endTimeMillis <= currentTime
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Leilões",
            style = MaterialTheme.typography.headlineMedium
        )

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

        if (displayedAuctions.isEmpty()) {
            Text(
                text = if (
                    selectedSection == AuctionSection.ACTIVE
                ) {
                    "Nenhum leilão ativo."
                } else {
                    "Nenhum leilão encerrado."
                },
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
            }
        }

        Button(
            onClick = onAddAuction
        ) {
            Text("Cadastrar leilão")
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = auction.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = auction.platform,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Tempo restante: $remainingTime",
                style = MaterialTheme.typography.bodyMedium
            )
        }
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

@Preview(showBackground = true)
@Composable
fun ActiveAuctionsScreenPreview() {
    LeilõesRetroGamesTheme {
        ActiveAuctionsScreen(
            auctions = listOf(
                Auction(
                    id = 1,
                    title = "Resident Evil 2",
                    platform = "PlayStation",
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