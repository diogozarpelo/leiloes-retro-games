package br.com.diogozarpelao.leiloesretrogames.ui.screens

import br.com.diogozarpelao.leiloesretrogames.model.ItemCondition
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal val auctionDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

internal val auctionTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm")

internal fun ItemCondition.toDisplayName(): String {
    return when (this) {
        ItemCondition.EXCELLENT -> "Ótimo"
        ItemCondition.GOOD -> "Bom"
        ItemCondition.AVERAGE -> "Médio"
        ItemCondition.POOR -> "Ruim"
        ItemCondition.VERY_POOR -> "Péssimo"
        ItemCondition.NOT_INFORMED -> "Não informado"
    }
}

internal fun parseEndTimeMillis(
    date: String,
    time: String
): Long? {
    return runCatching {
        val localDate =
            LocalDate.parse(
                date.trim(),
                auctionDateFormatter
            )

        val localTime =
            LocalTime.parse(
                time.trim(),
                auctionTimeFormatter
            )

        LocalDateTime.of(
            localDate,
            localTime
        )
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }.getOrNull()
}

internal fun parseMoneyToCents(
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

internal fun formatDateForForm(
    value: Long
): String {
    return Instant
        .ofEpochMilli(value)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(auctionDateFormatter)
}

internal fun formatTimeForForm(
    value: Long
): String {
    return Instant
        .ofEpochMilli(value)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(auctionTimeFormatter)
}

internal fun formatMoneyForForm(
    valueInCents: Long
): String {
    return BigDecimal
        .valueOf(valueInCents, 2)
        .stripTrailingZeros()
        .toPlainString()
        .replace(".", ",")
}