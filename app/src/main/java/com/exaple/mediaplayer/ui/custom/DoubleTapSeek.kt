package com.exaple.mediaplayer.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exaple.mediaplayer.R
import com.exaple.mediaplayer.ui.viewmodels.ExoplayerViewModel
import com.exaple.mediaplayer.ui.viewmodels.PlayerViewModel

/**
 * Componente de controles táctiles para:
 * - Navegación rápida (15 segundos adelante/atrás con doble tap)
 * - Toggle de visibilidad de controles UI
 * - Feedback visual durante saltos temporales
 *
 * @param viewModel ViewModel que gestiona el estado del reproductor
 * @param backGround Color de fondo para los iconos de feedback
 */
@Composable
fun DoubleTapSeekControl(
    viewModel: PlayerViewModel = ExoplayerViewModel,
    backGround: Color
) {
    // Estados observados del reproductor
    val showControls by viewModel.showControls.collectAsStateWithLifecycle()
    val forward by viewModel.fordward.collectAsStateWithLifecycle()
    val backward by viewModel.backward.collectAsStateWithLifecycle()

    // Layout principal dividido en dos zonas táctiles
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Zona izquierda (retroceso 15s)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { // Tap simple
                            viewModel.showControls(!showControls)
                            viewModel.showLanguageItems(false)
                        },
                        onDoubleTap = { // Doble tap
                            viewModel.jumpTo(
                                time = (15 * 1000), // 15 segundos
                                left = true // Dirección: atrás
                            )
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Muestra icono de retroceso durante la operación
            if (backward) {
                Icon(
                    painter = painterResource(R.drawable.backward),
                    contentDescription = "Retroceso 15 segundos",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(backGround)
                        .padding(10.dp),
                    tint = Color.White
                )
            }
        }

        // Zona derecha (avance 15s)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            viewModel.showControls(!showControls)
                            viewModel.showLanguageItems(false)
                        },
                        onDoubleTap = {
                            viewModel.jumpTo(time = (15 * 1000)) // 15 segundos adelante
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Muestra icono de avance durante la operación
            if (forward) {
                Icon(
                    painter = painterResource(R.drawable.fordward),
                    contentDescription = "Avance 15 segundos",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(backGround)
                        .padding(10.dp),
                    tint = Color.White
                )
            }
        }
    }
}