package com.salon.art

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.salon.art.ui.SalonApp
import com.salon.art.ui.theme.Palette
import com.salon.art.ui.theme.SalonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lightBars = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = lightBars, navigationBarStyle = lightBars)
        setContent {
            SalonTheme {
                Surface(Modifier.fillMaxSize(), color = Palette.Paper, contentColor = Palette.Ink) {
                    SalonApp()
                }
            }
        }
    }
}
