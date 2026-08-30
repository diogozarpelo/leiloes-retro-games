package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import br.com.diogozarpelao.leiloesretrogames.model.ItemCondition
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val detailsDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")

@Composable
fun AuctionDetailsScreen(
    auction: Auction,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(
            onClick = onBack
        ) {
            Text("Voltar")
        }

        Text(
            text = auction.title,
            style = MaterialTheme.typography.headlineMedium
        )

        DetailItem(
            label = "Plataforma",
            value = auction.platform
        )

        DetailItem(
            label = "Encerramento",
            value = formatDateTime(auction.endTimeMillis)
        )

        DetailItem(
            label = "Lance inicial",
            value = formatMoney(auction.initialBidInCents)
        )

        DetailItem(
            label = "Múltiplo dos lances",
            value = formatMoney(auction.bidIncrementInCents)
        )

        auction.buyoutPriceInCents?.let { value ->
            DetailItem(
                label = "Valor de arremate",
                value = formatMoney(value)
            )
        }

        auction.finalPriceInCents?.let { value ->
            DetailItem(
                label = "Valor final",
                value = formatMoney(value)
            )
        }

        DetailItem(
            label = "Conservação",
            value = formatCondition(auction.condition)
        )

        DetailItem(
            label = "Status",
            value = formatStatus(auction.status)
        )

        DetailItem(
            label = "Alertas",
            value = if (auction.alertsEnabled) {
                "Ativados"
            } else {
                "Desativados"
            }
        )

        DetailItem(
            label = "Link da publicação",
            value = auction.postUrl
        )

        if (auction.notes.isNotBlank()) {
            DetailItem(
                label = "Observações",
                value = auction.notes
            )
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun formatDateTime(value: Long): String {
    return Instant.ofEpochMilli(value)
        .atZone(ZoneId.systemDefault())
        .format(detailsDateFormatter)
}

private fun formatMoney(valueInCents: Long): String {
    val value = BigDecimal.valueOf(valueInCents, 2)

    return NumberFormat
        .getCurrencyInstance(Locale("pt", "BR"))
        .format(value)
}

private fun formatCondition(condition: ItemCondition): String {
    return when (condition) {
        ItemCondition.EXCELLENT -> "Excelente"
        ItemCondition.GOOD -> "Bom"
        ItemCondition.AVERAGE -> "Médio"
        ItemCondition.POOR -> "Ruim"
        ItemCondition.VERY_POOR -> "Péssimo"
        ItemCondition.NOT_INFORMED -> "Não informado"
    }
}

private fun formatStatus(status: AuctionStatus): String {
    return when (status) {
        AuctionStatus.ACTIVE -> "Ativo"
        AuctionStatus.ENDED -> "Encerrado"
        AuctionStatus.NOT_WON -> "Não ganho"
        AuctionStatus.WON_PENDING_PAYMENT -> "Ganho — a pagar"
        AuctionStatus.WON_PAID -> "Ganho — pago"
    }
}

@Preview(showBackground = true)
@Composable
fun AuctionDetailsScreenPreview() {
    LeilõesRetroGamesTheme {
        AuctionDetailsScreen(
            auction = Auction(
                id = 1,
                title = "Resident Evil 2",
                platform = "PlayStation",
                postUrl = "https://facebook.com/teste",
                endTimeMillis = 1_800_000_000_000,
                notes = "Jogo original em bom estado.",
                initialBidInCents = 500,
                bidIncrementInCents = 500,
                buyoutPriceInCents = 10_000
            ),
            onBack = {}
        )
    }
}