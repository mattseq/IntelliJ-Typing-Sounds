package dk.project.app

import javax.sound.sampled.AudioSystem

class SoundPlayer {

    private val clip by lazy {
        val audioStream = this::class.java.getResourceAsStream("/typing.wav")
            ?: throw IllegalStateException("typing.wav ikke fundet i resources")
        AudioSystem.getClip().apply {
            open(AudioSystem.getAudioInputStream(audioStream))
        }
    }

    fun playSound() {
        if (clip.isRunning) clip.stop()
        clip.framePosition = 0
        clip.start()
    }

}