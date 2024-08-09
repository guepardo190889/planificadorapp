package com.example.planificadorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.planificadorapp.composables.BarraSuperior
import com.example.planificadorapp.configuracion.NavegacionController
import com.example.planificadorapp.ui.theme.PlanificadorappTheme
import kotlinx.coroutines.launch

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
