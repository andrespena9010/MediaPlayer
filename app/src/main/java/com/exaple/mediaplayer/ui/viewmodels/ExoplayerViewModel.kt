package com.exaple.mediaplayer.ui.viewmodels

import android.content.Context
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.exaple.mediaplayer.ui.models.Language
import com.exaple.mediaplayer.ui.models.MediaItems
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 *
 * ViewModel principal para el reproductor multimedia con ExoPlayer.
 *
 * Funcionalidades clave:
 *
 * Gestión del estado del reproductor:
 * Control de reproducción (play/pause)
 * Seguimiento de posición y duración
 * Configuración de volumen y mute
 * Operaciones multimedia:
 * Saltos temporales (forward/backward)
 * Cambio entre tracks de audio
 * Navegación entre items multimedia
 * Control de interfaz:
 * Auto-ocultamiento de controles UI
 * Gestión de estados de carga
 * Visualización de opciones de idioma
 *
 */

open class PlayerViewModel: ViewModel() {

    // Job para controlar corrutinas de temporizador de ocultamiento de controles
    var job: Job? = null

    // Instancia principal del reproductor ExoPlayer
    private lateinit var player: ExoPlayer

    // Flujos de estado para la UI (patrón MVI/MVVM)
    private val _items = MutableStateFlow(MediaItems())
    val items: StateFlow<MediaItems> = _items.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L) // Posición actual en milisegundos
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _isPlaying = MutableStateFlow(false) // Estado de reproducción
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isMute = MutableStateFlow(false) // Estado de silencio
    val isMute: StateFlow<Boolean> = _isMute.asStateFlow()

    private val _fordward = MutableStateFlow(false) // Estado de avance rápido
    val fordward: StateFlow<Boolean> = _fordward.asStateFlow()

    private val _backward = MutableStateFlow(false) // Estado de retroceso rápido
    val backward: StateFlow<Boolean> = _backward.asStateFlow()

    private val _showControls = MutableStateFlow(false) // Visibilidad de controles
    val showControls: StateFlow<Boolean> = _showControls.asStateFlow()

    private val _showLanguageItems = MutableStateFlow(false) // Visibilidad de selector de idiomas
    val showLanguageItems: StateFlow<Boolean> = _showLanguageItems.asStateFlow()

    private val _loading = MutableStateFlow(false) // Estado de carga/buffering
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _duration = MutableStateFlow(1L) // Duración total del media
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _currentVolume = MutableStateFlow(0.7f) // Volumen actual (0-1)
    val currentVolume: StateFlow<Float> = _currentVolume.asStateFlow()

    /**
     *
     * Funcion para inicializar el viewmodel.
     *
     * @property mediaItems Lista de elementos multimedia a reproducir
     * @property context Contexto de la aplicación para inicialización
     *
     */

    @OptIn(UnstableApi::class)
    fun init(
        mediaItems: List<MediaItem>,
        context: Context
    ): ExoPlayer? {
        // Configuración avanzada del selector de pistas para video 4K:
        // - Resolución máxima 3840x2160 (4K)
        // - Frame rate máximo 60fps
        // - Prioriza la mejor calidad disponible
        val trackSelector = DefaultTrackSelector(context).apply {
            parameters = buildUponParameters()
                .setMaxVideoSize(3840, 2160)
                .setMaxVideoFrameRate(60)
                .setForceHighestSupportedBitrate(true)
                .build()
        }

        // Construcción del reproductor ExoPlayer con configuración personalizada:
        player = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setLoadControl(
                DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                        60 * 1000, // Buffer mínimo
                        120 * 1000, // Buffer máximo
                        5000, // Buffer para empezar a reproducir
                        5000 // Buffer después de rebuffer
                    )
                    .build()
            )
            .build()

        // Configuración inicial del reproductor
        player.setVideoTextureView(TextureView(context)) // Surface para renderizado
        player.setMediaItems(mediaItems) // Asignación de contenido
        player.prepare() // Precarga
        player.playWhenReady = true // Auto-reproducción

        // Listener para cambios de estado del reproductor
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_READY -> {
                        setInfoItems() // Carga metadatos (idiomas disponibles)
                        _loading.update { false } // Finaliza estado de carga
                        _duration.update { player.duration } // Actualiza duración
                        hide() // Inicia temporizador para ocultar controles
                    }
                    Player.STATE_BUFFERING -> {
                        _loading.update { true } // Activa indicador de carga
                    }
                    Player.STATE_ENDED -> {
                        // Lógica cuando termina la reproducción
                    }
                    Player.STATE_IDLE -> {
                        // Estado inactivo
                    }
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                // Actualiza duración cuando cambia el ítem multimedia
                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                    _duration.update { player.duration }
                }
            }
        })

        _showControls.update { true } // Muestra controles inicialmente
        hide() // Inicia temporizador de ocultamiento
        updateUI() // Inicia actualización periódica de la UI

        return player
    }

    // Actualización periódica del estado de reproducción (cada 100ms)
    fun updateUI() {
        viewModelScope.launch {
            while (true) {
                _currentPosition.update { player.currentPosition }
                _isPlaying.update { player.isPlaying }
                _isMute.update { player.volume == 0f }
                delay(100)
            }
        }
    }

    // Salto temporal (adelante/atrás)
    fun jumpTo(time: Long, left: Boolean = false) {
        viewModelScope.launch {
            if (player.playbackState == Player.STATE_READY) {
                var advance = 0L
                if (left) {
                    _backward.update { true } // Activa feedback visual
                    advance = currentPosition.value - time
                } else {
                    _fordward.update { true }
                    advance = currentPosition.value + time
                }
                player.seekTo(advance)
                _currentPosition.update { player.currentPosition }
                delay(100) // Pequeño delay para feedback visual
                if (left) _backward.update { false } else _fordward.update { false }
            }
        }
    }

    // Temporizador para ocultar controles (5 segundos)
    fun hide() {
        job?.cancel() // Cancela temporizador previo si existe
        job = viewModelScope.launch {
            delay(5000)
            if (player.playbackState != Player.STATE_BUFFERING) {
                _showControls.update { false }
                _showLanguageItems.update { false }
            }
        }
    }

    // Métodos de control básico
    fun showControls(value: Boolean) {
        _showControls.update { value }
    }

    fun showLanguageItems(value: Boolean) {
        _showLanguageItems.update { value }
    }

    fun seekTo(millis: Long) {
        if (player.playbackState == Player.STATE_READY) {
            player.seekTo(millis)
            _currentPosition.update { player.currentPosition }
        }
    }

    fun pause() {
        player.pause()
    }

    fun play() {
        player.play()
    }

    // Navegación entre ítems multimedia
    fun goToNextMedia() {
        if (player.hasNextMediaItem()) {
            _currentPosition.update { 0L } // Reinicia posición
            player.seekToNext()
            player.play()
        }
    }

    fun goToPreviousMedia() {
        if (player.hasPreviousMediaItem()) {
            _currentPosition.update { 0L }
            player.seekToPrevious()
            player.play()
        }
    }

    // Extrae metadatos de pistas de audio disponibles
    @OptIn(UnstableApi::class)
    fun setInfoItems() {
        var audioLs = mutableListOf<Language>()
        player.currentTracks.groups.forEach { group ->
            if (group.type == C.TRACK_TYPE_AUDIO) {
                if (group.mediaTrackGroup.length > 0) {
                    val format = group.mediaTrackGroup.getFormat(0)
                    if (format.language.toString() != "und") { // Filtra idiomas no definidos
                        audioLs.add(
                            Language(
                                mediaTrackGroupId = group.mediaTrackGroup.id,
                                languageIso6392Name = format.language.toString()
                            )
                        )
                    }
                }
            }
        }
        _items.update { MediaItems(audioList = audioLs) }
    }

    // Cambia la pista de audio (para multi-idioma)
    @OptIn(UnstableApi::class)
    fun setAudio(mediaTrackGroupId: String) {
        if (player.playbackState != Player.STATE_READY) return

        var mediaTrackGroup: TrackGroup? = null

        // Busca el grupo de pistas correspondiente al ID
        player.currentTracks.groups.forEach { group ->
            if (group.type == C.TRACK_TYPE_AUDIO) {
                if (group.mediaTrackGroup.id == mediaTrackGroupId) {
                    mediaTrackGroup = group.mediaTrackGroup
                }
            }
        }

        // Aplica la selección de pista
        mediaTrackGroup?.let {
            val newParameters = player.trackSelectionParameters.buildUpon()
                .setOverrideForType(
                    TrackSelectionOverride(mediaTrackGroup, listOf(0))
                )
                .build()
            player.trackSelectionParameters = newParameters
        }
    }

    // Control de volumen (0-100)
    fun setVolume(position: Long) {
        _currentVolume.update { position.toFloat() / 100 }
        player.volume = currentVolume.value
    }

    // Silenciar/desilenciar
    fun mute(value: Boolean) {
        if (value) {
            player.volume = 0f
        } else {
            player.volume = currentVolume.value
        }
    }
}

// Implementación singleton del ViewModel
object ExoplayerViewModel : PlayerViewModel()