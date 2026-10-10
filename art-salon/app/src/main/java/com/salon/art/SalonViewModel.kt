package com.salon.art

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salon.art.data.Artwork
import com.salon.art.data.MockArtworks
import com.salon.art.data.Stage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** 서버 대신 메모리에 목록을 들고 있는 목업 저장소. 앱을 다시 켜면 초기 상태로 돌아간다. */
class SalonViewModel : ViewModel() {
    val artworks = mutableStateListOf<Artwork>().apply { addAll(MockArtworks) }

    fun find(id: String): Artwork? = artworks.firstOrNull { it.id == id }

    fun purchase(id: String) = update(id) { it.copy(sold = true, collected = true) }

    /** 판매 신청을 받고, 서버 처리 대신 몇 초 간격으로 판매 중 단계까지 진행시킨다. */
    fun submit(artwork: Artwork) {
        artworks.add(0, artwork)
        viewModelScope.launch {
            for (next in artwork.saleType.stages.drop(1)) {
                delay(STEP_MILLIS)
                update(artwork.id) { it.copy(stage = next) }
                if (next == Stage.ON_SALE) break
            }
        }
    }

    private fun update(id: String, change: (Artwork) -> Artwork) {
        val i = artworks.indexOfFirst { it.id == id }
        if (i >= 0) artworks[i] = change(artworks[i])
    }

    private companion object {
        const val STEP_MILLIS = 3_000L
    }
}
