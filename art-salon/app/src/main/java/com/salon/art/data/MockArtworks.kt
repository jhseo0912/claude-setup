package com.salon.art.data

import androidx.compose.ui.graphics.Color

private object Palettes {
    val Terracotta = listOf(Color(0xFFE8DFD0), Color(0xFFB5533C), Color(0xFF2B2B2B), Color(0xFFD9A441))
    val Indigo = listOf(Color(0xFFE9E4D8), Color(0xFF1F2A44), Color(0xFF8C3B2E), Color(0xFFC9B79C))
    val Sage = listOf(Color(0xFFDCE0D5), Color(0xFF2F3A2F), Color(0xFF6E7F68), Color(0xFFC7A27A))
    val Ochre = listOf(Color(0xFFEFE6D2), Color(0xFF3B2F2A), Color(0xFFD9A441), Color(0xFFA23E2A))
    val Noir = listOf(Color(0xFF151515), Color(0xFF8C2F23), Color(0xFF3A3A3A), Color(0xFFC9B79C))
    val Blush = listOf(Color(0xFFF4EEE6), Color(0xFF3D3A4B), Color(0xFFC46F5E), Color(0xFFEAD7CF))
    val InkPaper = listOf(Color(0xFFF2EDE3), Color(0xFF1A1A1A), Color(0xFF7A7266), Color(0xFFB22A1E))
}

private fun gen(style: ArtStyle, seed: Int, palette: List<Color>) = ArtSource.Generative(style, seed, palette)

