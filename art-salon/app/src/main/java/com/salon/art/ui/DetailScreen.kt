package com.salon.art.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.salon.art.data.Artwork
import com.salon.art.data.artistIndex
import com.salon.art.data.change
import com.salon.art.ui.theme.Palette
import com.salon.art.ui.theme.SalonType
import kotlin.math.roundToInt

@Composable
fun DetailScreen(art: Artwork, onBack: () -> Unit, onPurchase: () -> Unit) {
    var confirming by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().background(Palette.Paper)) {
        Box(Modifier.weight(1f)) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Palette.Wall)
                        .statusBarsPadding()
                        .padding(start = 40.dp, end = 40.dp, top = 64.dp, bottom = 48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    ArtworkImage(art, Modifier.heightIn(max = 420.dp).shadow(14.dp, RectangleShape))
                }
                Column(Modifier.padding(horizontal = 24.dp, vertical = 28.dp)) {
                    Caps(art.artist)
                    Spacer(Modifier.height(8.dp))
                    Text(art.title, style = SalonType.Display)
                    Spacer(Modifier.height(6.dp))
                    Text("${art.year}, ${art.medium}", style = SalonType.Body, color = Palette.Graphite)
                    Spacer(Modifier.height(32.dp))
                    AppraisalCard(art)
                    Spacer(Modifier.height(40.dp))
                    ArtistIndexSection(art.artist)
                    Spacer(Modifier.height(40.dp))
                    Caps("작품 정보", color = Palette.Ink)
                    Spacer(Modifier.height(12.dp))
                    SpecRow("재료", art.medium)
                    SpecRow("크기", art.dimensions)
                    SpecRow("제작연도", art.year.toString())
                    SpecRow("판매 방식", art.saleType.label)
                    Hairline()
                    Spacer(Modifier.height(24.dp))
                    Text(art.description, style = SalonType.Body, color = Palette.Ink.copy(alpha = 0.82f))
                    Spacer(Modifier.height(40.dp))
                    TradeGuide()
                }
            }
            IconTap(Glyph.Back, "뒤로", onBack, Modifier.statusBarsPadding().padding(8.dp))
        }
        PurchaseBar(art) { confirming = true }
    }

    if (confirming) {
        PurchaseDialog(
            art,
            onConfirm = { confirming = false; onPurchase() },
            onDismiss = { confirming = false },
        )
    }
}

@Composable
private fun AppraisalCard(art: Artwork) {
    val parts = listOfNotNull("보증서".takeIf { art.certificate }, "액자".takeIf { art.framed })
    Column(Modifier.fillMaxWidth().border(1.dp, Palette.Hairline).padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(8.dp)
                    .background(if (art.appraised) Palette.Ink else Palette.Paper, CircleShape)
                    .border(1.dp, Palette.Ink, CircleShape),
            )
            Spacer(Modifier.width(10.dp))
            Caps(if (art.appraised) "Salon 감정 완료" else "구매 후 Salon 감정", color = Palette.Ink)
        }
        Spacer(Modifier.height(18.dp))
        Row {
            Fact("진위", if (art.appraised) "확인 완료" else "감정 예정", Modifier.weight(1f))
            Fact("상태", if (art.appraised) "${art.grade} 등급" else "판매자 기재", Modifier.weight(1f))
            Fact("구성품", parts.joinToString(", ").ifEmpty { "작품 단품" }, Modifier.weight(1f))
        }
    }
}

@Composable
private fun Fact(label: String, value: String, modifier: Modifier) {
    Column(modifier) {
        Caps(label)
        Spacer(Modifier.height(6.dp))
        Text(value, style = SalonType.Body.copy(fontSize = 13.sp, lineHeight = 18.sp))
    }
}

