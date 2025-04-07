package com.exaple.mediaplayer.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exaple.mediaplayer.ui.viewmodels.ExoplayerViewModel
import com.exaple.mediaplayer.ui.viewmodels.PlayerViewModel

/**
 * Componente de reproductor personalizado que muestra:
 * - Tiempo transcurrido
 * - Slider de progreso interactivo
 * - Duración total
 *
 * Diseñado para usarse dentro de un RowScope
 *
 * @param viewModel ViewModel que provee los estados del reproductor
 * @param backGround Color de fondo para los indicadores de tiempo
 */
@Composable
fun RowScope.SliderCustom(
    viewModel: PlayerViewModel = ExoplayerViewModel, // ViewModel por defecto
    backGround: Color // Color de fondo para los textos
) {
    // Estados observables del reproductor
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val duration by viewModel.duration.collectAsStateWithLifecycle()

    // Espaciado inicial
    Spacer(Modifier.size(10.dp))

    // Indicador de tiempo transcurrido
    TimeInfo(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp)) // Bordes redondeados
            .background(backGround) // Fondo personalizado
            .padding(start = 5.dp, end = 5.dp), // Padding interno
        time = currentPosition // Tiempo actual
    )

    // Slider interactivo de progreso
    SliderPlayer(
        modifier = Modifier
            .weight(1f) // Ocupa el espacio disponible
            .padding(start = 10.dp, end = 10.dp), // Margen horizontal
        position = currentPosition,
        duration = duration,
        onTapOrDragEnd = { newPos ->
            viewModel.seekTo(newPos) // Actualiza posición al interactuar
        },
        timeInfo = true // Muestra tiempo durante el arrastre
    )

    // Indicador de duración total
    TimeInfo(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backGround)
            .padding(start = 5.dp, end = 5.dp),
        time = duration
    )

    // Espaciado final
    Spacer(Modifier.size(10.dp))
}