val MockArtworks: List<Artwork> = listOf(
    Artwork(
        id = "a01", title = "Field No. 7", artist = "Han Ji-woo", year = 2024,
        medium = "캔버스에 유채", dimensions = "162 x 130 cm", price = 8_400_000,
        description = "겹겹이 쌓은 얇은 색면이 서로의 경계를 흐립니다. 가까이 설수록 색이 숨을 쉬듯 번져 보이는 작가의 대표 연작입니다.",
        aspect = 0.8f, source = gen(ArtStyle.FIELD, 7, Palettes.Terracotta),
        grade = "A+", framed = true,
    ),
    Artwork(
        id = "a02", title = "Orbit, Quiet", artist = "Mara Lindqvist", year = 2023,
        medium = "종이에 과슈", dimensions = "56 x 56 cm", price = 2_800_000,
        description = "두 개의 원이 서로를 당기는 순간을 정지된 화면에 붙잡았습니다. 북유럽의 낮은 햇빛에서 출발한 작업입니다.",
        aspect = 1f, source = gen(ArtStyle.ORBIT, 21, Palettes.Indigo),
        grade = "A",
    ),
    Artwork(
        id = "a03", title = "Lines for a Morning", artist = "Seo Eun-jae", year = 2025,
        medium = "캔버스에 흑연과 아크릴", dimensions = "100 x 100 cm", price = 5_600_000,
        description = "자 없이 손으로 그은 수백 개의 수평선입니다. 흔들림이 그대로 남아 화면 전체가 미세하게 진동합니다.",
        aspect = 1f, source = gen(ArtStyle.GRID, 3, Palettes.Sage),
        grade = "A",
    ),
    Artwork(
        id = "a04", title = "Quarter Sun", artist = "Teodor Vass", year = 2022,
        medium = "실크스크린, 에디션 12/30", dimensions = "70 x 90 cm", price = 1_200_000,
        description = "사분원 하나로 만든 리듬입니다. 바우하우스의 조형 문법을 오늘의 색으로 다시 찍었습니다.",
        aspect = 0.78f, source = gen(ArtStyle.ARCS, 42, Palettes.Ochre),
        grade = "A", certificate = false,
    ),
    Artwork(
        id = "a05", title = "Ink Weather", artist = "Park Do-hyun", year = 2024,
        medium = "한지에 먹", dimensions = "140 x 70 cm", price = 4_300_000,
        description = "비가 오기 직전의 공기를 먹의 농담으로 옮겼습니다. 오른쪽 아래의 붉은 낙관이 화면의 무게를 잡아 줍니다.",
        aspect = 0.5f, source = gen(ArtStyle.STROKES, 11, Palettes.InkPaper),
        saleType = SaleType.DIRECT, framed = true,
    ),
    Artwork(
        id = "a06", title = "Composition in Clay", artist = "Inès Moreau", year = 2021,
        medium = "패널에 유채", dimensions = "80 x 100 cm", price = 6_900_000,
        description = "흙빛 면들을 나누고 다시 나눈 구성 연작입니다. 검은 선이 면 사이의 긴장을 만듭니다.",
        aspect = 0.8f, source = gen(ArtStyle.BLOCKS, 5, Palettes.Blush), sold = true,
        grade = "A+", framed = true,
    ),
    Artwork(
        id = "a07", title = "Night Field", artist = "Han Ji-woo", year = 2025,
        medium = "캔버스에 유채", dimensions = "130 x 97 cm", price = 7_200_000,
        description = "Field 연작 가운데 가장 어두운 작품입니다. 검은 바탕 위로 붉은 면이 천천히 떠오릅니다.",
        aspect = 0.75f, source = gen(ArtStyle.FIELD, 19, Palettes.Noir),
        grade = "A+",
    ),
    Artwork(
        id = "a08", title = "Two Moons", artist = "Kim Na-rae", year = 2024,
        medium = "리넨에 아크릴", dimensions = "60 x 60 cm", price = 3_100_000,
        description = "같은 하늘에 뜬 두 개의 달이라는 상상에서 출발했습니다. 단순한 형태와 따뜻한 색의 대비가 돋보입니다.",
        aspect = 1f, source = gen(ArtStyle.ORBIT, 8, Palettes.Ochre), sold = true,
        grade = "B+",
    ),
    Artwork(
        id = "a09", title = "Margin", artist = "Seo Eun-jae", year = 2023,
        medium = "종이에 흑연", dimensions = "42 x 59 cm", price = 1_800_000,
        description = "여백과 선 사이의 거리를 재는 드로잉입니다. 옅은 분홍 띠가 시간의 흐름을 표시합니다.",
        aspect = 0.71f, source = gen(ArtStyle.GRID, 27, Palettes.Blush),
        saleType = SaleType.DIRECT,
    ),
    Artwork(
        id = "a10", title = "Gesture III", artist = "Park Do-hyun", year = 2025,
        medium = "캔버스에 먹과 아크릴", dimensions = "120 x 90 cm", price = 5_100_000,
        description = "한 번의 호흡으로 그은 붓질만 남겼습니다. 고쳐 그리지 않는 것이 이 연작의 유일한 규칙입니다.",
        aspect = 1.33f, source = gen(ArtStyle.STROKES, 33, Palettes.Indigo),
        grade = "A",
    ),
    Artwork(
        id = "a11", title = "Garden Study", artist = "Lee Seo-yeon", year = 2024,
        medium = "목판화, 에디션 5/20", dimensions = "50 x 50 cm", price = 900_000,
        description = "정원의 화단을 위에서 내려다본 구조를 사분원 패턴으로 옮겼습니다.",
        aspect = 1f, source = gen(ArtStyle.ARCS, 15, Palettes.Sage),
        grade = "A", framed = true,
    ),
    Artwork(
        id = "a12", title = "Grid of Noon", artist = "Mara Lindqvist", year = 2022,
        medium = "캔버스에 아크릴", dimensions = "90 x 110 cm", price = 4_700_000,
        description = "정오의 그림자가 가장 짧아지는 순간의 구획입니다. 노란 면이 빛이 머무는 자리를 가리킵니다.",
        aspect = 0.82f, source = gen(ArtStyle.BLOCKS, 12, Palettes.Ochre),
        grade = "B+",
    ),
)
