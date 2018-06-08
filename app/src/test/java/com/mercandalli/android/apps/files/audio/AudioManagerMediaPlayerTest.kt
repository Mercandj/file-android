package com.mercandalli.android.apps.files.audio

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class AudioManagerMediaPlayerTest {

    @Mock
    private val mediaPlayer: AudioManagerMediaPlayer.MediaPlayerWrapper? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun setSourcePathSetPath() {
        // Given
        val audioManager = createInstanceToTest()
        val sourcePath = "path"

        // When
        audioManager.setSourcePath(sourcePath)

        // Then
        verify(mediaPlayer)!!.setDataSource(sourcePath)
    }

    @Test
    fun setSourcePathGivePath() {
        // Given
        val audioManager = createInstanceToTest()
        val sourcePath = "path"
        audioManager.setSourcePath(sourcePath)

        // When
        val sourcePathFromGetter = audioManager.getSourcePath()

        // Then
        Assert.assertEquals(sourcePath, sourcePathFromGetter)
    }

    @Test
    fun initialSourcePathIsNull() {
        // Given
        val audioManager = createInstanceToTest()

        // When
        val sourcePathFromGetter = audioManager.getSourcePath()

        // Then
        Assert.assertNull(sourcePathFromGetter)
    }

    @Test
    fun isPlaying() {
        // Given
        val audioManager = createInstanceToTest()

        // When
        audioManager.isPlaying()

        // Then
        verify(mediaPlayer)!!.isPlaying
    }

    @Test
    fun isMp3Supported() {
        // Given
        val audioManager = createInstanceToTest()
        val pathToChecks = ArrayList<String>()
        pathToChecks.add("totoOo_- .mP3")
        pathToChecks.add("SdCard/Test.MP3")
        pathToChecks.add("SdCard/TesMp3.mp3")

        for (pathToCheck in pathToChecks) {
            // When
            val supportedPath = audioManager.isSupportedPath(pathToCheck)

            // Then
            Assert.assertTrue("$pathToCheck should be supported", supportedPath)
        }
    }

    @Test
    fun isWmaSupported() {
        // Given
        val audioManager = createInstanceToTest()
        val pathToChecks = ArrayList<String>()
        pathToChecks.add("totoOo_- .Wma")
        pathToChecks.add("01 Piste 1.wma")
        pathToChecks.add("SdCard/TesMp3.wma")

        for (pathToCheck in pathToChecks) {
            // When
            val supportedPath = audioManager.isSupportedPath(pathToCheck)

            // Then
            Assert.assertFalse("$pathToCheck should be supported", supportedPath)
        }
    }

    @Test
    fun isFlacNotSupported() {
        // Given
        val audioManager = createInstanceToTest()
        val pathToChecks = ArrayList<String>()
        pathToChecks.add("totoOo_- .Flac")
        pathToChecks.add("SdCard/Test.flac")
        pathToChecks.add("SdCard/TesMp3.flac")

        for (pathToCheck in pathToChecks) {
            // When
            val supportedPath = audioManager.isSupportedPath(pathToCheck)

            // Then
            Assert.assertFalse("$pathToCheck should not be supported", supportedPath)
        }
    }

    private fun createInstanceToTest(): AudioManagerMediaPlayer {
        return AudioManagerMediaPlayer(
                mediaPlayer!!
        )
    }
}