package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuctionDatePickerDialog(
    currentDate: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val initialDateMillis = runCatching {
        LocalDate
            .parse(
                currentDate,
                auctionDateFormatter
            )
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }.getOrElse {
        LocalDate
            .now()
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis
                        ?.let { millis ->
                            val selectedDate =
                                Instant
                                    .ofEpochMilli(millis)
                                    .atZone(ZoneOffset.UTC)
                                    .toLocalDate()
                                    .format(
                                        auctionDateFormatter
                                    )

                            onDateSelected(
                                selectedDate
                            )
                        }

                    onDismiss()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
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

@Composable
internal fun AuctionTimePickerDialog(
    currentTime: String,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val initialTime = runCatching {
        LocalTime.parse(
            currentTime,
            auctionTimeFormatter
        )
    }.getOrElse {
        LocalTime.now()
    }

    var selectedHour by rememberSaveable(
        currentTime
    ) {
        mutableStateOf(
            initialTime.hour
                .toString()
                .padStart(2, '0')
        )
    }

    var selectedMinute by rememberSaveable(
        currentTime
    ) {
        mutableStateOf(
            initialTime.minute
                .toString()
                .padStart(2, '0')
        )
    }

    val selectedHourNumber =
        selectedHour.toIntOrNull()

    val selectedMinuteNumber =
        selectedMinute.toIntOrNull()

    val inputIsValid =
        selectedHourNumber != null &&
            selectedHourNumber in 0..23 &&
            selectedMinuteNumber != null &&
            selectedMinuteNumber in 0..59

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Selecionar horário")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val hour =
                        requireNotNull(
                            selectedHourNumber
                        )

                    val minute =
                        requireNotNull(
                            selectedMinuteNumber
                        )

                    onTimeSelected(
                        "%02d:%02d".format(
                            hour,
                            minute
                        )
                    )

                    onDismiss()
                },
                enabled = inputIsValid
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
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
                            value.all {
                                it.isDigit()
                            }
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
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Number
                        ),
                    singleLine = true,
                    modifier =
                        Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = selectedMinute,
                    onValueChange = { value ->
                        if (
                            value.length <= 2 &&
                            value.all {
                                it.isDigit()
                            }
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
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Number
                        ),
                    singleLine = true,
                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        }
    )
}