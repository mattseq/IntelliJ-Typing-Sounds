// Package
package dk.project.keyboard

// Import
import com.intellij.openapi.editor.event.*
import dk.project.app.SoundPlayer
import kotlinx.coroutines.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class KeyboardSoundListener : EditorFactoryListener {

    // Attributes
    private val soundPlayer = SoundPlayer()
    private val scope = CoroutineScope(Dispatchers.IO)
    private val pressedKeys = mutableSetOf<Int>()

    // __________________________________________________________

    override fun editorCreated(event: EditorFactoryEvent) {

        val editor = event.editor
        val component = editor.contentComponent

        component.addKeyListener(object : KeyListener {
            override fun keyTyped(e: java.awt.event.KeyEvent) {}

            override fun keyPressed(e: java.awt.event.KeyEvent) {
                val keyCode = e.keyCode
                if (!pressedKeys.contains(keyCode)) {
                    pressedKeys.add(keyCode)
                    scope.launch { soundPlayer.playSound(keyCode) }
                }
            }

            override fun keyReleased(e: java.awt.event.KeyEvent) {
                pressedKeys.remove(e.keyCode)
                soundPlayer.keyReleased(e.keyCode)
            }
        })

        editor.document.addDocumentListener(object : DocumentListener {
            override fun beforeDocumentChange(event: DocumentEvent) {}

            override fun documentChanged(event: DocumentEvent) {
                scope.launch {
                    val newText = event.newFragment.toString()

                    if (newText == "\n") {
                        soundPlayer.playSound(KeyEvent.VK_ENTER)
                    } else if (newText.isEmpty() && event.oldLength > 0) {
                        soundPlayer.playSound(KeyEvent.VK_BACK_SPACE)
                    }
                }
            }
        })
    }

    // __________________________________________________________

    override fun editorReleased(event: EditorFactoryEvent) {
        scope.cancel()
    }

} // KeyboardSoundListener end