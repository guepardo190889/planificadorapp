package com.blackdeath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.blackdeath.planificadorapp.navegacion.NavegacionController
import com.blackdeath.planificadorapp.ui.theme.PlanificadorappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlanificadorappTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavegacionController()
                }
            }
        }
    }
}
