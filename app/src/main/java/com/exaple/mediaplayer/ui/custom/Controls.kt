package com.exaple.mediaplayer.ui.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exaple.mediaplayer.ui.viewmodels.ExoplayerViewModel
import com.exaple.mediaplayer.ui.viewmodels.PlayerViewModel

/**
 * Componente principal que contiene todos los controles del reproductor multimedia.
 *
 * @param modifier Modificador para personalizar el diseño del componente.
 * @param viewModel ViewModel que proporciona los datos y la lógica del reproductor.
 */
@Composable
fun Controls(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = ExoplayerViewModel
) {
    // Obtener estados observables del ViewModel
    val items by viewModel.items.collectAsStateWithLifecycle() // Lista de elementos multimedia
    val loading by viewModel.loading.collectAsStateWithLifecycle() // Estado de carga
    val showControls by viewModel.showControls.collectAsStateWithLifecycle() // Visibilidad de controles
    val showLanguageItems by viewModel.showLanguageItems.collectAsStateWithLifecycle() // Visibilidad de selector de idioma
    val backGround = Color(0x33888888) // Color de fondo semitransparente

    // Contenedor principal que ocupa toda la pantalla
    Box(
        modifier = modifier
            .fillMaxSize()
            // Manejar eventos de toque para ocultar controles
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent(pass = PointerEventPass.Final)
                        viewModel.hide() // Oculta los controles al tocar la pantalla
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Mostrar indicador de carga si está cargando
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp) // Tamaño grande del indicador
            )
        }

        // Componente para manejar búsqueda con doble toque
        DoubleTapSeekControl(
            backGround = backGround
        )

        // Mostrar controles solo si showControls es true
        if (showControls) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Sección superior (para selector de idioma)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Ocupa el espacio disponible
                    contentAlignment = Alignment.BottomEnd
                ) {
                    // Mostrar selector de idioma si hay múltiples pistas de audio
                    if (showLanguageItems && items.audioList.size > 1) {
                        Box(
                            modifier = Modifier
                                .padding(30.dp), // Margen interno
                            contentAlignment = Alignment.Center
                        ) {
                            LanguageItems() // Componente de selección de idioma
                        }
                    }
                }

                // Barra de progreso (slider)
                Row(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SliderCustom(backGround = backGround) // Barra deslizante personalizada
                }

                // Espaciador
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

                // Controles inferiores (play/pause, etc.)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    BottomControls(backGround = backGround) // Componente con botones de control
                }

                // Espaciador final
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
            }
        }
    }
}