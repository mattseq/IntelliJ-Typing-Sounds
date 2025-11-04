// Package
package dk.project.app

// Imports
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import java.awt.event.KeyEvent

class SoundPlayer {

    // Attributes
    private var lastPlayedTime = 0L
    private val cooldownMs = 140L
    private val pressedKeys = mutableSetOf<Int>()
    private val typingData: ByteArray by lazy { loadSound("/sounds/typing.wav") }
    private val enterData: ByteArray by lazy { loadSound("/sounds/enter.wav") }
    private val backData: ByteArray by lazy { loadSound("/sounds/back.wav") }

    // __________________________________________________________

    private fun loadSound(path: String): ByteArray {

        val inputStream = this::class.java.getResourceAsStream(path)
            ?: throw IllegalStateException("$path not found in /resources folder location")
        val buffer = ByteArrayOutputStream()

        inputStream.use { inp ->
            val data = ByteArray(1024)
            var n: Int
            while (inp.read(data).also { n = it } != -1) {
                buffer.write(data, 0, n)
            }
        }

        return buffer.toByteArray()

    }

    // __________________________________________________________

    fun playSound(keyCode: Int) {

        val currentTime = System.currentTimeMillis()
        if (pressedKeys.contains(keyCode)) return
        if (currentTime - lastPlayedTime < cooldownMs) return

        lastPlayedTime = currentTime
        pressedKeys.add(keyCode)

        val soundData = when (keyCode) {
            KeyEvent.VK_ENTER -> enterData
            KeyEvent.VK_BACK_SPACE, KeyEvent.VK_DELETE -> backData
            else -> typingData
        }

        try {
            val audioStream = AudioSystem.getAudioInputStream(ByteArrayInputStream(soundData))
            val clip = AudioSystem.getClip().apply {

                open(audioStream)
                val volumeControl = getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
                val randomOffset = (-5..5).random()
                volumeControl.value = -28f + randomOffset
                start()

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // __________________________________________________________

    fun keyReleased(keyCode: Int) {
        pressedKeys.remove(keyCode)
    }

} // SoundPlayer end