package com.foodsaver.app.utils

import java.io.File
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.attribute.DosFileAttributeView

actual class IsUserAuthenticated() {

    private val parentFolder = File(System.getProperty("user.home") + ".foodsaver")
    val file = File(parentFolder, "auth")

    init {
        try {
            if (!parentFolder.exists()) {
                parentFolder.mkdirs()

                val dosView = Files.getFileAttributeView(parentFolder.toPath(), DosFileAttributeView::class.java,
                    LinkOption.NOFOLLOW_LINKS)
                dosView?.setHidden(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    actual operator fun invoke(): Boolean {
        return try {
            if (file.exists()) file.readText().trim().toBoolean()
            else false
        }catch (_: Exception) {
            false
        }
    }

    actual operator fun invoke(value: Boolean) {
        try {
            file.writeText(value.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}