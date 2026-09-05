package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme
import java.math.BigDecimal

@Composable
fun AuctionResultActions(
    status: AuctionStatus,
    onStatusChange: (AuctionStatus) -> Unit,
    finalPriceInCents: Long? = null,
    onFinalPriceChange: (Long) -> Unit = {}
) {
    var finalPriceText by rememberSaveable(finalPriceInCents) {
        mutableStateOf(
            finalPriceInCents?.let {
                formatFinalPriceForForm(it)
            }.orEmpty()
        )
    }

    var finalPriceSaved by rememberSaveable {
        mutableStateOf(finalPriceInCents != null)
    }

    val parsedFinalPrice =
        parseFinalPriceToCents(finalPriceText)

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

        if (
            status == AuctionStatus.WON_PENDING_PAYMENT ||
            status == AuctionStatus.WON_PAID
        ) {
            OutlinedTextField(
                value = finalPriceText,
                onValueChange = {
                    finalPriceText = it
                    finalPriceSaved = false
                },
                label = {
                    Text("Valor final")
                },
                placeholder = {
                    Text("50,00")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    onFinalPriceChange(
                        requireNotNull(parsedFinalPrice)
                    )

                    finalPriceSaved = true
                },
                enabled =
                    parsedFinalPrice != null &&
                            !finalPriceSaved,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (finalPriceSaved) {
                        "Valor salvo"
                    } else {
                        "Salvar valor final"
                    }
                )
            }

            if (finalPriceSaved) {
                Text(
                    text = "Valor final salvo.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (status == AuctionStatus.WON_PENDING_PAYMENT) {
            Button(
                onClick = {
                    onStatusChange(AuctionStatus.WON_PAID)
                },
                modifier = Modifier.fillMaxWidth()
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

private fun parseFinalPriceToCents(
    value: String
): Long? {
    val normalizedValue = value
        .trim()
        .replace(",", ".")

    val decimalValue = normalizedValue.toBigDecimalOrNull()
        ?: return null

    if (
        decimalValue < BigDecimal.ZERO ||
        decimalValue.scale() > 2
    ) {
        return null
    }

    return runCatching {
        decimalValue
            .movePointRight(2)
            .longValueExact()
    }.getOrNull()
}

private fun formatFinalPriceForForm(
    valueInCents: Long
): String {
    return BigDecimal.valueOf(valueInCents, 2)
        .stripTrailingZeros()
        .toPlainString()
        .replace(".", ",")
}

@Preview(showBackground = true)
@Composable
fun AuctionResultActionsPreview() {
    LeilõesRetroGamesTheme {
        AuctionResultActions(
            status = AuctionStatus.WON_PENDING_PAYMENT,
            finalPriceInCents = 5_000,
            onStatusChange = {},
            onFinalPriceChange = {}
        )
    }
}