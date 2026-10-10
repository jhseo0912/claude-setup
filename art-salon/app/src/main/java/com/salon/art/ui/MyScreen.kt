package com.salon.art.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salon.art.data.Artwork
import com.salon.art.ui.theme.Palette
import com.salon.art.ui.theme.SalonType

@Composable
fun MyScreen(artworks: List<Artwork>, onOpen: (String) -> Unit, onSell: () -> Unit) {
    val selling = artworks.filter { it.mine }
    val collected = artworks.filter { it.collected }
    val top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = top + 20.dp, bottom = 32.dp),
    ) {
        item {
            Caps("My Salon")
            Spacer(Modifier.height(8.dp))
            Text("내 거래", style = SalonType.Heading)
            Spacer(Modifier.height(32.dp))
            SectionHead("판매", selling.size)
        }
        if (selling.isEmpty()) {
            item { EmptyNote("판매 신청한 작품이 없습니다.", "판매 신청하기", onSell) }
        }
        items(selling, key = { it.id }) { art ->
            Column(Modifier.clickable(interactionSource = null, indication = null) { onOpen(art.id) }) {
                ArtRow(art, art.saleType.label)
                StageTrack(art)
                Spacer(Modifier.height(20.dp))
                Hairline()
            }
        }
        item {
            Spacer(Modifier.height(40.dp))
            SectionHead("구매", collected.size)
        }
        if (collected.isEmpty()) {
            item { EmptyNote("구매한 작품이 없습니다.") }
        }
        items(collected, key = { "c" + it.id }) { art ->
            Column(Modifier.clickable(interactionSource = null, indication = null) { onOpen(art.id) }) {
                ArtRow(art, "결제 완료, 안심 배송 준비 중")
                Hairline()
            }
        }
    }
}

@Composable
private fun SectionHead(title: String, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Caps(title, color = Palette.Ink)
        Spacer(Modifier.weight(1f))
        Caps("$count")
    }
    Spacer(Modifier.height(12.dp))
    Hairline()
}

@Composable
private fun ArtRow(art: Artwork, caption: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
        WallTile(art, Modifier.size(76.dp), padding = 10.dp, elevation = 3.dp)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Caps(caption)
            Spacer(Modifier.height(4.dp))
            Text(art.title, style = SalonType.Title, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(4.dp))
            Text(formatPrice(art.price), style = SalonType.Price)
        }
    }
}

@Composable
private fun EmptyNote(text: String, action: String? = null, onAction: () -> Unit = {}) {
    Column(Modifier.fillMaxWidth().padding(vertical = 28.dp)) {
        Text(text, style = SalonType.Body, color = Palette.Graphite)
        if (action != null) {
            Spacer(Modifier.height(16.dp))
            GhostButton(action, onAction, Modifier.fillMaxWidth())
        }
    }
    Hairline()
}
