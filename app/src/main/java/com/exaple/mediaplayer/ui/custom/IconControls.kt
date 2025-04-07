package com.exaple.mediaplayer.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Componente reutilizable para iconos de control con:
 * - Efecto visual de fondo circular
 * - Gestión de eventos táctiles
 * - Estilo consistente para controles multimedia
 *
 * @param source ID del recurso drawable del icono
 * @param contentDescription Descripción para accesibilidad
 * @param backGround Color de fondo circular
 * @param onTap Callback al tocar el icono (opcional)
 */
@Composable
fun IconControls(
    source: Int, // ID del recurso del icono
    contentDescription: String, // Texto para accesibilidad
    backGround: Color, // Color del fondo circular
    onTap: () -> Unit = {} // Función al hacer tap (opcional)
) {
    Icon(
        painter = painterResource(source), // Icono a mostrar
        contentDescription = contentDescription,
        modifier = Modifier
            // Detector de gestos táctiles
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTap() } // Ejecuta callback al tocar
                )
            }
            // Estilo visual
            .clip(RoundedCornerShape(30.dp)) // Forma circular completa
            .background(backGround) // Fondo coloreado
            .padding(10.dp), // Espaciado interno
        tint = Color.White // Color blanco para el icono
    )
}