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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus

@Composable
internal fun AuctionResultSelector(
    status: AuctionStatus,
    onStatusChange: (AuctionStatus) -> Unit
) {
    val isWon =
        status == AuctionStatus.WON_PENDING_PAYMENT ||
            status == AuctionStatus.WON_PAID

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
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
                    fontWeight =
                        FontWeight.SemiBold
                )
            },
            colors =
                FilterChipDefaults
                    .filterChipColors(
                        selectedContainerColor =
                            MaterialTheme
                                .colorScheme
                                .errorContainer,
                        selectedLabelColor =
                            MaterialTheme
                                .colorScheme
                                .onErrorContainer
                    )
        )

        FilterChip(
            selected = isWon,
            onClick = {
                onStatusChange(
                    AuctionStatus
                        .WON_PENDING_PAYMENT
                )
            },
            label = {
                Text(
                    text = "Ganho",
                    fontWeight =
                        FontWeight.SemiBold
                )
            },
            colors =
                FilterChipDefaults
                    .filterChipColors(
                        selectedContainerColor =
                            Color(0xFF14532D),
                        selectedLabelColor =
                            Color(0xFFBBF7D0)
                    )
        )
    }
}

@Composable
internal fun FinalPriceSection(
    value: String,
    onValueChange: (String) -> Unit,
    parsedValueInCents: Long?,
    saved: Boolean,
    onSave: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color =
            MaterialTheme
                .colorScheme
                .surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Valor final do leilão",
                style =
                    MaterialTheme
                        .typography
                        .titleSmall,
                fontWeight =
                    FontWeight.SemiBold
            )

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
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
                onClick = onSave,
                enabled =
                    parsedValueInCents != null &&
                        !saved,
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(12.dp)
            ) {
                Text(
                    text =
                        if (saved) {
                            "Valor salvo"
                        } else {
                            "Salvar valor final"
                        },
                    fontWeight =
                        FontWeight.SemiBold
                )
            }

            if (saved) {
                Surface(
                    shape =
                        RoundedCornerShape(10.dp),
                    color =
                        Color(0xFF14532D)
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

@Composable
internal fun PendingPaymentSection(
    onMarkAsPaid: () -> Unit
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
                    MaterialTheme
                        .typography
                        .titleSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text =
                    "Marque como pago quando o pagamento for concluído.",
                color = Color(0xFFFDE68A),
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
            )

            Button(
                onClick = onMarkAsPaid,
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(12.dp),
                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                Color(0xFF22C55E),
                            contentColor =
                                Color.Black
                        )
            ) {
                Text(
                    text = "Marcar como pago",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
internal fun PaidSection() {
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
                    MaterialTheme
                        .typography
                        .titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text =
                    "Este leilão está marcado como pago.",
                color = Color(0xFFBBF7D0),
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
            )
        }
    }
}