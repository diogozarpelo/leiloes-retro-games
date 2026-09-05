package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
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
    onEdit: () -> Unit,
    onStatusChange: (AuctionStatus) -> Unit,
    onFinalPriceChange: (Long) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val uriHandler = LocalUriHandler.current
    val isEnded =
        auction.endTimeMillis <= System.currentTimeMillis()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TextButton(
            onClick = onBack
        ) {
            Text("Voltar")
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = auction.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlatformBadge(
                    platform = auction.platform
                )

                AuctionStatusBadge(
                    auction = auction,
                    isEnded = isEnded
                )
            }
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
                    Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Informações do leilão",
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                DetailLine(
                    label = "Encerramento",
                    value =
                        formatDateTime(
                            auction.endTimeMillis
                        )
                )

                DetailLine(
                    label = "Lance inicial",
                    value =
                        formatMoney(
                            auction.initialBidInCents
                        )
                )

                DetailLine(
                    label = "Múltiplo dos lances",
                    value =
                        formatMoney(
                            auction.bidIncrementInCents
                        )
                )

                auction.buyoutPriceInCents?.let {
                    DetailLine(
                        label = "Valor de arremate",
                        value = formatMoney(it)
                    )
                }

                auction.finalPriceInCents?.let {
                    DetailLine(
                        label = "Valor final",
                        value = formatMoney(it),
                        highlight = true
                    )
                }
            }
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
                    Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Detalhes",
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                DetailLine(
                    label = "Conservação",
                    value =
                        formatCondition(
                            auction.condition
                        )
                )

                DetailLine(
                    label = "Status",
                    value =
                        formatStatus(
                            auction.status
                        )
                )

                DetailLine(
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

        if (isEnded) {
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
                        fontWeight = FontWeight.SemiBold
                    )

                    AuctionResultActions(
                        status = auction.status,
                        onStatusChange = onStatusChange,
                        finalPriceInCents =
                            auction.finalPriceInCents,
                        onFinalPriceChange =
                            onFinalPriceChange
                    )
                }
            }
        }

        if (auction.notes.isNotBlank()) {
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
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = auction.notes,
                        style =
                            MaterialTheme.typography.bodyLarge,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Button(
            onClick = {
                runCatching {
                    uriHandler.openUri(
                        auction.postUrl
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Abrir publicação",
                fontWeight = FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = onEdit,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Editar leilão",
                fontWeight = FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = {
                showDeleteDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors =
                ButtonDefaults.outlinedButtonColors(
                    contentColor =
                        MaterialTheme.colorScheme.error
                )
        ) {
            Text(
                text = "Excluir leilão",
                fontWeight = FontWeight.SemiBold
            )
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
                    Text(
                        text = "Excluir",
                        color =
                            MaterialTheme.colorScheme.error
                    )
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
private fun PlatformBadge(
    platform: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = platformBackgroundColor(platform)
    ) {
        Text(
            text = platformBadge(platform),
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            style =
                MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = platformTextColor(platform)
        )
    }
}

@Composable
private fun AuctionStatusBadge(
    auction: Auction,
    isEnded: Boolean
) {
    val text = when {
        !isEnded ->
            "Em andamento"

        auction.status ==
                AuctionStatus.NOT_WON ->
            "Não ganho"

        auction.status ==
                AuctionStatus.WON_PENDING_PAYMENT ->
            "A pagar"

        auction.status ==
                AuctionStatus.WON_PAID ->
            "Pago"

        else ->
            "Encerrado"
    }

    val backgroundColor =
        when {
            !isEnded ->
                MaterialTheme.colorScheme.primaryContainer

            auction.status ==
                    AuctionStatus.NOT_WON ->
                MaterialTheme.colorScheme.errorContainer

            auction.status ==
                    AuctionStatus.WON_PENDING_PAYMENT ->
                Color(0xFF78350F)

            auction.status ==
                    AuctionStatus.WON_PAID ->
                Color(0xFF14532D)

            else ->
                MaterialTheme.colorScheme.secondaryContainer
        }

    val textColor =
        when {
            !isEnded ->
                MaterialTheme.colorScheme.onPrimaryContainer

            auction.status ==
                    AuctionStatus.NOT_WON ->
                MaterialTheme.colorScheme.onErrorContainer

            auction.status ==
                    AuctionStatus.WON_PENDING_PAYMENT ->
                Color(0xFFFDE68A)

            auction.status ==
                    AuctionStatus.WON_PAID ->
                Color(0xFFBBF7D0)

            else ->
                MaterialTheme.colorScheme.onSecondaryContainer
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
                MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
private fun DetailLine(
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
                MaterialTheme.colorScheme.onSurfaceVariant
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

private fun formatDateTime(
    value: Long
): String {
    return Instant.ofEpochMilli(value)
        .atZone(ZoneId.systemDefault())
        .format(detailsDateFormatter)
}

private fun formatMoney(
    valueInCents: Long
): String {
    val value =
        BigDecimal.valueOf(valueInCents, 2)

    return NumberFormat
        .getCurrencyInstance(
            Locale("pt", "BR")
        )
        .format(value)
}

private fun formatCondition(
    condition: ItemCondition
): String {
    return when (condition) {
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

private fun formatStatus(
    status: AuctionStatus
): String {
    return when (status) {
        AuctionStatus.ACTIVE ->
            "Ativo"

        AuctionStatus.ENDED ->
            "Encerrado"

        AuctionStatus.NOT_WON ->
            "Não ganho"

        AuctionStatus.WON_PENDING_PAYMENT ->
            "Ganho — a pagar"

        AuctionStatus.WON_PAID ->
            "Ganho — pago"
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
                platform = "PlayStation 2",
                postUrl = "https://facebook.com",
                endTimeMillis =
                    1_700_000_000_000,
                notes =
                    "Jogo original em bom estado.",
                initialBidInCents = 500,
                bidIncrementInCents = 500,
                buyoutPriceInCents = 10_000,
                finalPriceInCents = 8_500,
                status =
                    AuctionStatus.WON_PENDING_PAYMENT
            ),
            onBack = {},
            onEdit = {},
            onStatusChange = {},
            onDelete = {},
            onFinalPriceChange = {}
        )
    }
}