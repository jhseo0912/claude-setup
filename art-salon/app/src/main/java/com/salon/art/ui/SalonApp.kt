package com.salon.art.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salon.art.SalonViewModel
import com.salon.art.ui.theme.Palette
import com.salon.art.ui.theme.SalonType

enum class SalonTab(val label: String) { MARKET("마켓"), SELL("판매"), MY("내 거래") }

@Composable
fun SalonApp(vm: SalonViewModel = viewModel()) {
    var tab by rememberSaveable { mutableStateOf(SalonTab.MARKET) }
    var detailId by rememberSaveable { mutableStateOf<String?>(null) }
    val gridState = rememberLazyGridState()

    BackHandler(enabled = detailId != null || tab != SalonTab.MARKET) {
        if (detailId != null) detailId = null else tab = SalonTab.MARKET
    }

    AnimatedContent(
        targetState = detailId,
        transitionSpec = {
            (fadeIn(tween(320)) + slideInVertically(tween(320)) { it / 20 }) togetherWith fadeOut(tween(180))
        },
        label = "detail",
    ) { id ->
        val art = id?.let(vm::find)
        if (art != null) {
            DetailScreen(art, onBack = { detailId = null }, onPurchase = { vm.purchase(art.id) })
        } else {
            val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
            Column(Modifier.fillMaxSize().imePadding()) {
                Crossfade(tab, Modifier.weight(1f), label = "tab") { current ->
                    when (current) {
                        SalonTab.MARKET -> MarketScreen(vm.artworks, gridState) { detailId = it }
                        SalonTab.SELL -> SellScreen { vm.submit(it); tab = SalonTab.MY }
                        SalonTab.MY -> MyScreen(vm.artworks, onOpen = { detailId = it }, onSell = { tab = SalonTab.SELL })
                    }
                }
                if (!imeVisible) TabBar(tab) { tab = it }
            }
        }
    }
}

@Composable
private fun TabBar(current: SalonTab, onSelect: (SalonTab) -> Unit) {
    Column(Modifier.background(Palette.Paper)) {
        Hairline()
        Row(Modifier.fillMaxWidth().navigationBarsPadding().height(60.dp)) {
            SalonTab.entries.forEach { tab ->
                val selected = tab == current
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(interactionSource = null, indication = null) { onSelect(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        tab.label,
                        style = SalonType.Body.copy(
                            fontSize = 13.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        ),
                        color = if (selected) Palette.Ink else Palette.Graphite,
                    )
                    Spacer(Modifier.height(6.dp))
                    Box(Modifier.size(4.dp).background(if (selected) Palette.Ink else Color.Transparent, CircleShape))
                }
            }
        }
    }
}
