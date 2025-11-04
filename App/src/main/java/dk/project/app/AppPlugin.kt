package dk.project.app

import com.intellij.openapi.editor.event.*
import com.intellij.openapi.editor.EditorFactory
import javax.swing.SwingUtilities

class AppPlugin : EditorFactoryListener {

    private val soundPlayer = SoundPlayer()

    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor
        editor.document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                SwingUtilities.invokeLater {
                    soundPlayer.playSound()
                }
            }
        })
    }

    override fun editorReleased(event: EditorFactoryEvent) {}

}