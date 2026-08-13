package com.ranjan.somiq.core.platform

import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JvmMediaPicker(
    private val parentFrame: Frame? = null
) : MediaPicker {

    override suspend fun pickMedia(type: MediaType): String? = withContext(Dispatchers.IO) {
        val dialog = FileDialog(parentFrame, "Select Media", FileDialog.LOAD)
        
        dialog.file = when (type) {
            MediaType.IMAGE -> "*.jpg;*.jpeg;*.png;*.gif"
            MediaType.VIDEO -> "*.mp4;*.mov;*.avi;*.mkv"
        }
        
        dialog.isVisible = true
        
        val directory = dialog.directory
        val file = dialog.file
        
        if (directory != null && file != null) {
            File(directory, file).absolutePath
        } else {
            null
        }
    }
}
