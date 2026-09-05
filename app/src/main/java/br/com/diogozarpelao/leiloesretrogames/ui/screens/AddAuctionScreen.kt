package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val dateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm")

@OptIn(ExperimentalMaterial3Api::class)
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

    var platformMenuExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var showDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    var showTimePicker by rememberSaveable {
        mutableStateOf(false)
    }

    val initialDateMillis = runCatching {
        LocalDate.parse(endDate, dateFormatter)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }.getOrElse {
        LocalDate.now()
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )

    val initialTime = runCatching {
        LocalTime.parse(endTime, timeFormatter)
    }.getOrElse {
        LocalTime.now()
    }

    var selectedHour by rememberSaveable(stateKey) {
        mutableStateOf(
            initialTime.hour.toString().padStart(2, '0')
        )
    }

    var selectedMinute by rememberSaveable(stateKey) {
        mutableStateOf(
            initialTime.minute.toString().padStart(2, '0')
        )
    }

    val selectedHourNumber = selectedHour.toIntOrNull()
    val selectedMinuteNumber = selectedMinute.toIntOrNull()

    val timeInputIsValid =
        selectedHourNumber != null &&
                selectedHourNumber in 0..23 &&
                selectedMinuteNumber != null &&
                selectedMinuteNumber in 0..59

    val endTimeMillis = parseEndTimeMillis(endDate, endTime)
    val initialBidInCents = parseMoneyToCents(initialBid)
    val bidIncrementInCents = parseMoneyToCents(bidIncrement)

    val buyoutPriceInCents = if (buyoutPrice.isBlank()) {
        null
    } else {
        parseMoneyToCents(buyoutPrice)
    }

    val postUrlIsValid =
        postUrl.startsWith("http://") ||
                postUrl.startsWith("https://")

    val initialBidIsValid =
        initialBidInCents != null

    val bidIncrementIsValid =
        bidIncrementInCents != null &&
                bidIncrementInCents > 0

    val buyoutIsValid =
        buyoutPrice.isBlank() ||
                buyoutPriceInCents != null

    val endTimeIsValid =
        endTimeMillis != null &&
                endTimeMillis > System.currentTimeMillis()

    val formIsValid =
        title.isNotBlank() &&
                platform.isNotBlank() &&
                postUrl.isNotBlank() &&
                postUrlIsValid &&
                endTimeIsValid &&
                initialBidIsValid &&
                bidIncrementIsValid &&
                buyoutIsValid

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            endDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                                .format(dateFormatter)
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = {
                showTimePicker = false
            },
            title = {
                Text("Selecionar horário")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        endTime = "%02d:%02d".format(
                            requireNotNull(selectedHourNumber),
                            requireNotNull(selectedMinuteNumber)
                        )

                        showTimePicker = false
                    },
                    enabled = timeInputIsValid
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                    }
                ) {
                    Text("Cancelar")
                }
            },
            text = {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = selectedHour,
                        onValueChange = { value ->
                            if (
                                value.length <= 2 &&
                                value.all { it.isDigit() }
                            ) {
                                selectedHour = value
                            }
                        },
                        label = {
                            Text("Hora")
                        },
                        supportingText = {
                            Text("00 a 23")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = selectedMinute,
                        onValueChange = { value ->
                            if (
                                value.length <= 2 &&
                                value.all { it.isDigit() }
                            ) {
                                selectedMinute = value
                            }
                        },
                        label = {
                            Text("Minuto")
                        },
                        supportingText = {
                            Text("00 a 59")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 24.dp,
                    bottom = 12.dp
                ),
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
                value = postUrl,
                onValueChange = { postUrl = it },
                label = { Text("Link da publicação") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri
                ),
                isError =
                    postUrl.isNotBlank() &&
                            !postUrlIsValid,
                supportingText = {
                    if (
                        postUrl.isNotBlank() &&
                        !postUrlIsValid
                    ) {
                        Text(
                            "Informe um link iniciado por http:// ou https://"
                        )
                    }
                },
                singleLine = true
            )

            Column {
                Text(
                    text = "Plataforma",
                    style = MaterialTheme.typography.bodyMedium
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {
                            platformMenuExpanded = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (platform.isBlank()) {
                                "Selecionar plataforma"
                            } else {
                                platform
                            }
                        )
                    }

                    DropdownMenu(
                        expanded = platformMenuExpanded,
                        onDismissRequest = {
                            platformMenuExpanded = false
                        }
                    ) {
                        platformOptions.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "${option.badge} • ${option.name}"
                                    )
                                },
                                onClick = {
                                    platform = option.name
                                    platformMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

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
                                    Text(
                                        itemCondition.toDisplayName()
                                    )
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

            Column {
                Text(
                    text = "Data de encerramento",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedButton(
                    onClick = {
                        showDatePicker = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (endDate.isBlank()) {
                            "Selecionar data"
                        } else {
                            endDate
                        }
                    )
                }
            }

            Column {
                Text(
                    text = "Horário de encerramento",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedButton(
                    onClick = {
                        showTimePicker = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (endTime.isBlank()) {
                            "Selecionar horário"
                        } else {
                            endTime
                        }
                    )
                }

                if (
                    endDate.isNotBlank() &&
                    endTime.isNotBlank() &&
                    !endTimeIsValid
                ) {
                    Text(
                        text =
                            "O encerramento precisa estar no futuro.",
                        color =
                            MaterialTheme.colorScheme.error,
                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }
            }

            OutlinedTextField(
                value = initialBid,
                onValueChange = {
                    initialBid = it
                },
                label = {
                    Text("Lance inicial")
                },
                placeholder = {
                    Text("5,00")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                isError =
                    initialBid.isNotBlank() &&
                            !initialBidIsValid,
                supportingText = {
                    if (
                        initialBid.isNotBlank() &&
                        !initialBidIsValid
                    ) {
                        Text(
                            "Informe um valor válido. Exemplo: 5,00"
                        )
                    }
                },
                singleLine = true
            )

            OutlinedTextField(
                value = bidIncrement,
                onValueChange = {
                    bidIncrement = it
                },
                label = {
                    Text("Múltiplo dos lances")
                },
                placeholder = {
                    Text("5,00")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                isError =
                    bidIncrement.isNotBlank() &&
                            !bidIncrementIsValid,
                supportingText = {
                    if (
                        bidIncrement.isNotBlank() &&
                        !bidIncrementIsValid
                    ) {
                        Text(
                            "O múltiplo precisa ser maior que zero."
                        )
                    }
                },
                singleLine = true
            )

            OutlinedTextField(
                value = buyoutPrice,
                onValueChange = {
                    buyoutPrice = it
                },
                label = {
                    Text("Valor de arremate — opcional")
                },
                placeholder = {
                    Text("100,00")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                isError =
                    buyoutPrice.isNotBlank() &&
                            !buyoutIsValid,
                supportingText = {
                    if (
                        buyoutPrice.isNotBlank() &&
                        !buyoutIsValid
                    ) {
                        Text(
                            "Informe um valor válido. Exemplo: 100,00"
                        )
                    }
                },
                singleLine = true
            )

            OutlinedTextField(
                value = notes,
                onValueChange = {
                    notes = it
                },
                label = {
                    Text("Descrição e observações")
                },
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
                            endTimeMillis =
                                requireNotNull(endTimeMillis),
                            notes = notes.trim(),
                            initialBidInCents =
                                requireNotNull(
                                    initialBidInCents
                                ),
                            bidIncrementInCents =
                                requireNotNull(
                                    bidIncrementInCents
                                ),
                            buyoutPriceInCents =
                                buyoutPriceInCents,
                            finalPriceInCents =
                                auctionToEdit
                                    ?.finalPriceInCents,
                            condition = condition,
                            status =
                                auctionToEdit?.status
                                    ?: AuctionStatus.ACTIVE,
                            alertsEnabled =
                                auctionToEdit
                                    ?.alertsEnabled
                                    ?: true
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
        }

        TextButton(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                )
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
        val localDate =
            LocalDate.parse(date.trim(), dateFormatter)

        val localTime =
            LocalTime.parse(time.trim(), timeFormatter)

        LocalDateTime.of(
            localDate,
            localTime
        )
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }.getOrNull()
}

private fun parseMoneyToCents(
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

private fun formatDateForForm(
    value: Long
): String {
    return Instant
        .ofEpochMilli(value)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(dateFormatter)
}

private fun formatTimeForForm(
    value: Long
): String {
    return Instant
        .ofEpochMilli(value)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(timeFormatter)
}

private fun formatMoneyForForm(
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
fun AddAuctionScreenPreview() {
    LeilõesRetroGamesTheme {
        AddAuctionScreen(
            onSave = {},
            onCancel = {}
        )
    }
}