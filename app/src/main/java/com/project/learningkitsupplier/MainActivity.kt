package com.project.learningkitsupplier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.learningkitsupplier.navigation.NavigationHost
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.view.CreateSupplierScreen
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.viewmodel.CreateSupplierViewModel
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