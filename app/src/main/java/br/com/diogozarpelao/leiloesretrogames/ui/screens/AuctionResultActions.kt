package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme

@Composable
fun AuctionResultActions(
    status: AuctionStatus,
    onStatusChange: (AuctionStatus) -> Unit,
    finalPriceInCents: Long? = null,
    onFinalPriceChange: (Long) -> Unit = {}
) {
    var finalPriceText by rememberSaveable(
        finalPriceInCents
    ) {
        mutableStateOf(
            finalPriceInCents
                ?.let(
                    ::formatMoneyForForm
                )
                .orEmpty()
        )
    }

    var finalPriceSaved by rememberSaveable(
        finalPriceInCents
    ) {
        mutableStateOf(
            finalPriceInCents != null
        )
    }

    val parsedFinalPrice =
        parseMoneyToCents(
            finalPriceText
        )

    val isWon =
        status ==
            AuctionStatus.WON_PENDING_PAYMENT ||
            status ==
            AuctionStatus.WON_PAID

    Column(
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Resultado",
            style =
                MaterialTheme
                    .typography
                    .titleMedium,
            fontWeight =
                FontWeight.SemiBold
        )

        AuctionResultSelector(
            status = status,
            onStatusChange =
                onStatusChange
        )

        if (isWon) {
            FinalPriceSection(
                value =
                    finalPriceText,
                onValueChange = {
                    finalPriceText = it
                    finalPriceSaved = false
                },
                parsedValueInCents =
                    parsedFinalPrice,
                saved =
                    finalPriceSaved,
                onSave = {
                    val finalPrice =
                        requireNotNull(
                            parsedFinalPrice
                        )

                    onFinalPriceChange(
                        finalPrice
                    )

                    finalPriceSaved = true
                }
            )
        }

        if (
            status ==
                AuctionStatus
                    .WON_PENDING_PAYMENT
        ) {
            PendingPaymentSection(
                onMarkAsPaid = {
                    onStatusChange(
                        AuctionStatus.WON_PAID
                    )
                }
            )
        }

        if (
            status ==
                AuctionStatus.WON_PAID
        ) {
            PaidSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuctionResultActionsPreview() {
    LeilõesRetroGamesTheme {
        AuctionResultActions(
            status =
                AuctionStatus
                    .WON_PENDING_PAYMENT,
            finalPriceInCents =
                5_000,
            onStatusChange = {},
            onFinalPriceChange = {}
        )
    }
}