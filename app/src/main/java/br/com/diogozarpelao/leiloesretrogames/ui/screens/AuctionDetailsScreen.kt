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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme
import kotlinx.coroutines.delay

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

    val currentTime =
        rememberAuctionDetailsTime(
            endTimeMillis =
                auction.endTimeMillis
        )

    val isEnded =
        auction.endTimeMillis <= currentTime

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {
        TextButton(
            onClick = onBack
        ) {
            Text("Voltar")
        }

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
                text = auction.title,
                modifier =
                    Modifier.weight(1f),
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        AuctionStatusBadge(
            auction = auction,
            currentTime = currentTime
        )

        AuctionInformationCard(
            auction = auction
        )

        AuctionExtraDetailsCard(
            auction = auction
        )

        if (isEnded) {
            AuctionResultCard(
                auction = auction,
                onStatusChange =
                    onStatusChange,
                onFinalPriceChange =
                    onFinalPriceChange
            )
        }

        AuctionNotesCard(
            notes = auction.notes
        )

        Button(
            onClick = {
                runCatching {
                    uriHandler.openUri(
                        auction.postUrl
                    )
                }
            },
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Abrir publicação",
                fontWeight =
                    FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = onEdit,
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Editar leilão",
                fontWeight =
                    FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = {
                showDeleteDialog = true
            },
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(14.dp),
            colors =
                ButtonDefaults
                    .outlinedButtonColors(
                        contentColor =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
        ) {
            Text(
                text = "Excluir leilão",
                fontWeight =
                    FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = onBack,
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Voltar",
                fontWeight =
                    FontWeight.SemiBold
            )
        }
    }

    if (showDeleteDialog) {
        AuctionDeleteDialog(
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
}

@Composable
private fun rememberAuctionDetailsTime(
    endTimeMillis: Long
): Long {
    val currentTime by produceState(
        initialValue =
            System.currentTimeMillis(),
        key1 = endTimeMillis
    ) {
        if (value < endTimeMillis) {
            val waitTime =
                (
                    endTimeMillis -
                        value +
                        50L
                    )
                    .coerceAtLeast(1L)

            delay(waitTime)

            value =
                System.currentTimeMillis()
        }
    }

    return currentTime
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
                postUrl =
                    "https://facebook.com",
                endTimeMillis =
                    1_700_000_000_000,
                notes =
                    "Jogo original em bom estado.",
                initialBidInCents = 500,
                bidIncrementInCents = 500,
                buyoutPriceInCents =
                    10_000,
                status =
                    AuctionStatus.ENDED
            ),
            onBack = {},
            onEdit = {},
            onStatusChange = {},
            onDelete = {},
            onFinalPriceChange = {}
        )
    }
}