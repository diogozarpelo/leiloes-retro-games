package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import br.com.diogozarpelao.leiloesretrogames.model.ItemCondition

@Composable
internal fun AuctionInformationCard(
    auction: Auction
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Informações do leilão",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight =
                    FontWeight.SemiBold
            )

            AuctionDetailLine(
                label = "Encerramento",
                value =
                    formatAuctionCardDate(
                        auction.endTimeMillis
                    )
            )

            AuctionDetailLine(
                label = "Lance inicial",
                value =
                    formatAuctionCurrency(
                        auction.initialBidInCents
                    )
            )

            AuctionDetailLine(
                label = "Múltiplo dos lances",
                value =
                    formatAuctionCurrency(
                        auction.bidIncrementInCents
                    )
            )

            auction.buyoutPriceInCents
                ?.let { buyoutPrice ->
                    AuctionDetailLine(
                        label = "Valor de arremate",
                        value =
                            formatAuctionCurrency(
                                buyoutPrice
                            )
                    )
                }

            auction.finalPriceInCents
                ?.let { finalPrice ->
                    AuctionDetailLine(
                        label = "Valor final",
                        value =
                            formatAuctionCurrency(
                                finalPrice
                            ),
                        highlight = true
                    )
                }
        }
    }
}

@Composable
internal fun AuctionExtraDetailsCard(
    auction: Auction
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Detalhes",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight =
                    FontWeight.SemiBold
            )

            AuctionDetailLine(
                label = "Conservação",
                value =
                    auction.condition
                        .toDetailsDisplayName()
            )

            AuctionDetailLine(
                label = "Alertas",
                value =
                    if (auction.alertsEnabled) {
                        "Ativados"
                    } else {
                        "Desativados"
                    }
            )
        }
    }
}

@Composable
internal fun AuctionResultCard(
    auction: Auction,
    onStatusChange: (AuctionStatus) -> Unit,
    onFinalPriceChange: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Resultado do leilão",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight =
                    FontWeight.SemiBold
            )

            AuctionResultActions(
                status = auction.status,
                onStatusChange =
                    onStatusChange,
                finalPriceInCents =
                    auction.finalPriceInCents,
                onFinalPriceChange =
                    onFinalPriceChange
            )
        }
    }
}

@Composable
internal fun AuctionNotesCard(
    notes: String
) {
    if (notes.isBlank()) {
        return
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Descrição e observações",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text = notes,
                style =
                    MaterialTheme.typography.bodyLarge,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

@Composable
internal fun AuctionDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
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
                onClick = onConfirm
            ) {
                Text(
                    text = "Excluir",
                    color =
                        MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun AuctionDetailLine(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )

        Text(
            text = value,
            style =
                MaterialTheme.typography.bodyMedium,
            fontWeight =
                if (highlight) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                },
            color =
                if (highlight) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
        )
    }
}

private fun ItemCondition.toDetailsDisplayName(): String {
    return when (this) {
        ItemCondition.EXCELLENT ->
            "Excelente"

        ItemCondition.GOOD ->
            "Bom"

        ItemCondition.AVERAGE ->
            "Médio"

        ItemCondition.POOR ->
            "Ruim"

        ItemCondition.VERY_POOR ->
            "Péssimo"

        ItemCondition.NOT_INFORMED ->
            "Não informado"
    }
}