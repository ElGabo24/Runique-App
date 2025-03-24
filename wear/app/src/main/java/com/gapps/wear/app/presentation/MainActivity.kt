package com.gapps.wear.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gapps.core.notification.ActiveRunService
import com.gapps.core.presentation.designsystem_wear.RuniqueTheme
import com.gapps.wear.run.presentation.TrackerScreenRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            RuniqueTheme {
                TrackerScreenRoot(
                    onServiceToggle = { shouldStartRunning ->
                        if (shouldStartRunning) {
                            startService(
                                ActiveRunService.createStartintent(
                                    applicationContext, this::class.java
                                )
                            )
                        } else {
                            startService(
                                ActiveRunService.createStopIntent(applicationContext)
                            )
                        }
                    }
                )
            }
        }
    }
}