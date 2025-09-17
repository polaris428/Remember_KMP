package com.devlog.remember

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.devlog.remember.utils.Analytics
import com.devlog.remember.utils.AndroidAnalytics
import com.mixpanel.android.mpmetrics.MixpanelAPI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val trackAutomaticEvents = false;
        Analytics.install(AndroidAnalytics(MixpanelAPI.getInstance(applicationContext,"cb6660b7534b41cbba6bf01b403eb908",trackAutomaticEvents )))
        Analytics.track("App_Opened")
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}