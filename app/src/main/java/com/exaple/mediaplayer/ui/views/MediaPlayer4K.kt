package com.exaple.mediaplayer.ui.views

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.media3.common.*
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.exaple.mediaplayer.ui.custom.Controls
import com.exaple.mediaplayer.ui.viewmodels.ExoplayerViewModel

/**
 * Reproductor de video 4K que utiliza ExoPlayer en Jetpack Compose.
 *
 * Estructura principal:
 * 1. Scaffold -> Diseño base de Material Design
 * 2. Box -> Contenedor principal
 *   2.1. Controls -> Controles personalizados superpuestos
 *   2.2. AndroidView -> Vista nativa de ExoPlayer
 *
 * @param mediaItems Lista de elementos multimedia a reproducir
 */
@OptIn(UnstableApi::class) // Para APIs experimentales de Media3
@Composable
fun MediaPlayer4K(mediaItems: List<MediaItem>) {

    // Contexto local para inicialización de componentes de Android
    val context = LocalContext.current

    // LÓGICA PRINCIPAL: Inicialización del reproductor mediante ViewModel
    val player = ExoplayerViewModel.init(
        mediaItems = mediaItems,
        context = context
    )

    // ESTRUCTURA UI: Scaffold provee estructura básica de Material Design
    Scaffold { innerPaddings ->
        // Contenedor principal para el video y controles
        Box(
            modifier = Modifier
                .padding(innerPaddings)  // Respeta los espacios del Scaffold
                .fillMaxSize()           // Ocupa toda la pantalla disponible
                .background(Color.Black), // Fondo negro para área del video
            contentAlignment = Alignment.Center
        ) {
            // CONTROLES PERSONALIZADOS (UI superpuesta)
            Controls(
                modifier = Modifier
                    .zIndex(1f)  // Asegura que los controles estén sobre el video
            )

            // VISOR DE VIDEO NATIVO (Integración con ExoPlayer)
            AndroidView(
                factory = {
                    PlayerView(context).apply {
                        this.player = player // Conecta el reproductor
                        useController = false // Desactiva controles nativos (usamos los personalizados)
                    }
                }
                // Modificadores adicionales podrían ir aquí
            )
        }
    }
}