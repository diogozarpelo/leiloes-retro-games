package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import br.com.diogozarpelao.leiloesretrogames.model.ItemCondition
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.OutlinedButton

private val dateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun AddAuctionScreen(
    onSave: (Auction) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    auctionToEdit: Auction? = null
) {
    val stateKey = auctionToEdit?.id ?: 0L

    var title by rememberSaveable(stateKey) {
        mutableStateOf(auctionToEdit?.title.orEmpty())
    }

    var platform by rememberSaveable(stateKey) {
        mutableStateOf(auctionToEdit?.platform.orEmpty())
    }

    var postUrl by rememberSaveable(stateKey) {
        mutableStateOf(auctionToEdit?.postUrl.orEmpty())
    }

    var endDate by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.let {
                formatDateForForm(it.endTimeMillis)
            }.orEmpty()
        )
    }

    var endTime by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.let {
                formatTimeForForm(it.endTimeMillis)
            }.orEmpty()
        )
    }

    var initialBid by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.let {
                formatMoneyForForm(it.initialBidInCents)
            }.orEmpty()
        )
    }

    var bidIncrement by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.let {
                formatMoneyForForm(it.bidIncrementInCents)
            }.orEmpty()
        )
    }

    var buyoutPrice by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.buyoutPriceInCents?.let {
                formatMoneyForForm(it)
            }.orEmpty()
        )
    }

    var notes by rememberSaveable(stateKey) {
        mutableStateOf(auctionToEdit?.notes.orEmpty())
    }

    var condition by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.condition ?: ItemCondition.NOT_INFORMED
        )
    }

    var conditionMenuExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    val endTimeMillis = parseEndTimeMillis(endDate, endTime)
    val initialBidInCents = parseMoneyToCents(initialBid)
    val bidIncrementInCents = parseMoneyToCents(bidIncrement)

    val buyoutPriceInCents = if (buyoutPrice.isBlank()) {
        null
    } else {
        parseMoneyToCents(buyoutPrice)
    }

    val buyoutIsValid =
        buyoutPrice.isBlank() || buyoutPriceInCents != null

    val endTimeIsValid =
        endTimeMillis != null &&
                endTimeMillis > System.currentTimeMillis()

    val formIsValid =
        title.isNotBlank() &&
                platform.isNotBlank() &&
                postUrl.isNotBlank() &&
                endTimeIsValid &&
                initialBidInCents != null &&
                bidIncrementInCents != null &&
                buyoutIsValid

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (auctionToEdit == null) {
                "Cadastrar leilão"
            } else {
                "Editar leilão"
            },
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Produto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = platform,
            onValueChange = { platform = it },
            label = { Text("Plataforma") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = postUrl,
            onValueChange = { postUrl = it },
            label = { Text("Link da publicação") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri
            ),
            singleLine = true
        )

        Column {
            Text(
                text = "Estado de conservação",
                style = MaterialTheme.typography.bodyMedium
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        conditionMenuExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(condition.toDisplayName())
                }

                DropdownMenu(
                    expanded = conditionMenuExpanded,
                    onDismissRequest = {
                        conditionMenuExpanded = false
                    }
                ) {
                    listOf(
                        ItemCondition.EXCELLENT,
                        ItemCondition.GOOD,
                        ItemCondition.AVERAGE,
                        ItemCondition.POOR,
                        ItemCondition.VERY_POOR
                    ).forEach { itemCondition ->
                        DropdownMenuItem(
                            text = {
                                Text(itemCondition.toDisplayName())
                            },
                            onClick = {
                                condition = itemCondition
                                conditionMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("Data de encerramento") },
            placeholder = { Text("dd/MM/aaaa") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = endTime,
            onValueChange = { endTime = it },
            label = { Text("Horário de encerramento") },
            placeholder = { Text("20:30") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = initialBid,
            onValueChange = { initialBid = it },
            label = { Text("Lance inicial") },
            placeholder = { Text("5,00") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = bidIncrement,
            onValueChange = { bidIncrement = it },
            label = { Text("Múltiplo dos lances") },
            placeholder = { Text("5,00") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = buyoutPrice,
            onValueChange = { buyoutPrice = it },
            label = { Text("Valor de arremate — opcional") },
            placeholder = { Text("100,00") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Descrição e observações") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {
                onSave(
                    Auction(
                        id = auctionToEdit?.id ?: 0,
                        title = title.trim(),
                        platform = platform.trim(),
                        postUrl = postUrl.trim(),
                        endTimeMillis = requireNotNull(endTimeMillis),
                        notes = notes.trim(),
                        initialBidInCents =
                            requireNotNull(initialBidInCents),
                        bidIncrementInCents =
                            requireNotNull(bidIncrementInCents),
                        buyoutPriceInCents = buyoutPriceInCents,
                        finalPriceInCents =
                            auctionToEdit?.finalPriceInCents,
                        condition = condition,
                        status = auctionToEdit?.status
                            ?: AuctionStatus.ACTIVE,
                        alertsEnabled =
                            auctionToEdit?.alertsEnabled ?: true
                    )
                )
            },
            enabled = formIsValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (auctionToEdit == null) {
                    "Salvar"
                } else {
                    "Salvar alterações"
                }
            )
        }

        TextButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}

private fun ItemCondition.toDisplayName(): String {
    return when (this) {
        ItemCondition.EXCELLENT -> "Ótimo"
        ItemCondition.GOOD -> "Bom"
        ItemCondition.AVERAGE -> "Médio"
        ItemCondition.POOR -> "Ruim"
        ItemCondition.VERY_POOR -> "Péssimo"
        ItemCondition.NOT_INFORMED -> "Não informado"
    }
}

private fun parseEndTimeMillis(
    date: String,
    time: String
): Long? {
    return runCatching {
        val localDate = LocalDate.parse(date.trim(), dateFormatter)
        val localTime = LocalTime.parse(time.trim(), timeFormatter)

        LocalDateTime.of(localDate, localTime)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }.getOrNull()
}

private fun parseMoneyToCents(value: String): Long? {
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

private fun formatDateForForm(value: Long): String {
    return Instant.ofEpochMilli(value)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(dateFormatter)
}

private fun formatTimeForForm(value: Long): String {
    return Instant.ofEpochMilli(value)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(timeFormatter)
}

private fun formatMoneyForForm(valueInCents: Long): String {
    return BigDecimal.valueOf(valueInCents, 2)
        .stripTrailingZeros()
        .toPlainString()
        .replace(".", ",")
}

@Preview(showBackground = true)
@Composable
fun AddAuctionScreenPreview() {
    LeilõesRetroGamesTheme {
        AddAuctionScreen(
            onSave = {},
            onCancel = {}
        )
    }
}