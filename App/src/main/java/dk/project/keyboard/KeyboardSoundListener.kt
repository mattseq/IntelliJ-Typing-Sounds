package dk.project.keyboard

import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import kotlinx.coroutines.*
import java.io.InputStream
import javax.sound.sampled.AudioSystem

class KeyboardSoundListener : EditorFactoryListener {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor

        editor.document.addDocumentListener(object : DocumentListener {
            override fun beforeDocumentChange(event: DocumentEvent) {
            }

            override fun documentChanged(event: DocumentEvent) {
                scope.launch {
                    playTypingSound()
                }
            }
        })
    }

    override fun editorReleased(event: EditorFactoryEvent) {
        scope.cancel()
    }

    private fun playTypingSound() {
        try {
            val inputStream: InputStream = javaClass.getResourceAsStream("/sounds/typing.wav")
                ?: return
            val audioStream = AudioSystem.getAudioInputStream(inputStream)
            val clip = AudioSystem.getClip()
            clip.open(audioStream)
            clip.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}