@Composable
private fun ArtistIndexSection(artist: String) {
    val points = artistIndex(artist)
    val change = points.change()
    Caps("Artist Index", color = Palette.Ink)
    Spacer(Modifier.height(4.dp))
    Text("$artist 작품의 호당 거래가", style = SalonType.Body.copy(fontSize = 12.sp), color = Palette.Graphite)
    Spacer(Modifier.height(14.dp))
    Row(verticalAlignment = Alignment.Bottom) {
        Text("${points.last().roundToInt()}만원", style = SalonType.Heading.copy(fontSize = 24.sp))
        Spacer(Modifier.width(10.dp))
        Text(
            formatChange(change),
            color = changeColor(change),
            style = SalonType.Label.copy(fontSize = 11.sp),
            modifier = Modifier.padding(bottom = 6.dp),
        )
    }
    Spacer(Modifier.height(16.dp))
    IndexChart(points, Modifier.fillMaxWidth().height(110.dp))
    Spacer(Modifier.height(8.dp))
    Row {
        Caps("12개월 전")
        Spacer(Modifier.weight(1f))
        Caps("현재")
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Hairline()
    Row(Modifier.padding(vertical = 14.dp)) {
        Text(label, Modifier.width(88.dp), style = SalonType.Body, color = Palette.Graphite)
        Text(value, style = SalonType.Body)
    }
}

@Composable
private fun TradeGuide() {
    Caps("거래 안내", color = Palette.Ink)
    Spacer(Modifier.height(16.dp))
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        GuideRow("01", "안전 결제", "결제 대금은 작품 수령을 확인한 뒤 판매자에게 정산됩니다.")
        GuideRow("02", "Salon 감정", "전문 감정팀이 진위와 상태를 확인하고 감정서를 동봉합니다.")
        GuideRow("03", "안심 배송", "작품 전용 포장과 보험이 적용된 아트 핸들링으로 배송합니다.")
    }
}

@Composable
private fun GuideRow(number: String, title: String, body: String) {
    Row {
        Text(number, Modifier.width(36.dp), style = SalonType.Price, color = Palette.Graphite)
        Column {
            Text(title, style = SalonType.Body.copy(fontWeight = FontWeight.Medium))
            Spacer(Modifier.height(2.dp))
            Text(body, style = SalonType.Body.copy(fontSize = 13.sp, lineHeight = 20.sp), color = Palette.Graphite)
        }
    }
}

@Composable
private fun PurchaseBar(art: Artwork, onBuy: () -> Unit) {
    Column(Modifier.background(Palette.Paper)) {
        Hairline()
        Row(
            Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 24.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                if (art.sold) {
                    Caps("상태")
                    Spacer(Modifier.height(6.dp))
                    SoldMark()
                } else {
                    Caps("판매가")
                    Spacer(Modifier.height(2.dp))
                    Text(formatPrice(art.price), style = SalonType.Price.copy(fontSize = 20.sp))
                }
            }
            when {
                art.collected -> InkButton("소장 완료", {}, Modifier.width(150.dp), enabled = false)
                art.sold -> InkButton("판매 완료", {}, Modifier.width(150.dp), enabled = false)
                art.mine -> InkButton("내 판매 작품", {}, Modifier.width(150.dp), enabled = false)
                else -> InkButton("구매하기", onBuy, Modifier.width(150.dp))
            }
        }
    }
}

@Composable
private fun PurchaseDialog(art: Artwork, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().background(Palette.Paper).padding(28.dp)) {
            Caps("Acquisition")
            Spacer(Modifier.height(14.dp))
            Text(art.title, style = SalonType.Display.copy(fontSize = 24.sp, lineHeight = 30.sp))
            Spacer(Modifier.height(4.dp))
            Text(art.artist, style = SalonType.Body, color = Palette.Graphite)
            Spacer(Modifier.height(24.dp))
            Hairline()
            ReceiptRow("작품가", formatPrice(art.price))
            ReceiptRow("Salon 감정", "포함")
            ReceiptRow("안심 배송", "무료")
            Hairline()
            ReceiptRow("결제 금액", formatPrice(art.price), strong = true)
            Spacer(Modifier.height(12.dp))
            Text(
                "목업 결제입니다. 실제 결제는 진행되지 않습니다.",
                style = SalonType.Body.copy(fontSize = 12.sp),
                color = Palette.Graphite,
            )
            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GhostButton("취소", onDismiss, Modifier.weight(1f))
                InkButton("결제하기", onConfirm, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String, strong: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(label, style = SalonType.Body, color = if (strong) Palette.Ink else Palette.Graphite)
        Spacer(Modifier.weight(1f))
        Text(value, style = if (strong) SalonType.Price.copy(fontSize = 18.sp) else SalonType.Body)
    }
}
