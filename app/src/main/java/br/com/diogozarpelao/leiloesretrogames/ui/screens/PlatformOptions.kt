package br.com.diogozarpelao.leiloesretrogames.ui.screens

import androidx.compose.ui.graphics.Color

data class PlatformOption(
    val name: String,
    val badge: String,
    val backgroundColor: Color,
    val textColor: Color
)

val platformOptions = listOf(
    PlatformOption(
        name = "PlayStation 1",
        badge = "PS1",
        backgroundColor = Color(0xFF9CA3AF),
        textColor = Color(0xFFDC2626)
    ),
    PlatformOption(
        name = "PlayStation 2",
        badge = "PS2",
        backgroundColor = Color(0xFF0B1F3A),
        textColor = Color(0xFF3B82F6)
    ),
    PlatformOption(
        name = "PlayStation 3",
        badge = "PS3",
        backgroundColor = Color(0xFF111827),
        textColor = Color.White
    ),
    PlatformOption(
        name = "PlayStation 4",
        badge = "PS4",
        backgroundColor = Color(0xFF003087),
        textColor = Color.White
    ),
    PlatformOption(
        name = "PlayStation 5",
        badge = "PS5",
        backgroundColor = Color.White,
        textColor = Color.Black
    ),
    PlatformOption(
        name = "PSP",
        badge = "PSP",
        backgroundColor = Color.Black,
        textColor = Color.White
    ),
    PlatformOption(
        name = "PS Vita",
        badge = "VITA",
        backgroundColor = Color(0xFF0A3D91),
        textColor = Color.White
    ),
    PlatformOption(
        name = "Xbox",
        badge = "XBOX",
        backgroundColor = Color.Black,
        textColor = Color(0xFF22C55E)
    ),
    PlatformOption(
        name = "Xbox 360",
        badge = "X360",
        backgroundColor = Color(0xFF166534),
        textColor = Color.White
    ),
    PlatformOption(
        name = "Xbox One",
        badge = "XONE",
        backgroundColor = Color(0xFF166534),
        textColor = Color.White
    ),
    PlatformOption(
        name = "Dreamcast",
        badge = "DC",
        backgroundColor = Color.White,
        textColor = Color(0xFFDC2626)
    ),
    PlatformOption(
        name = "Sega Saturn",
        badge = "SAT",
        backgroundColor = Color(0xFFFACC15),
        textColor = Color.Black
    ),
    PlatformOption(
        name = "GameCube",
        badge = "GC",
        backgroundColor = Color(0xFF5B21B6),
        textColor = Color.White
    ),
    PlatformOption(
        name = "Nintendo Wii",
        badge = "WII",
        backgroundColor = Color.White,
        textColor = Color(0xFF38BDF8)
    ),
    PlatformOption(
        name = "Wii U",
        badge = "WIIU",
        backgroundColor = Color(0xFF38BDF8),
        textColor = Color.White
    ),
    PlatformOption(
        name = "Nintendo 64",
        badge = "N64",
        backgroundColor = Color.Black,
        textColor = Color(0xFF16A34A)
    ),
    PlatformOption(
        name = "Super Nintendo",
        badge = "SNES",
        backgroundColor = Color(0xFF6B7280),
        textColor = Color(0xFF7C3AED)
    ),
    PlatformOption(
        name = "Mega Drive",
        badge = "MD",
        backgroundColor = Color.Black,
        textColor = Color.White
    ),
    PlatformOption(
        name = "Game Boy Advance",
        badge = "GBA",
        backgroundColor = Color(0xFF5B21B6),
        textColor = Color.White
    ),
    PlatformOption(
        name = "Nintendo DS",
        badge = "NDS",
        backgroundColor = Color.White,
        textColor = Color(0xFFDC2626)
    ),
    PlatformOption(
        name = "Nintendo 3DS",
        badge = "3DS",
        backgroundColor = Color.White,
        textColor = Color(0xFFDC2626)
    ),
    PlatformOption(
        name = "Nintendo Switch",
        badge = "SWITCH",
        backgroundColor = Color(0xFFE60012),
        textColor = Color.White
    ),
    PlatformOption(
        name = "Nintendo Switch 2",
        badge = "SWITCH2",
        backgroundColor = Color(0xFFE60012),
        textColor = Color.White
    ),
    PlatformOption(
        name = "PC",
        badge = "PC",
        backgroundColor = Color(0xFF0EA5E9),
        textColor = Color.White
    )
)

fun platformBadge(platform: String): String {
    return platformOptions
        .firstOrNull {
            it.name.equals(platform, ignoreCase = true)
        }
        ?.badge
        ?: platform.take(5).uppercase()
}

fun platformBackgroundColor(platform: String): Color {
    return platformOptions
        .firstOrNull {
            it.name.equals(platform, ignoreCase = true)
        }
        ?.backgroundColor
        ?: Color(0xFF64748B)
}

fun platformTextColor(platform: String): Color {
    return platformOptions
        .firstOrNull {
            it.name.equals(platform, ignoreCase = true)
        }
        ?.textColor
        ?: Color.White
}