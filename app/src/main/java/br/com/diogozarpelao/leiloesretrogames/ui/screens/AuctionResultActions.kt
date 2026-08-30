package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme

@Composable
fun AuctionResultActions(
    status: AuctionStatus,
    onStatusChange: (AuctionStatus) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Resultado",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = status == AuctionStatus.NOT_WON,
                onClick = {
                    onStatusChange(AuctionStatus.NOT_WON)
                },
                label = {
                    Text("Não ganho")
                }
            )

            FilterChip(
                selected =
                    status == AuctionStatus.WON_PENDING_PAYMENT ||
                            status == AuctionStatus.WON_PAID,
                onClick = {
                    onStatusChange(
                        AuctionStatus.WON_PENDING_PAYMENT
                    )
                },
                label = {
                    Text("Ganho")
                }
            )
        }

        if (status == AuctionStatus.WON_PENDING_PAYMENT) {
            Button(
                onClick = {
                    onStatusChange(AuctionStatus.WON_PAID)
                }
            ) {
                Text("Marcar como pago")
            }
        }

        if (status == AuctionStatus.WON_PAID) {
            Text(
                text = "Pagamento concluído",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuctionResultActionsPreview() {
    LeilõesRetroGamesTheme {
        AuctionResultActions(
            status = AuctionStatus.WON_PENDING_PAYMENT,
            onStatusChange = {}
        )
    }
}