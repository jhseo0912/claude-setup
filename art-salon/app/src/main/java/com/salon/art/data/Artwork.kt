package com.salon.art.data

import android.net.Uri
import androidx.compose.ui.graphics.Color

enum class ArtStyle { FIELD, ORBIT, GRID, ARCS, STROKES, BLOCKS }

sealed interface ArtSource {
    /** 서버 이미지 대신 Canvas로 그리는 목업 작품. palette[0]이 바탕색이다. */
    data class Generative(val style: ArtStyle, val seed: Int, val palette: List<Color>) : ArtSource

    /** 사용자가 직접 올린 작품 사진. */
    data class Photo(val uri: Uri) : ArtSource
}

enum class Stage(val label: String) {
    RECEIVED("신청 접수"),
    INBOUND("입고"),
    APPRAISAL("감정"),
    ON_SALE("판매 중"),
    SETTLED("정산"),
}

/** 직접 판매는 판매자가 보관하다 팔리면 감정을 거치고, 위탁 판매는 Salon이 먼저 입고와 감정을 맡는다. */
enum class SaleType(val label: String, val summary: String, val fee: Float, val stages: List<Stage>) {
    DIRECT(
        "직접 판매", "작품을 직접 보관하다가 판매되면 Salon으로 보냅니다", 0.05f,
        listOf(Stage.RECEIVED, Stage.ON_SALE, Stage.APPRAISAL, Stage.SETTLED),
    ),
    CONSIGN(
        "위탁 판매", "보관, 감정, 촬영, 판매를 Salon이 대행합니다", 0.09f,
        listOf(Stage.RECEIVED, Stage.INBOUND, Stage.APPRAISAL, Stage.ON_SALE, Stage.SETTLED),
    ),
}

data class Artwork(
    val id: String,
    val title: String,
    val artist: String,
    val year: Int,
    val medium: String,
    val dimensions: String,
    val price: Long,
    val description: String,
    val aspect: Float,
    val source: ArtSource,
    val grade: String = "A",
    val certificate: Boolean = true,
    val framed: Boolean = false,
    val saleType: SaleType = SaleType.CONSIGN,
    val stage: Stage = Stage.ON_SALE,
    val sold: Boolean = false,
    val collected: Boolean = false,
    val mine: Boolean = false,
) {
    val appraised: Boolean
        get() = saleType.stages.indexOf(stage) >= saleType.stages.indexOf(Stage.APPRAISAL)
}
