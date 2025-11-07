// Package
package dk.project.app

// Imports
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import java.awt.event.KeyEvent

class SoundPlayer {

    // Same as "public static" in .java basically
    companion object {
        const val ENTER_MARKER = -1000
    }

    // Attributes
    private var lastPlayedTime = 0L
    private val cooldownMs = 140L
    private val pressedKeys = mutableSetOf<Int>()
    private val typingData: ByteArray by lazy { loadSound("/sounds/typing.wav") }
    private val enterData: ByteArray by lazy { loadSound("/sounds/enter2.wav") }
    private val backData: ByteArray by lazy { loadSound("/sounds/back2.wav") }

    // __________________________________________________________
    // Loads our .wav file and converts it to ByteArray for better stability

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

        // If same key is pressed
        if (pressedKeys.contains(keyCode)) return

        // Prevent cooldown on important keys such as BACK_SPACE & ENTER.
        val isBackOrDelete = (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE)
        val isEnterMarker = (keyCode == ENTER_MARKER)
        val ignoreCooldown = isBackOrDelete || isEnterMarker

        if (!ignoreCooldown && currentTime - lastPlayedTime < cooldownMs) return

        lastPlayedTime = currentTime

        if (!isEnterMarker) {
            pressedKeys.add(keyCode)
        }

        // Choose correct sound file
        val soundData = when {
            isEnterMarker -> enterData
            keyCode == KeyEvent.VK_ENTER -> enterData
            isBackOrDelete -> backData
            else -> typingData
        }

        try {
            val audioStream = AudioSystem.getAudioInputStream(ByteArrayInputStream(soundData))
            val clip = AudioSystem.getClip().apply {
                open(audioStream)
                val volumeControl = getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
                val randomOffset = (-5..5).random()
                volumeControl.value = -28f + randomOffset // Maybe -25f is better for a later version. Needs testing.
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