package com.project.learningkitsupplier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.project.learningkitsupplier.navigation.NavigationHost
import com.tagsamurai.tscomponents.navigation.ModuleScreen
import com.tagsamurai.tscomponents.theme.LocalTheme
import com.tagsamurai.tscomponents.theme.TagSamuraiTheme
import com.tagsamurai.tscomponents.theme.Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalTheme provides Theme(module = ModuleScreen.SupplyAsset),
            ) {
                TagSamuraiTheme {
                    NavigationHost()
                }
            }
        }
    }
}