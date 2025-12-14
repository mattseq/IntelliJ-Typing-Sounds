// Package
package dk.project.keyboard

// Imports
import com.intellij.openapi.editor.event.*
import dk.project.app.SoundPlayer
import kotlinx.coroutines.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class KeyboardSoundListener : EditorFactoryListener {

    // Attributes
    private val soundPlayer = SoundPlayer()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO) // V. 1.3.0 FIX
    private val pressedKeys = mutableSetOf<Int>()

    // __________________________________________________________

    override fun editorCreated(event: EditorFactoryEvent) {

        val editor = event.editor
        val component = editor.contentComponent

        component.addKeyListener(object : KeyListener {

            // Typed
            override fun keyTyped(e: java.awt.event.KeyEvent) {}

            // Pressed
            override fun keyPressed(e: java.awt.event.KeyEvent) {

                val keyCode = e.keyCode

                // Fixes the ctrl+s sound issue
                if (e.isControlDown || e.isAltDown || e.isMetaDown) return

                // Don't play if key already is pressed
                if (!pressedKeys.contains(keyCode)) {
                    pressedKeys.add(keyCode)
                    scope.launch { soundPlayer.playSound(keyCode) }
                }
            }

            // Released
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

                    // Fixes our VK_ENTER bug in 1.1.0
                    if (newText.contains('\n') || newText.contains('\r')) {
                        soundPlayer.playSound(SoundPlayer.ENTER_MARKER)
                    } else if (newText.isEmpty() && event.oldLength > 0) {
                        soundPlayer.playSound(KeyEvent.VK_BACK_SPACE)
                    }
                }

            }
        })
    }

    // __________________________________________________________

    override fun editorReleased(event: EditorFactoryEvent) {
        // scope.cancel() V. 1.3.0 FIX
    }

} // KeyboardSoundListener end
