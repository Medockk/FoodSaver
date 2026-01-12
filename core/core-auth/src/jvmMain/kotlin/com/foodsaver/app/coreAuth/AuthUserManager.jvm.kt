package com.foodsaver.app.coreAuth

import java.io.File
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.attribute.DosFileAttributeView

actual class AuthUserManager {

    private val parentFolder = File(System.getProperty("user.home") + ".foodsaver")
    private val file = File(parentFolder, "auth")

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

    actual fun getCurrentUid(): String? {
        return try {
            if (file.exists()) file.readText().trim()
            else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    actual fun setCurrentUid(uid: String) {
        try {
            file.writeText(uid)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun isUserAuthenticated(): Boolean {
        return getCurrentUid() != null
    }

    actual fun logout() {
        try {
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}