package com.salon.art.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salon.art.data.Artwork
import com.salon.art.data.SalonArtIndex
import com.salon.art.data.Stage
import com.salon.art.data.change
import com.salon.art.ui.theme.Palette
import com.salon.art.ui.theme.SalonType
import java.util.Locale

@Composable
fun MarketScreen(artworks: List<Artwork>, gridState: LazyGridState, onOpen: (String) -> Unit) {
    val listed = artworks.filter { it.stage == Stage.ON_SALE }
    val artists = listed.map { it.artist }.distinct()
    var artist by rememberSaveable { mutableStateOf<String?>(null) }
    val shown = if (artist == null) listed else listed.filter { it.artist == artist }
    val top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = top + 20.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        item(key = "header", span = { GridItemSpan(maxLineSpan) }) { Header() }
        item(key = "index", span = { GridItemSpan(maxLineSpan) }) { IndexCard() }
        item(key = "filter", span = { GridItemSpan(maxLineSpan) }) {
            ArtistFilter(artists, artist, shown.size) { artist = it }
        }
        items(shown, key = { it.id }) { art -> ArtCard(art) { onOpen(art.id) } }
    }
}

@Composable
private fun Header() {
    Column {
        Text("SALON", style = SalonType.Wordmark)
        Spacer(Modifier.height(6.dp))
        Caps("Art Resale & Consignment")
    }
}

@Composable
private fun IndexCard() {
    val change = SalonArtIndex.change()
    Row(
        Modifier.fillMaxWidth().background(Palette.Wall).padding(20.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(Modifier.weight(1f)) {
            Caps("Salon Art Index")
            Spacer(Modifier.height(10.dp))
            Text(String.format(Locale.US, "%,.2f", SalonArtIndex.last()), style = SalonType.Heading)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(formatChange(change), color = changeColor(change), style = SalonType.Label.copy(fontSize = 11.sp))
                Spacer(Modifier.width(8.dp))
                Caps("24개월")
            }
        }
        IndexChart(SalonArtIndex, Modifier.width(128.dp).height(60.dp))
    }
}

@Composable
private fun ArtistFilter(artists: List<String>, selected: String?, count: Int, onSelect: (String?) -> Unit) {
    Column {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item { Chip("전체", selected == null) { onSelect(null) } }
            items(artists) { name -> Chip(name, selected == name) { onSelect(name) } }
        }
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Caps("On View", color = Palette.Ink)
            Spacer(Modifier.weight(1f))
            Caps("$count works")
        }
        Spacer(Modifier.height(12.dp))
        Hairline()
    }
}

@Composable
fun Chip(text: String, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(50)
    Box(
        Modifier
            .border(1.dp, if (selected) Palette.Ink else Palette.Hairline, shape)
            .background(if (selected) Palette.Ink else Palette.Paper, shape)
            .clickable(interactionSource = null, indication = null, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(
            text,
            style = SalonType.Body.copy(fontSize = 13.sp, lineHeight = 16.sp),
            color = if (selected) Palette.Paper else Palette.Ink,
        )
    }
}

@Composable
private fun ArtCard(art: Artwork, onClick: () -> Unit) {
    Column(Modifier.clickable(interactionSource = null, indication = null, onClick = onClick)) {
        Box {
            WallTile(art, Modifier.fillMaxWidth().aspectRatio(1f))
            if (art.mine) Caps("My", Modifier.padding(10.dp), color = Palette.Ink)
        }
        Spacer(Modifier.height(12.dp))
        Caps(art.artist)
        Spacer(Modifier.height(4.dp))
        Text(art.title, style = SalonType.Title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(6.dp))
        if (art.sold) SoldMark() else Text(formatPrice(art.price), style = SalonType.Price)
    }
}
