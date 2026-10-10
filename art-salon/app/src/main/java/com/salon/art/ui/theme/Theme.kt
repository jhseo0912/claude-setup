package com.salon.art.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** 화이트 큐브 갤러리의 벽과 먹색, 판매 표시용 레드닷만 쓴다. */
object Palette {
    val Paper = Color(0xFFF5F3EE)
    val Wall = Color(0xFFE9E6DF)
    val Ink = Color(0xFF141414)
    val Graphite = Color(0xFF77736B)
    val Hairline = Color(0xFFD8D4CB)
    val RedDot = Color(0xFFB3261E)

    // 국내 시세 표기 관례대로 상승은 빨강, 하락은 파랑
    val Up = Color(0xFFC4352B)
    val Down = Color(0xFF2A5BB8)
}

object SalonType {
    val Wordmark = TextStyle(fontFamily = FontFamily.Serif, fontSize = 28.sp, letterSpacing = 8.sp)
    val Display = TextStyle(
        fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic,
        fontSize = 32.sp, lineHeight = 38.sp,
    )
    val Heading = TextStyle(fontFamily = FontFamily.Serif, fontSize = 28.sp, lineHeight = 34.sp)
    val Title = TextStyle(
        fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic,
        fontSize = 16.sp, lineHeight = 20.sp,
    )
    val Label = TextStyle(
        fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium,
        fontSize = 10.sp, letterSpacing = 1.8.sp,
    )
    val Body = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 23.sp)
    val Price = TextStyle(fontFamily = FontFamily.Serif, fontSize = 14.sp, letterSpacing = 0.4.sp)
}

@Composable
fun SalonTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Palette.Ink,
            onPrimary = Palette.Paper,
            background = Palette.Paper,
            onBackground = Palette.Ink,
            surface = Palette.Paper,
            onSurface = Palette.Ink,
            surfaceVariant = Palette.Wall,
            outline = Palette.Hairline,
        ),
        content = content,
    )
}
