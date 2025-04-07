package com.exaple.mediaplayer.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exaple.mediaplayer.ui.viewmodels.ExoplayerViewModel
import com.exaple.mediaplayer.ui.viewmodels.PlayerViewModel

/**
 * Componente desplegable para selección de idiomas/audio tracks
 *
 * Muestra una lista vertical de opciones de idioma disponibles con:
 * - Diseño compacto con scroll
 * - Efectos visuales al seleccionar
 * - Integración directa con el ViewModel del reproductor
 *
 * @param viewModel Provee la lista de tracks de audio y maneja los cambios
 */
@Composable
fun LanguageItems(
    viewModel: PlayerViewModel = ExoplayerViewModel // ViewModel por defecto
) {
    // Estado observado de los items de audio disponibles
    val items by viewModel.items.collectAsStateWithLifecycle()

    // Contenedor principal con estilo de tarjeta flotante
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp)) // Bordes redondeados
            .background(Color.Gray.copy(alpha = 0.3f)) // Fondo semitransparente
            .heightIn(max = 100.dp) // Altura máxima con scroll
            .width(80.dp) // Ancho fijo
    ) {
        // Lista optimizada con LazyColumn
        LazyColumn(
            modifier = Modifier.padding(5.dp), // Margen interno
            verticalArrangement = Arrangement.Center, // Centrado vertical
            horizontalAlignment = Alignment.End // Alineación a la derecha
        ) {
            // Renderizado eficiente de cada item
            items(items.audioList) { item ->
                // Cada opción de idioma
                Text(
                    modifier = Modifier
                        .padding(top = 2.dp, bottom = 2.dp) // Espaciado vertical
                        .clip(RoundedCornerShape(20.dp)) // Bordes redondeados
                        .background(Color.Black.copy(alpha = 0.6f)) // Fondo oscuro
                        .fillMaxWidth() // Ancho completo
                        .clickable { // Manejo de clics
                            viewModel.setAudio(item.mediaTrackGroupId) // Cambia track de audio
                        },
                    text = "   ${item.languageIso6392Name}   ", // Texto con padding visual
                    color = Color.White // Texto blanco
                )
            }
        }
    }
}