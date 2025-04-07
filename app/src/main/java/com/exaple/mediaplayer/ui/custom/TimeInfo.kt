package com.exaple.mediaplayer.ui.custom

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Componente que muestra el tiempo de reproducción formateado
 *
 * @param modifier Modificador para personalizar el layout del componente
 * @param time Tiempo actual en milisegundos que será formateado
 */
@Composable
fun TimeInfo(
    modifier: Modifier = Modifier,  // Modificador por defecto vacío
    time: Long  // Tiempo en ms a mostrar
) {
    // Contenedor que centra el texto
    Box(
        modifier = modifier,  // Aplica los modificadores recibidos
        contentAlignment = Alignment.Center  // Centra el contenido
    ) {
        // Texto que muestra el tiempo formateado
        Text(
            text = formatTime(time),  // Llama a la función de formateo
            color = Color.White  // Color blanco para mejor visibilidad
        )
    }
}

/**
 * Función de utilidad para formatear milisegundos a formato HH:MM:SS
 *
 * @param millis Tiempo en milisegundos a formatear
 * @return String con el tiempo formateado (ej: "1:23:45")
 */
@SuppressLint("DefaultLocale")  // Suprime warning de formato localizado
fun formatTime(millis: Long): String {
    // Conversión y cálculos:
    val totalSeconds = millis / 1000  // Convierte ms a segundos
    val hours = totalSeconds / 3600  // Calcula horas completas
    val remainingSeconds = totalSeconds % 3600  // Segundos restantes
    val minutes = remainingSeconds / 60  // Calcula minutos
    val seconds = remainingSeconds % 60  // Segundos finales

    // Formatea a 2 dígitos para minutos/segundos, 1 para horas
    return String.format("%01d:%02d:%02d", hours, minutes, seconds)
}