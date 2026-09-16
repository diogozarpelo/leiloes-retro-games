package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme
import kotlinx.coroutines.delay

enum class AuctionSection {
    ACTIVE,
    ENDED
}

@Composable
fun ActiveAuctionsScreen(
    auctions: List<Auction>,
    selectedSection: AuctionSection,
    onSectionChange: (AuctionSection) -> Unit,
    onAuctionClick: (Auction) -> Unit,
    onAddAuction: () -> Unit,
    modifier: Modifier = Modifier
) {
    var endedFilter by rememberSaveable {
        mutableStateOf(
            EndedAuctionFilter.ALL
        )
    }

    var wonFilter by rememberSaveable {
        mutableStateOf(
            WonAuctionFilter.ALL
        )
    }

    val boundaryTime =
        rememberAuctionBoundaryTime(
            auctions = auctions
        )

    val activeCount =
        remember(
            auctions,
            boundaryTime
        ) {
            countActiveAuctions(
                auctions = auctions,
                currentTime = boundaryTime
            )
        }

    val endedCount =
        remember(
            auctions,
            boundaryTime
        ) {
            countEndedAuctions(
                auctions = auctions,
                currentTime = boundaryTime
            )
        }

    val displayedAuctions =
        remember(
            auctions,
            selectedSection,
            endedFilter,
            wonFilter,
            boundaryTime
        ) {
            filterAuctionsForSection(
                auctions = auctions,
                selectedSection =
                    selectedSection,
                endedFilter =
                    endedFilter,
                wonFilter =
                    wonFilter,
                currentTime =
                    boundaryTime
            )
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {
        ActiveAuctionsHeader()

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            AuctionSummaryCard(
                title = "Ativos",
                value =
                    activeCount.toString(),
                selected =
                    selectedSection ==
                        AuctionSection.ACTIVE,
                onClick = {
                    onSectionChange(
                        AuctionSection.ACTIVE
                    )
                },
                modifier =
                    Modifier.weight(1f)
            )

            AuctionSummaryCard(
                title = "Encerrados",
                value =
                    endedCount.toString(),
                selected =
                    selectedSection ==
                        AuctionSection.ENDED,
                onClick = {
                    onSectionChange(
                        AuctionSection.ENDED
                    )
                },
                modifier =
                    Modifier.weight(1f)
            )
        }

        if (
            selectedSection ==
                AuctionSection.ENDED
        ) {
            EndedAuctionFilters(
                selectedFilter =
                    endedFilter,
                onFilterSelected = {
                    endedFilter = it
                }
            )
        }

        if (
            selectedSection ==
                AuctionSection.ENDED &&
            endedFilter ==
                EndedAuctionFilter.WON
        ) {
            WonAuctionFilters(
                selectedFilter =
                    wonFilter,
                onFilterSelected = {
                    wonFilter = it
                }
            )
        }

        Text(
            text =
                if (
                    selectedSection ==
                        AuctionSection.ACTIVE
                ) {
                    "Leilões ativos"
                } else {
                    "Leilões encerrados"
                },
            style =
                MaterialTheme
                    .typography
                    .titleMedium,
            fontWeight =
                FontWeight.SemiBold
        )

        if (
            displayedAuctions.isEmpty()
        ) {
            EmptyAuctionsMessage(
                text =
                    emptyAuctionsMessage(
                        selectedSection =
                            selectedSection,
                        endedFilter =
                            endedFilter,
                        wonFilter =
                            wonFilter
                    )
            )
        } else {
            LazyColumn(
                modifier =
                    Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {
                items(
                    items =
                        displayedAuctions,
                    key = { auction ->
                        auction.id
                    }
                ) { auction ->
                    AuctionCard(
                        auction = auction,
                        onClick = {
                            onAuctionClick(
                                auction
                            )
                        }
                    )
                }

                item {
                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )
                }
            }
        }

        Button(
            onClick =
                onAddAuction,
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(14.dp),
            contentPadding =
                ButtonDefaults.ContentPadding
        ) {
            Text(
                text =
                    "Cadastrar novo leilão",
                fontWeight =
                    FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = {
                onSectionChange(
                    if (
                        selectedSection ==
                            AuctionSection.ACTIVE
                    ) {
                        AuctionSection.ENDED
                    } else {
                        AuctionSection.ACTIVE
                    }
                )
            },
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(14.dp)
        ) {
            Text(
                text =
                    if (
                        selectedSection ==
                            AuctionSection.ACTIVE
                    ) {
                        "Ir para encerrados"
                    } else {
                        "Voltar para ativos"
                    },
                fontWeight =
                    FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun rememberAuctionBoundaryTime(
    auctions: List<Auction>
): Long {
    val currentTime by produceState(
        initialValue =
            System.currentTimeMillis(),
        key1 = auctions
    ) {
        while (true) {
            val now =
                System.currentTimeMillis()

            value = now

            val nextEndTime =
                auctions
                    .asSequence()
                    .map {
                        it.endTimeMillis
                    }
                    .filter {
                        it > now
                    }
                    .minOrNull()
                    ?: break

            val waitTime =
                (
                    nextEndTime -
                        now +
                        50L
                    )
                    .coerceAtLeast(1L)

            delay(waitTime)
        }
    }

    return currentTime
}

@Composable
private fun ActiveAuctionsHeader() {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Leilões RetroGames",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium,
            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                "Acompanhe seus leilões em um só lugar",
            style =
                MaterialTheme
                    .typography
                    .bodyMedium,
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActiveAuctionsScreenPreview() {
    LeilõesRetroGamesTheme {
        ActiveAuctionsScreen(
            auctions =
                listOf(
                    Auction(
                        id = 1,
                        title =
                            "Resident Evil 2",
                        platform =
                            "PlayStation 2",
                        postUrl =
                            "https://facebook.com",
                        endTimeMillis =
                            System
                                .currentTimeMillis() +
                                3_600_000,
                        initialBidInCents =
                            500,
                        bidIncrementInCents =
                            500
                    )
                ),
            selectedSection =
                AuctionSection.ACTIVE,
            onSectionChange = {},
            onAuctionClick = {},
            onAddAuction = {}
        )
    }
}