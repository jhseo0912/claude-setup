package com.salon.art.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.salon.art.data.ArtSource
import com.salon.art.data.ArtStyle
import kotlin.math.ceil
import kotlin.math.min
import kotlin.random.Random

/** 목업 작품 이미지. 같은 시드는 항상 같은 그림을 그린다. */
@Composable
fun GenerativeArt(source: ArtSource.Generative, modifier: Modifier = Modifier) {
    Canvas(modifier.clipToBounds()) {
        val r = Random(source.seed)
        val p = source.palette
        when (source.style) {
            ArtStyle.FIELD -> field(r, p)
            ArtStyle.ORBIT -> orbit(r, p)
            ArtStyle.GRID -> pencilGrid(r, p)
            ArtStyle.ARCS -> arcs(r, p)
            ArtStyle.STROKES -> strokes(r, p)
            ArtStyle.BLOCKS -> blocks(r, p)
        }
    }
}

/** 번진 경계의 색면을 위아래로 쌓는다. */
private fun DrawScope.field(r: Random, p: List<Color>) {
    drawRect(p[0])
    val bands = 2 + r.nextInt(2)
    val mx = size.width * 0.09f
    val my = size.height * 0.07f
    val gap = size.height * 0.035f
    val weights = List(bands) { 0.5f + r.nextFloat() }
    val usable = size.height - my * 2 - gap * (bands - 1)
    var top = my
    weights.forEachIndexed { i, w ->
        val h = usable * w / weights.sum()
        softRect(p[1 + i % (p.size - 1)], Rect(mx, top, size.width - mx, top + h))
        top += h + gap
    }
}

private fun DrawScope.softRect(color: Color, rect: Rect) {
    val feather = min(rect.width, rect.height) * 0.08f
    val layers = 10
    for (k in 0 until layers) {
        val inset = -feather / 2 + feather * k / (layers - 1)
        drawRect(
            color.copy(alpha = 0.2f),
            topLeft = Offset(rect.left + inset, rect.top + inset),
            size = Size(rect.width - inset * 2, rect.height - inset * 2),
        )
    }
}

/** 큰 원과 작은 원, 수평선. */
private fun DrawScope.orbit(r: Random, p: List<Color>) {
    val w = size.width
    val h = size.height
    val m = size.minDimension
    drawRect(p[0])
    val horizon = h * (0.62f + r.nextFloat() * 0.12f)
    drawRect(p[3].copy(alpha = 0.35f), topLeft = Offset(0f, horizon), size = Size(w, h - horizon))
    val big = m * (0.24f + r.nextFloat() * 0.08f)
    val c = Offset(w * (0.36f + r.nextFloat() * 0.26f), h * (0.34f + r.nextFloat() * 0.12f))
    drawCircle(p[1], big, c)
    val side = if (r.nextBoolean()) 1f else -1f
    drawCircle(p[2], big * (0.32f + r.nextFloat() * 0.18f), c + Offset(big * 0.9f * side, big * 0.8f))
    drawCircle(p[1], big * 1.35f, c, style = Stroke(m * 0.004f))
}

/** 손으로 그은 듯한 연필 수평선과 옅은 띠. */
private fun DrawScope.pencilGrid(r: Random, p: List<Color>) {
    drawRect(p[0])
    val m = size.width * 0.1f
    val bands = 6 + r.nextInt(5)
    val bandH = (size.height - 2 * m) / bands
    for (i in 0 until bands step 2) {
        drawRect(p[3].copy(alpha = 0.3f), Offset(m, m + i * bandH), Size(size.width - 2 * m, bandH))
    }
    val lines = 24 + r.nextInt(16)
    for (i in 0..lines) {
        val y = m + (size.height - 2 * m) * i / lines
        drawLine(
            p[1].copy(alpha = 0.3f),
            Offset(m, y),
            Offset(size.width - m, y + (r.nextFloat() - 0.5f) * 2f),
            strokeWidth = 1.2f,
        )
    }
}

