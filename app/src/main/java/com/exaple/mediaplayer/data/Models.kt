package com.exaple.mediaplayer.data

/**
 * Modelo principal que contiene la lista de elementos multimedia (audios) disponibles.
 *
 * Propiedades:
 * @param audioList Lista de objetos Language que representan los diferentes audios/tracks disponibles.
 *                 Por defecto es una lista vacía para evitar nullability.
 */
data class MediaItems(
    val audioList: List<Language> = listOf()  // Inicialización segura con lista vacía por defecto
)

/**
 * Modelo que representa un track de audio con su idioma asociado.
 *
 * Propiedades:
 * @param mediaTrackGroupId Identificador único del grupo de tracks (usado por ExoPlayer para selección)
 * @param languageIso6392Name Código ISO 639-2 del idioma (estándar de 3 letras para identificación de idiomas)
 *
 */
data class Language(
    val mediaTrackGroupId: String,      // ID para gestión de tracks en el reproductor
    val languageIso6392Name: String     // Representación estándar del idioma (ej: "spa" para español)
)