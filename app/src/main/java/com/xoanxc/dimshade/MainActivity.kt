package com.xoanxc.dimshade

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xoanxc.dimshade.ui.theme.DimShadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Preparamos el archivo para guardar la configuración
        val sharedPreferences = getSharedPreferences("DimShadePrefs", Context.MODE_PRIVATE)

        setContent {
            DimShadeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DimmerScreen(sharedPreferences)
                }
            }
        }
    }
}

@Composable
fun DimmerScreen(sharedPrefs: SharedPreferences) {
    // Leemos el valor guardado (por defecto 128, que es la mitad de 255)
    var sliderValue by remember {
        mutableStateOf(sharedPrefs.getInt("dim_alpha", 128) / 255f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Intensidad del Filtro: ${(sliderValue * 100).toInt()}%",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Slider modificado con el límite de seguridad del 95%
        Slider(
            value = sliderValue,
            onValueChange = { newValue ->
                sliderValue = newValue
                // El valor máximo que pasará será 0.94 * 255 = ~240 (podría usar 0.95, pero eso da aprox un 242, que es un numero más desagradable)
                sharedPrefs.edit().putInt("dim_alpha", (newValue * 255).toInt()).apply()
            },
            valueRange = 0f..0.95f, // <-- ¡Límite del 95%!
            modifier = Modifier.fillMaxWidth()
        )
    }
}