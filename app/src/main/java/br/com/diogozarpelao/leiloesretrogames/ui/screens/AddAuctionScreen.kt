package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
import br.com.diogozarpelao.leiloesretrogames.model.ItemCondition
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme
import kotlinx.coroutines.delay

@Composable
fun AddAuctionScreen(
    onSave: (Auction) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    auctionToEdit: Auction? = null
) {
    val stateKey = auctionToEdit?.id ?: 0L

    var title by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.title.orEmpty()
        )
    }

    var platform by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.platform.orEmpty()
        )
    }

    var postUrl by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.postUrl.orEmpty()
        )
    }

    var endDate by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit
                ?.let {
                    formatDateForForm(
                        it.endTimeMillis
                    )
                }
                .orEmpty()
        )
    }

    var endTime by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit
                ?.let {
                    formatTimeForForm(
                        it.endTimeMillis
                    )
                }
                .orEmpty()
        )
    }

    var initialBid by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit
                ?.let {
                    formatMoneyForForm(
                        it.initialBidInCents
                    )
                }
                .orEmpty()
        )
    }

    var bidIncrement by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit
                ?.let {
                    formatMoneyForForm(
                        it.bidIncrementInCents
                    )
                }
                .orEmpty()
        )
    }

    var buyoutPrice by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit
                ?.buyoutPriceInCents
                ?.let(
                    ::formatMoneyForForm
                )
                .orEmpty()
        )
    }

    var notes by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.notes.orEmpty()
        )
    }

    var condition by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.condition
                ?: ItemCondition.NOT_INFORMED
        )
    }

    var alertsEnabled by rememberSaveable(stateKey) {
        mutableStateOf(
            auctionToEdit?.alertsEnabled
                ?: true
        )
    }

    val endTimeMillis =
        parseEndTimeMillis(
            endDate,
            endTime
        )

    val initialBidInCents =
        parseMoneyToCents(
            initialBid
        )

    val bidIncrementInCents =
        parseMoneyToCents(
            bidIncrement
        )

    val buyoutPriceInCents =
        if (buyoutPrice.isBlank()) {
            null
        } else {
            parseMoneyToCents(
                buyoutPrice
            )
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

    val currentTime =
        rememberAuctionFormBoundaryTime(
            endTimeMillis = endTimeMillis
        )

    val endTimeIsValid =
        endTimeMillis != null &&
            endTimeMillis >
                currentTime

    val formIsValid =
        title.isNotBlank() &&
            platform.isNotBlank() &&
            postUrl.isNotBlank() &&
            postUrlIsValid &&
            endTimeIsValid &&
            initialBidIsValid &&
            bidIncrementIsValid &&
            buyoutIsValid

    Column(
        modifier =
            modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 24.dp,
                    bottom = 12.dp
                ),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text =
                    if (auctionToEdit == null) {
                        "Cadastrar leilão"
                    } else {
                        "Editar leilão"
                    },
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            AuctionTitleField(
                value = title,
                onValueChange = {
                    title = it
                }
            )

            AuctionPostUrlField(
                value = postUrl,
                onValueChange = {
                    postUrl = it
                },
                isValid = postUrlIsValid
            )

            AuctionPlatformField(
                value = platform,
                onValueChange = {
                    platform = it
                }
            )

            AuctionConditionField(
                value = condition,
                onValueChange = {
                    condition = it
                }
            )

            AuctionDateField(
                value = endDate,
                onValueChange = {
                    endDate = it
                }
            )

            AuctionTimeField(
                value = endTime,
                onValueChange = {
                    endTime = it
                },
                dateIsFilled =
                    endDate.isNotBlank(),
                isValid =
                    endTimeIsValid
            )

            AuctionMoneyField(
                value = initialBid,
                onValueChange = {
                    initialBid = it
                },
                label = "Lance inicial",
                placeholder = "5,00",
                isValid =
                    initialBidIsValid,
                errorMessage =
                    "Informe um valor válido. Exemplo: 5,00"
            )

            AuctionMoneyField(
                value = bidIncrement,
                onValueChange = {
                    bidIncrement = it
                },
                label =
                    "Múltiplo dos lances",
                placeholder = "5,00",
                isValid =
                    bidIncrementIsValid,
                errorMessage =
                    "O múltiplo precisa ser maior que zero."
            )

            AuctionMoneyField(
                value = buyoutPrice,
                onValueChange = {
                    buyoutPrice = it
                },
                label =
                    "Valor de arremate — opcional",
                placeholder = "100,00",
                isValid =
                    buyoutIsValid,
                errorMessage =
                    "Informe um valor válido. Exemplo: 100,00"
            )

            AuctionNotesField(
                value = notes,
                onValueChange = {
                    notes = it
                }
            )

            AuctionAlertsField(
                enabled = alertsEnabled,
                onEnabledChange = {
                    alertsEnabled = it
                }
            )

            Button(
                onClick = {
                    onSave(
                        Auction(
                            id =
                                auctionToEdit?.id
                                    ?: 0,
                            title =
                                title.trim(),
                            platform =
                                platform.trim(),
                            postUrl =
                                postUrl.trim(),
                            endTimeMillis =
                                requireNotNull(
                                    endTimeMillis
                                ),
                            notes =
                                notes.trim(),
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
                            condition =
                                condition,
                            status =
                                auctionToEdit
                                    ?.status
                                    ?: AuctionStatus.ACTIVE,
                            alertsEnabled =
                                alertsEnabled
                        )
                    )
                },
                enabled = formIsValid,
                modifier =
                    Modifier.fillMaxWidth()
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

@Composable
private fun rememberAuctionFormBoundaryTime(
    endTimeMillis: Long?
): Long {
    val currentTime by produceState(
        initialValue =
            System.currentTimeMillis(),
        key1 =
            endTimeMillis
    ) {
        val targetTime =
            endTimeMillis
                ?: return@produceState

        if (value < targetTime) {
            val waitTime =
                (
                    targetTime -
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
fun AddAuctionScreenPreview() {
    LeilõesRetroGamesTheme {
        AddAuctionScreen(
            onSave = {},
            onCancel = {}
        )
    }
}