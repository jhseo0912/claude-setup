package com.salon.art.data

import kotlin.random.Random

/** 작가별 호당 시세(만원) 12개월 목업. 작가 이름으로 시드를 고정해 매번 같은 곡선이 나온다. */
fun artistIndex(artist: String): List<Float> {
    val r = Random(artist.hashCode())
    var v = 18f + r.nextInt(70)
    return List(12) { i ->
        if (i > 0) v *= 1f + (r.nextFloat() - 0.4f) * 0.09f
        v
    }
}

/** 마켓 전체 지수 24개월 목업. */
val SalonArtIndex: List<Float> = run {
    val r = Random(2026)
    var v = 1000f
    List(24) { i ->
        if (i > 0) v *= 1f + (r.nextFloat() - 0.42f) * 0.04f
        v
    }
}

fun List<Float>.change(): Float = last() / first() - 1f
