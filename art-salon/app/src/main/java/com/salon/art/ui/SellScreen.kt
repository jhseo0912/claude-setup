package com.salon.art.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.salon.art.data.ArtSource
import com.salon.art.data.Artwork
import com.salon.art.data.SaleType
import com.salon.art.data.Stage
import com.salon.art.ui.theme.Palette
import com.salon.art.ui.theme.SalonType
import java.time.Year
import kotlin.math.roundToLong

@Composable
fun SellScreen(onSubmit: (Artwork) -> Unit) {
    var uri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var aspect by rememberSaveable { mutableFloatStateOf(0.8f) }
    var artist by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }
    var medium by rememberSaveable { mutableStateOf("") }
    var dimensions by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var certificate by rememberSaveable { mutableStateOf(true) }
    var framed by rememberSaveable { mutableStateOf(false) }
    var saleType by rememberSaveable { mutableStateOf(SaleType.CONSIGN) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { picked ->
        if (picked != null) uri = picked
    }
    val won = price.toLongOrNull() ?: 0L
    val ready = uri != null && artist.isNotBlank() && title.isNotBlank() && won > 0

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(20.dp))
            Caps("Sell with Salon")
            Spacer(Modifier.height(8.dp))
            Text("작품 판매 신청", style = SalonType.Heading)
            Spacer(Modifier.height(6.dp))
            Text(
                "신청 후 Salon 감정을 거쳐 마켓에 게시됩니다.",
                style = SalonType.Body,
                color = Palette.Graphite,
            )
            Spacer(Modifier.height(28.dp))

            PhotoPicker(uri, aspect, onAspect = { aspect = it }) {
                picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            Spacer(Modifier.height(36.dp))

            Caps("판매 방식", color = Palette.Ink)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SaleType.entries.forEach { type ->
                    SaleOption(type, type == saleType, Modifier.weight(1f)) { saleType = type }
                }
            }
            Spacer(Modifier.height(36.dp))

            LineField("작가명", artist, { artist = it }, "Han Ji-woo")
            LineField("작품명", title, { title = it }, "Untitled")
            LineField("재료", medium, { medium = it }, "캔버스에 유채")
            LineField("크기", dimensions, { dimensions = it }, "100 x 80 cm")
            LineField(
                "희망 판매가 (원)", price, { price = it.filter(Char::isDigit).take(12) }, "3000000",
                keyboardType = KeyboardType.Number,
                supporting = if (won > 0) formatPrice(won) else null,
            )
            LineField("작품 소개", description, { description = it }, "작품에 얽힌 이야기를 적어 주세요", singleLine = false)

            Caps("구성품", color = Palette.Ink)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Chip("작가 보증서", certificate) { certificate = !certificate }
                Chip("액자", framed) { framed = !framed }
            }
            Spacer(Modifier.height(32.dp))
        }

        Column(Modifier.background(Palette.Paper)) {
            Hairline()
            Column(Modifier.padding(horizontal = 24.dp, vertical = 14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Caps("예상 정산금")
                    Spacer(Modifier.weight(1f))
                    Text(
                        if (won > 0) formatPrice((won * (1 - saleType.fee)).roundToLong()) else "-",
                        style = SalonType.Price.copy(fontSize = 18.sp),
                    )
                }
                Spacer(Modifier.height(12.dp))
                InkButton(
                    "판매 신청하기",
                    onClick = {
                        onSubmit(
                            Artwork(
                                id = "u${System.currentTimeMillis()}",
                                title = title.trim(),
                                artist = artist.trim(),
                                year = Year.now().value,
                                medium = medium.trim().ifEmpty { "미기재" },
                                dimensions = dimensions.trim().ifEmpty { "미기재" },
                                price = won,
                                description = description.trim().ifEmpty { "판매자가 직접 등록한 작품입니다." },
                                aspect = aspect,
                                source = ArtSource.Photo(uri!!),
                                certificate = certificate,
                                framed = framed,
                                saleType = saleType,
                                stage = Stage.RECEIVED,
                                mine = true,
                            ),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = ready,
                )
            }
        }
    }
}

@Composable
private fun PhotoPicker(uri: Uri?, aspect: Float, onAspect: (Float) -> Unit, onPick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Palette.Wall)
            .clickable(interactionSource = null, indication = null, onClick = onPick),
        contentAlignment = Alignment.Center,
    ) {
        if (uri == null) {
            Column(Modifier.padding(vertical = 64.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(48.dp).border(1.dp, Palette.Ink, CircleShape), contentAlignment = Alignment.Center) {
                    LineIcon(Glyph.Plus, Modifier.size(16.dp))
                }
                Spacer(Modifier.height(14.dp))
                Caps("작품 사진 선택", color = Palette.Ink)
                Spacer(Modifier.height(4.dp))
                Text("정면에서 찍은 사진 한 장", style = SalonType.Body.copy(fontSize = 12.sp), color = Palette.Graphite)
            }
        } else {
            Column(Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = uri,
                    contentDescription = "선택한 작품 사진",
                    contentScale = ContentScale.Crop,
                    // 실제 사진 비율을 읽어 작품 비율로 쓴다
                    onSuccess = { state ->
                        val s = state.painter.intrinsicSize
                        if (s.width > 0f && s.height > 0f) onAspect(s.width / s.height)
                    },
                    modifier = Modifier.heightIn(max = 320.dp).aspectRatio(aspect).shadow(10.dp, RectangleShape),
                )
                Spacer(Modifier.height(14.dp))
                Caps("탭하여 사진 변경")
            }
        }
    }
}

@Composable
private fun SaleOption(type: SaleType, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier
            .border(if (selected) 1.5.dp else 1.dp, if (selected) Palette.Ink else Palette.Hairline)
            .clickable(interactionSource = null, indication = null, onClick = onClick)
            .padding(16.dp),
    ) {
        Text(type.label, style = SalonType.Body.copy(fontWeight = FontWeight.Medium))
        Spacer(Modifier.height(6.dp))
        Text(
            type.summary,
            style = SalonType.Body.copy(fontSize = 12.sp, lineHeight = 18.sp),
            color = Palette.Graphite,
            minLines = 2,
        )
        Spacer(Modifier.height(10.dp))
        Caps("수수료 ${(type.fee * 100).toInt()}%", color = if (selected) Palette.Ink else Palette.Graphite)
    }
}

@Composable
private fun LineField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    supporting: String? = null,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    val textStyle = SalonType.Body.copy(fontSize = 16.sp, color = Palette.Ink)

    Column(Modifier.fillMaxWidth().padding(bottom = 26.dp)) {
        Caps(label)
        Spacer(Modifier.height(10.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            singleLine = singleLine,
            minLines = if (singleLine) 1 else 3,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = if (singleLine) ImeAction.Next else ImeAction.Default,
            ),
            cursorBrush = SolidColor(Palette.Ink),
            interactionSource = interaction,
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                Box {
                    if (value.isEmpty()) Text(placeholder, style = textStyle, color = Palette.Graphite.copy(alpha = 0.5f))
                    inner()
                }
            },
        )
        Spacer(Modifier.height(10.dp))
        HorizontalDivider(thickness = 1.dp, color = if (focused) Palette.Ink else Palette.Hairline)
        if (supporting != null) {
            Spacer(Modifier.height(6.dp))
            Text(supporting, style = SalonType.Price.copy(fontSize = 13.sp), color = Palette.Graphite)
        }
    }
}
