package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme
import kotlinx.coroutines.delay

@Composable
fun ActiveAuctionsScreen(
    auctions: List<Auction>,
    onAuctionClick: (Auction) -> Unit,
    onAddAuction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Leilões ativos",
            style = MaterialTheme.typography.headlineMedium
        )

        if (auctions.isEmpty()) {
            Text(
                text = "Nenhum leilão cadastrado.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = auctions,
                    key = { auction -> auction.id }
                ) { auction ->
                    AuctionCard(
                        auction = auction,
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
    onClick: () -> Unit
) {
    val remainingTime by produceState(
        initialValue = calculateRemainingTime(
            auction.endTimeMillis
        ),
        key1 = auction.endTimeMillis
    ) {
        while (true) {
            value = calculateRemainingTime(
                auction.endTimeMillis
            )

            delay(1_000)
        }
    }

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
    endTimeMillis: Long
): String {
    val remainingMillis =
        endTimeMillis - System.currentTimeMillis()

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