package com.exaple.mediaplayer.ui.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exaple.mediaplayer.R
import com.exaple.mediaplayer.ui.viewmodels.ExoplayerViewModel
import com.exaple.mediaplayer.ui.viewmodels.PlayerViewModel

/**
 * Barra de controles inferiores del reproductor que incluye:
 * - Controles de reproducción (anterior/pausa-play/siguiente)
 * - Control de volumen (slider + mute)
 * - Acceso a configuración de idiomas
 *
 * @param viewModel ViewModel que gestiona el estado del reproductor
 * @param backGround Color de fondo para los iconos de control
 */
@Composable
fun BottomControls(
    viewModel: PlayerViewModel = ExoplayerViewModel,
    backGround: Color
) {
    // Estados observados del reproductor
    val currentVolume by viewModel.currentVolume.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val isMute by viewModel.isMute.collectAsStateWithLifecycle()
    val showLanguageItems by viewModel.showLanguageItems.collectAsStateWithLifecycle()

    // Fila principal de controles de reproducción
    Row(
        modifier = Modifier
            .height(40.dp)  // Altura fija para la fila
            .fillMaxWidth(),  // Ancho completo
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center  // Centrado horizontal
    ) {
        // Botón: Anterior
        IconControls(
            source = R.drawable.back,
            contentDescription = "Pista anterior",
            onTap = { viewModel.goToPreviousMedia() },
            backGround = backGround
        )

        Spacer(Modifier.size(10.dp))  // Separación entre botones

        // Botón: Play/Pause (cambia según estado actual)
        if (isPlaying) {
            IconControls(
                source = R.drawable.pausa,
                contentDescription = "Pausar",
                onTap = { viewModel.pause() },
                backGround = backGround
            )
        } else {
            IconControls(
                source = R.drawable.play,
                contentDescription = "Reproducir",
                onTap = { viewModel.play() },
                backGround = backGround
            )
        }

        Spacer(Modifier.size(10.dp))

        // Botón: Siguiente
        IconControls(
            source = R.drawable.next,
            contentDescription = "Pista siguiente",
            onTap = { viewModel.goToNextMedia() },
            backGround = backGround
        )
    }

    // Fila secundaria de controles adicionales (derecha de la pantalla)
    Row(
        modifier = Modifier.fillMaxSize(),  // Ocupa el espacio disponible
        horizontalArrangement = Arrangement.End,  // Alineado a la derecha
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón: Mute/Unmute (cambia según estado actual)
        if (isMute) {
            IconControls(
                source = R.drawable.mute,
                contentDescription = "Activar sonido",
                onTap = { viewModel.mute(false) },
                backGround = backGround
            )
        } else {
            IconControls(
                source = R.drawable.volume_on,
                contentDescription = "Silenciar",
                onTap = { viewModel.mute(true) },
                backGround = backGround
            )
        }

        // Control deslizante de volumen
        SliderPlayer(
            modifier = Modifier
                .width(100.dp)  // Ancho fijo para el slider
                .padding(start = 10.dp, end = 10.dp),  // Margen horizontal
            position = (currentVolume * 100).toLong(),  // Convierte a rango 0-100
            duration = 100,  // Rango máximo del slider
            onDrag = { newPos -> viewModel.setVolume(newPos) }  // Actualiza volumen
        )

        Spacer(Modifier.size(10.dp))

        // Botón: Configuración de idiomas
        IconControls(
            source = R.drawable.settings,
            contentDescription = "Opciones de idioma",
            onTap = { viewModel.showLanguageItems(!showLanguageItems) },
            backGround = backGround
        )

        Spacer(Modifier.size(20.dp))  // Margen final derecho
    }
}