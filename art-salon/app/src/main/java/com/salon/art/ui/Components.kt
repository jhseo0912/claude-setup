package com.salon.art.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.salon.art.data.ArtSource
import com.salon.art.data.Artwork
import com.salon.art.ui.theme.Palette
import com.salon.art.ui.theme.SalonType
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

private val won = NumberFormat.getNumberInstance(Locale.KOREA)

fun formatPrice(price: Long): String = "₩" + won.format(price)

fun formatChange(change: Float): String =
    (if (change >= 0f) "▲ " else "▼ ") + String.format(Locale.US, "%.2f%%", abs(change * 100))

fun changeColor(change: Float): Color = if (change >= 0f) Palette.Up else Palette.Down

// 넓은 자간은 영문 대문자에만 어울린다. 한글이 섞이면 자간을 거의 두지 않는다
private fun TextStyle.fitSpacing(text: String) =
    if (text.any { it in '\uAC00'..'\uD7A3' }) copy(letterSpacing = 0.3.sp) else this

@Composable
fun Caps(text: String, modifier: Modifier = Modifier, color: Color = Palette.Graphite) {
    Text(text.uppercase(), modifier, color = color, style = SalonType.Label.fitSpacing(text))
}

@Composable
fun Hairline(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier, thickness = 1.dp, color = Palette.Hairline)
}

/** 갤러리에서 판매된 작품 옆에 붙이는 레드닷. */
@Composable
fun SoldMark(text: String = "Sold") {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(7.dp).background(Palette.RedDot, CircleShape))
        Spacer(Modifier.width(6.dp))
        Caps(text, color = Palette.RedDot)
    }
}

private val ButtonText = SalonType.Label.copy(fontSize = 12.sp, letterSpacing = 2.sp)

@Composable
fun InkButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Box(
        modifier
            .height(52.dp)
            .background(if (enabled) Palette.Ink else Palette.Hairline)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = if (enabled) Palette.Paper else Palette.Graphite, style = ButtonText.fitSpacing(text))
    }
}

@Composable
fun GhostButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier.height(52.dp).border(1.dp, Palette.Ink).clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = Palette.Ink, style = ButtonText.fitSpacing(text))
    }
}

enum class Glyph { Back, Close, Plus }

@Composable
fun LineIcon(glyph: Glyph, modifier: Modifier = Modifier.size(18.dp), color: Color = Palette.Ink) {
    Canvas(modifier) {
        val stroke = 1.4.dp.toPx()
        val w = size.width
        val h = size.height
        fun line(a: Offset, b: Offset) = drawLine(color, a, b, stroke, StrokeCap.Round)
        when (glyph) {
            Glyph.Back -> {
                line(Offset(0f, h / 2), Offset(w, h / 2))
                line(Offset(0f, h / 2), Offset(w * 0.35f, h * 0.15f))
                line(Offset(0f, h / 2), Offset(w * 0.35f, h * 0.85f))
            }
            Glyph.Close -> {
                line(Offset(w * 0.1f, h * 0.1f), Offset(w * 0.9f, h * 0.9f))
                line(Offset(w * 0.9f, h * 0.1f), Offset(w * 0.1f, h * 0.9f))
            }
            Glyph.Plus -> {
                line(Offset(w / 2, 0f), Offset(w / 2, h))
                line(Offset(0f, h / 2), Offset(w, h / 2))
            }
        }
    }
}

@Composable
fun IconTap(glyph: Glyph, label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        LineIcon(glyph)
    }
}

@Composable
fun ArtworkImage(art: Artwork, modifier: Modifier = Modifier) {
    val sized = modifier.aspectRatio(art.aspect)
    when (val src = art.source) {
        is ArtSource.Generative -> GenerativeArt(src, sized)
        is ArtSource.Photo -> AsyncImage(
            model = src.uri,
            contentDescription = art.title,
            contentScale = ContentScale.Crop,
            modifier = sized.background(Palette.Hairline),
        )
    }
}

/** 갤러리 벽에 건 것처럼 회색 면 위에 그림자를 두고 작품을 놓는다. */
@Composable
fun WallTile(art: Artwork, modifier: Modifier = Modifier, padding: Dp = 20.dp, elevation: Dp = 8.dp) {
    Box(modifier.background(Palette.Wall).padding(padding), contentAlignment = Alignment.Center) {
        ArtworkImage(art, Modifier.shadow(elevation, RectangleShape))
    }
}

/** 시세 추이 선 그래프. 마지막 점에 현재값 표시를 찍는다. */
@Composable
fun IndexChart(points: List<Float>, modifier: Modifier = Modifier, color: Color = Palette.Ink) {
    Canvas(modifier) {
        val lo = points.min()
        val span = (points.max() - lo).takeIf { it > 0f } ?: 1f
        val pad = 4.dp.toPx()
        fun at(i: Int) = Offset(
            pad + i * (size.width - pad * 2) / (points.size - 1),
            pad + (1f - (points[i] - lo) / span) * (size.height - pad * 2),
        )
        val line = Path().apply {
            points.indices.forEach { i -> at(i).let { if (i == 0) moveTo(it.x, it.y) else lineTo(it.x, it.y) } }
        }
        val area = Path().apply {
            addPath(line)
            lineTo(at(points.lastIndex).x, size.height)
            lineTo(pad, size.height)
            close()
        }
        drawPath(area, Brush.verticalGradient(listOf(color.copy(alpha = 0.12f), Color.Transparent)))
        drawPath(line, color, style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawCircle(color, 3.dp.toPx(), at(points.lastIndex))
    }
}

/** 판매 신청 이후 단계를 가로 막대로 보여준다. */
@Composable
fun StageTrack(art: Artwork, modifier: Modifier = Modifier) {
    val stages = art.saleType.stages
    val current = stages.indexOf(art.stage)
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        stages.forEachIndexed { i, stage ->
            val reached = i <= current
            val bar by animateColorAsState(if (reached) Palette.Ink else Palette.Hairline, label = "stage")
            Column(Modifier.weight(1f)) {
                Box(Modifier.fillMaxWidth().height(2.dp).background(bar))
                Spacer(Modifier.height(8.dp))
                Text(
                    stage.label,
                    style = SalonType.Body.copy(
                        fontSize = 11.sp,
                        fontWeight = if (i == current) FontWeight.SemiBold else FontWeight.Normal,
                    ),
                    color = if (reached) Palette.Ink else Palette.Graphite,
                    maxLines = 1,
                )
            }
        }
    }
}