/** 칸마다 모서리 하나를 중심으로 한 사분원. */
private fun DrawScope.arcs(r: Random, p: List<Color>) {
    val cols = 2 + r.nextInt(2)
    val cell = size.width / cols
    val rows = ceil(size.height / cell).toInt()
    for (row in 0 until rows) for (col in 0 until cols) {
        val x = col * cell
        val y = row * cell
        val bg = r.nextInt(p.size)
        val fg = (bg + 1 + r.nextInt(p.size - 1)) % p.size
        drawRect(p[bg], Offset(x, y), Size(cell + 1f, cell + 1f))
        val corner = r.nextInt(4)
        val center = when (corner) {
            0 -> Offset(x, y)
            1 -> Offset(x + cell, y)
            2 -> Offset(x + cell, y + cell)
            else -> Offset(x, y + cell)
        }
        if (r.nextFloat() < 0.8f) {
            drawArc(p[fg], corner * 90f, 90f, true, center - Offset(cell, cell), Size(cell * 2, cell * 2))
        } else {
            drawCircle(p[fg], cell * 0.3f, Offset(x + cell / 2, y + cell / 2))
        }
    }
}

/** 먹 붓질과 번짐, 오른쪽 아래 붉은 낙관. */
private fun DrawScope.strokes(r: Random, p: List<Color>) {
    val w = size.width
    val h = size.height
    val m = size.minDimension
    fun px() = w * (0.12f + r.nextFloat() * 0.76f)
    fun py() = h * (0.1f + r.nextFloat() * 0.75f)
    drawRect(p[0])
    drawCircle(p[2].copy(alpha = 0.18f), m * 0.45f, Offset(px(), py()))
    repeat(3 + r.nextInt(3)) { i ->
        val path = Path().apply {
            moveTo(px(), py())
            cubicTo(px(), py(), px(), py(), px(), py())
        }
        val width = m * if (i == 0) 0.07f else 0.012f + r.nextFloat() * 0.035f
        drawPath(
            path,
            p[1].copy(alpha = 0.7f + r.nextFloat() * 0.25f),
            style = Stroke(width, cap = StrokeCap.Round),
        )
    }
    repeat(14) { drawCircle(p[1].copy(alpha = 0.6f), m * (0.002f + r.nextFloat() * 0.006f), Offset(px(), py())) }
    val seal = m * 0.06f
    drawRect(p[3], Offset(w * 0.86f - seal, h * 0.93f - seal), Size(seal, seal))
}

/** 면을 재귀적으로 나누고 검은 선으로 구획한다. */
private fun DrawScope.blocks(r: Random, p: List<Color>) {
    val line = size.minDimension * 0.014f
    drawRect(p[1])
    fun split(rect: Rect, depth: Int) {
        val stop = depth >= 4 ||
            (depth >= 2 && r.nextFloat() < 0.35f) ||
            min(rect.width, rect.height) < size.minDimension * 0.18f
        if (stop) {
            val roll = r.nextFloat()
            val color = when {
                roll < 0.62f -> p[0]
                roll < 0.8f -> p[2]
                roll < 0.93f -> p[3]
                else -> p[1]
            }
            drawRect(color, Offset(rect.left + line / 2, rect.top + line / 2), Size(rect.width - line, rect.height - line))
            return
        }
        val t = 0.3f + r.nextFloat() * 0.4f
        if (rect.width >= rect.height) {
            val x = rect.left + rect.width * t
            split(Rect(rect.left, rect.top, x, rect.bottom), depth + 1)
            split(Rect(x, rect.top, rect.right, rect.bottom), depth + 1)
        } else {
            val y = rect.top + rect.height * t
            split(Rect(rect.left, rect.top, rect.right, y), depth + 1)
            split(Rect(rect.left, y, rect.right, rect.bottom), depth + 1)
        }
    }
    split(Rect(line / 2, line / 2, size.width - line / 2, size.height - line / 2), 0)
}
