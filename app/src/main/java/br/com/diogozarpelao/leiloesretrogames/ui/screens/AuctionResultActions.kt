package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

    var finalPriceSaved by rememberSaveable(finalPriceInCents) {
        mutableStateOf(finalPriceInCents != null)
    }

    val parsedFinalPrice =
        parseFinalPriceToCents(finalPriceText)

    val isWon =
        status == AuctionStatus.WON_PENDING_PAYMENT ||
                status == AuctionStatus.WON_PAID

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Resultado",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected =
                    status == AuctionStatus.NOT_WON,
                onClick = {
                    onStatusChange(
                        AuctionStatus.NOT_WON
                    )
                },
                label = {
                    Text(
                        text = "Não ganho",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor =
                        MaterialTheme.colorScheme.errorContainer,
                    selectedLabelColor =
                        MaterialTheme.colorScheme.onErrorContainer
                )
            )

            FilterChip(
                selected = isWon,
                onClick = {
                    onStatusChange(
                        AuctionStatus.WON_PENDING_PAYMENT
                    )
                },
                label = {
                    Text(
                        text = "Ganho",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor =
                        Color(0xFF14532D),
                    selectedLabelColor =
                        Color(0xFFBBF7D0)
                )
            )
        }

        if (isWon) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Valor final do leilão",
                        style =
                            MaterialTheme.typography.titleSmall,
                        fontWeight =
                            FontWeight.SemiBold
                    )

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
                        prefix = {
                            Text("R$ ")
                        },
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Decimal
                            ),
                        modifier =
                            Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            onFinalPriceChange(
                                requireNotNull(
                                    parsedFinalPrice
                                )
                            )

                            finalPriceSaved = true
                        },
                        enabled =
                            parsedFinalPrice != null &&
                                    !finalPriceSaved,
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text =
                                if (finalPriceSaved) {
                                    "Valor salvo"
                                } else {
                                    "Salvar valor final"
                                },
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }

                    if (finalPriceSaved) {
                        Surface(
                            shape =
                                RoundedCornerShape(10.dp),
                            color = Color(0xFF14532D)
                        ) {
                            Text(
                                text =
                                    "Valor final salvo.",
                                modifier =
                                    Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 8.dp
                                    ),
                                color =
                                    Color(0xFFBBF7D0),
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodyMedium,
                                fontWeight =
                                    FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        if (
            status ==
            AuctionStatus.WON_PENDING_PAYMENT
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF78350F)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Pagamento pendente",
                        color = Color(0xFFFDE68A),
                        style =
                            MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text =
                            "Marque como pago quando o pagamento for concluído.",
                        color = Color(0xFFFDE68A),
                        style =
                            MaterialTheme.typography.bodyMedium
                    )

                    Button(
                        onClick = {
                            onStatusChange(
                                AuctionStatus.WON_PAID
                            )
                        },
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(12.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF22C55E),
                                contentColor =
                                    Color.Black
                            )
                    ) {
                        Text(
                            text = "Marcar como pago",
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (
            status ==
            AuctionStatus.WON_PAID
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF14532D)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Pagamento concluído",
                        color = Color(0xFFBBF7D0),
                        style =
                            MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text =
                            "Este leilão está marcado como pago.",
                        color = Color(0xFFBBF7D0),
                        style =
                            MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private fun parseFinalPriceToCents(
    value: String
): Long? {
    val normalizedValue =
        value
            .trim()
            .replace(",", ".")

    val decimalValue =
        normalizedValue.toBigDecimalOrNull()
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
    return BigDecimal
        .valueOf(valueInCents, 2)
        .stripTrailingZeros()
        .toPlainString()
        .replace(".", ",")
}

@Preview(showBackground = true)
@Composable
fun AuctionResultActionsPreview() {
    LeilõesRetroGamesTheme {
        AuctionResultActions(
            status =
                AuctionStatus.WON_PENDING_PAYMENT,
            finalPriceInCents = 5_000,
            onStatusChange = {},
            onFinalPriceChange = {}
        )
    }
}