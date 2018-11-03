package com.mercandalli.server.files.window

import com.mercandalli.server.files.main.ApplicationGraph
import java.awt.EventQueue
import javax.swing.*
import javax.swing.JPanel
import javax.swing.BoxLayout

class MainFrame(
    title: String
) : JFrame(),
    MainContract.Screen {

    private val userAction = createUserAction()

    init {
        setTitle(title)

        val panel = JPanel()
        val startJButton = JButton("Start server")
        val stopJButton = JButton("Stop server")
        val pullSubFoldersJButton = JButton("Pull sub folders")
        stopJButton.isEnabled = false

        val debugJLabel = JLabel("Debug")
        debugJLabel.border = BorderFactory.createEmptyBorder(20, 0, 20, 0)

        startJButton.addActionListener {
            userAction.onStartClicked()
            startJButton.isEnabled = false
            stopJButton.isEnabled = true
        }

        stopJButton.addActionListener {
            userAction.onStopClicked()
        }

        pullSubFoldersJButton.addActionListener {
            userAction.onPullSubFoldersClicked()
        }

        panel.border = BorderFactory.createEmptyBorder(20, 10, 20, 10)
        panel.add(startJButton)
        panel.add(stopJButton)
        panel.add(pullSubFoldersJButton)
        panel.add(debugJLabel)

        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        contentPane.add(panel)

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(300, 400)
        setLocationRelativeTo(null)
    }

    private fun createUserAction(): MainContract.UserAction {
        val serverManager = ApplicationGraph.getServerManager()
        val shellManager = ApplicationGraph.getShellManager()
        val rootPath = ApplicationGraph.getRootPath()
        val pullSubRepositoryShellFile = ApplicationGraph.getPullSubRepositoryShellFile()
        return MainPresenter(
            this,
            serverManager,
            shellManager,
            rootPath,
            pullSubRepositoryShellFile
        )
    }

    companion object {

        fun start() {
            EventQueue.invokeLater(Companion::createAndShowGUI)
        }

        private fun createAndShowGUI() {
            val frame = MainFrame(
                "Server - mercandalli.com"
            )
            frame.isVisible = true
        }
    }
}
