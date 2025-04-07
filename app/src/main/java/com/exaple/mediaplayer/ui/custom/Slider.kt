package com.exaple.mediaplayer.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * Slider personalizado para control de reproducción multimedia con:
 * - Interacción táctil (toque y arrastre)
 * - Visualización de progreso
 * - Opción para mostrar tiempo actual durante el arrastre
 *
 * @param position Posición actual en milisegundos
 * @param duration Duración total en milisegundos
 * @param modifier Modificador para personalizar el layout
 * @param timeInfo Habilita mostrar el tiempo durante el arrastre
 * @param onTapOrDragEnd Callback al finalizar interacción (nueva posición)
 * @param onDrag Callback durante el arrastre (posición actual)
 */
@Composable
fun SliderPlayer(
    position: Long,
    duration: Long,
    modifier: Modifier = Modifier,
    timeInfo: Boolean = false,
    onTapOrDragEnd: (Long) -> Unit = {},
    onDrag: (Long) -> Unit = {}
) {
    // Estados para controlar el slider
    var width by remember { mutableIntStateOf(0) } // Ancho total del slider
    var dragPos by remember { mutableFloatStateOf(0f) } // Posición de arrastre
    var dragged by remember { mutableStateOf(false) } // Estado de arrastre

    // Calcula la fracción de progreso (0-1)
    val sliderFraction: Float = if (dragged && width != 0) {
        (dragPos / width).coerceIn(0f, 1f) // Usa posición de arrastre si está activo
    } else {
        (position / duration.toFloat()).coerceIn(0f, 1f) // Usa posición normal
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .onSizeChanged { newSize ->
                width = newSize.width // Actualiza el ancho disponible
            }
            // Detección de toques simples
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        // Calcula nueva posición basada en el toque
                        onTapOrDragEnd((duration * (offset.x / width).coerceIn(0f, 1f)).toLong())
                    }
                )
            }
            // Detección de gestos de arrastre
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragged = true
                        dragPos = offset.x // Inicia posición de arrastre
                    },
                    onDrag = { pointer, offset ->
                        pointer.consume()
                        dragPos += offset.x // Actualiza posición durante arrastre
                        onDrag((duration * (dragPos / width).coerceIn(0f, 1f)).toLong())
                    },
                    onDragEnd = {
                        dragged = false // Finaliza arrastre
                        onTapOrDragEnd((duration * (dragPos / width).coerceIn(0f, 1f)).toLong())
                    }
                )
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Barra de fondo del slider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .height(5.dp)
                .background(Color.Gray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.CenterStart
        ) {
            // Barra de progreso (relleno)
            Box(
                modifier = Modifier
                    .fillMaxWidth(sliderFraction) // Rellena según progreso
                    .clip(RoundedCornerShape(20.dp))
                    .height(3.dp)
                    .background(Color.Black)
            )
        }

        // Muestra el tiempo durante el arrastre (si está habilitado)
        if (dragged && timeInfo) {
            TimeInfo(
                modifier = Modifier
                    .offset { IntOffset((dragPos - 50).toInt(), -100) } // Posición sobre el dedo
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .padding(5.dp),
                time = ((dragPos / width).coerceIn(0f, 1f) * duration).toLong()
            )
        }
    }
}