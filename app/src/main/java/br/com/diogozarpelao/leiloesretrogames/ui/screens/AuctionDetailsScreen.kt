package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
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
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val uriHandler = LocalUriHandler.current

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

        DetailItem("Plataforma", auction.platform)
        DetailItem("Encerramento", formatDateTime(auction.endTimeMillis))
        DetailItem("Lance inicial", formatMoney(auction.initialBidInCents))
        DetailItem(
            "Múltiplo dos lances",
            formatMoney(auction.bidIncrementInCents)
        )

        auction.buyoutPriceInCents?.let {
            DetailItem("Valor de arremate", formatMoney(it))
        }

        auction.finalPriceInCents?.let {
            DetailItem("Valor final", formatMoney(it))
        }

        DetailItem("Conservação", formatCondition(auction.condition))
        DetailItem("Status", formatStatus(auction.status))

        DetailItem(
            "Alertas",
            if (auction.alertsEnabled) "Ativados" else "Desativados"
        )

        DetailItem("Link da publicação", auction.postUrl)

        if (auction.notes.isNotBlank()) {
            DetailItem("Observações", auction.notes)
        }

        Button(
            onClick = {
                runCatching {
                    uriHandler.openUri(auction.postUrl)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Abrir publicação")
        }

        OutlinedButton(
            onClick = {
                showDeleteDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Excluir leilão")
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Excluir leilão?")
            },
            text = {
                Text(
                    "Esta ação removerá o leilão permanentemente."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
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
                postUrl = "https://facebook.com",
                endTimeMillis = 1_800_000_000_000,
                notes = "Jogo original em bom estado.",
                initialBidInCents = 500,
                bidIncrementInCents = 500,
                buyoutPriceInCents = 10_000
            ),
            onBack = {},
            onDelete = {}
        )
    }
}