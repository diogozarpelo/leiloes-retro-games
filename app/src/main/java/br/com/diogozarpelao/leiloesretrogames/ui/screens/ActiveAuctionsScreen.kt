package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme

@Composable
fun ActiveAuctionsScreen(
    auctions: List<Auction>,
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
                    Text(
                        text = auction.title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Button(
            onClick = onAddAuction
        ) {
            Text(text = "Cadastrar leilão")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActiveAuctionsScreenPreview() {
    LeilõesRetroGamesTheme {
        ActiveAuctionsScreen(
            auctions = emptyList(),
            onAddAuction = {}
        )
    }
}