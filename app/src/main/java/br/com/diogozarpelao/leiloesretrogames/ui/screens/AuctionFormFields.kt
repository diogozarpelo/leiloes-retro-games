package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.ItemCondition

@Composable
internal fun AuctionTitleField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text("Produto")
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
internal fun AuctionPostUrlField(
    value: String,
    onValueChange: (String) -> Unit,
    isValid: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text("Link da publicação")
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Uri
        ),
        isError =
            value.isNotBlank() &&
                !isValid,
        supportingText = {
            if (
                value.isNotBlank() &&
                !isValid
            ) {
                Text(
                    "Informe um link iniciado por http:// ou https://"
                )
            }
        },
        singleLine = true
    )
}

@Composable
internal fun AuctionPlatformField(
    value: String,
    onValueChange: (String) -> Unit
) {
    var menuExpanded by rememberSaveable {
        mutableStateOf(false)
    }

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
                    menuExpanded = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (value.isBlank()) {
                        "Selecionar plataforma"
                    } else {
                        value
                    }
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = {
                    menuExpanded = false
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
                            onValueChange(option.name)
                            menuExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun AuctionConditionField(
    value: ItemCondition,
    onValueChange: (ItemCondition) -> Unit
) {
    var menuExpanded by rememberSaveable {
        mutableStateOf(false)
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
                    menuExpanded = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    value.toDisplayName()
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = {
                    menuExpanded = false
                }
            ) {
                listOf(
                    ItemCondition.EXCELLENT,
                    ItemCondition.GOOD,
                    ItemCondition.AVERAGE,
                    ItemCondition.POOR,
                    ItemCondition.VERY_POOR
                ).forEach { condition ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                condition.toDisplayName()
                            )
                        },
                        onClick = {
                            onValueChange(condition)
                            menuExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun AuctionDateField(
    value: String,
    onValueChange: (String) -> Unit
) {
    var showPicker by rememberSaveable {
        mutableStateOf(false)
    }

    Column {
        Text(
            text = "Data de encerramento",
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedButton(
            onClick = {
                showPicker = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (value.isBlank()) {
                    "Selecionar data"
                } else {
                    value
                }
            )
        }
    }

    if (showPicker) {
        AuctionDatePickerDialog(
            currentDate = value,
            onDateSelected = onValueChange,
            onDismiss = {
                showPicker = false
            }
        )
    }
}

@Composable
internal fun AuctionTimeField(
    value: String,
    onValueChange: (String) -> Unit,
    dateIsFilled: Boolean,
    isValid: Boolean
) {
    var showPicker by rememberSaveable {
        mutableStateOf(false)
    }

    Column {
        Text(
            text = "Horário de encerramento",
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedButton(
            onClick = {
                showPicker = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (value.isBlank()) {
                    "Selecionar horário"
                } else {
                    value
                }
            )
        }

        if (
            dateIsFilled &&
            value.isNotBlank() &&
            !isValid
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

    if (showPicker) {
        AuctionTimePickerDialog(
            currentTime = value,
            onTimeSelected = onValueChange,
            onDismiss = {
                showPicker = false
            }
        )
    }
}

@Composable
internal fun AuctionMoneyField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isValid: Boolean,
    errorMessage: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        placeholder = {
            Text(placeholder)
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        isError =
            value.isNotBlank() &&
                !isValid,
        supportingText = {
            if (
                value.isNotBlank() &&
                !isValid
            ) {
                Text(errorMessage)
            }
        },
        singleLine = true
    )
}

@Composable
internal fun AuctionNotesField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text("Descrição e observações")
        },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3
    )
}

@Composable
internal fun AuctionAlertsField(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Alertas",
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Text(
                    text =
                        if (enabled) {
                            "Notificações ativadas para este leilão."
                        } else {
                            "Notificações desativadas para este leilão."
                        },
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }

            Switch(
                checked = enabled,
                onCheckedChange = onEnabledChange
            )
        }
    }
}