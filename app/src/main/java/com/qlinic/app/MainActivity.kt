package com.qlinic.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.qlinic.app.ui.navigation.QlinicNavHost
import com.qlinic.app.ui.theme.QlinicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QlinicTheme {
                QlinicNavHost()
            }
        }
    }
}
