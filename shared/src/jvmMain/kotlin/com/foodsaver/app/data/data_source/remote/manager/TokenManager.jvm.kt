package com.foodsaver.app.data.data_source.remote.manager

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Properties
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

actual class TokenManager {

    private val mainTokenDir = Paths.get(
        System.getProperty("user.home"),
        ".foodsaver",
        "secure_token"
    )
    private val tokenFile = mainTokenDir.resolve("refresh_token.enc")
    private val saltFile = mainTokenDir.resolve("salt.bin")
    private val configFile = mainTokenDir.resolve("config.properties")
    private val password by lazy {
        getOrCreateMasterPassword().toCharArray()
    }

    init {
        if (!mainTokenDir.exists()) {
            mainTokenDir.createDirectories()
            mainTokenDir.setDirectoryPermission()
        }
    }


    actual suspend fun getToken(): String? {
        return try {
            if (!tokenFile.exists()) null

            val file = Files.readAllBytes(tokenFile)
            if (file.size < 13) return null

             val iv = file.copyOfRange(0, 12)
            val encryptedFile = file.copyOfRange(0, file.size)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecureKey(), GCMParameterSpec(128, iv))

            String(cipher.doFinal(encryptedFile), Charsets.UTF_8)
        }catch (e: Exception) {
            clearStorage()
            null
        }
    }

    actual suspend fun setToken(token: String) {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(12).also { SecureRandom().nextBytes(it) }
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecureKey(), GCMParameterSpec(128, iv))

        val encryptedData = cipher.doFinal(token.toByteArray(Charsets.UTF_8))

        Files.write(tokenFile, iv + encryptedData)
        tokenFile.setPermission()
    }

    actual suspend fun clearStorage() {
        if (tokenFile.exists()) {
            Files.delete(tokenFile)
        }
    }

    private fun isWindows() = System.getProperty("os.name").lowercase().contains("windows")

    private fun getOrCreateMasterPassword(): String {
        val properties = Properties()
        val masterPasswordKey = "master_password_key"

        return if (configFile.exists()) {
            properties.load(Files.newInputStream(configFile))
            properties.getProperty(masterPasswordKey, generateNewMasterMasterPassword())
        } else {
            val newPassword = generateNewMasterMasterPassword()
            properties.setProperty(masterPasswordKey, newPassword)
            properties.store(Files.newOutputStream(configFile), "FoodSaver Secure Config")
            configFile.setPermission()
            newPassword
        }
    }

    private fun getOrCreateSecureKey(): SecretKey {
        val salt = if (saltFile.exists()) {
            Files.readAllBytes(saltFile)
        } else {
            val bytes = ByteArray(32).also { SecureRandom().nextBytes(it) }
            Files.write(saltFile, bytes)
            saltFile.setPermission()
            bytes
        }

        val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val pbeKeySpec = PBEKeySpec(password, salt, 100000, 256) // 100.000 for security

        val key = secretKeyFactory.generateSecret(pbeKeySpec)
        return SecretKeySpec(key.encoded, "AES")
    }

    private fun Path.setDirectoryPermission() {
        if (!isWindows()) {
            this.toFile().setWritable(true, true)
            this.toFile().setExecutable(true, true)
            this.toFile().setReadable(true, true)
        }
    }

    private fun generateNewMasterMasterPassword(): String {
        val base = System.getProperty("user.name") +
                System.getProperty("user.home") +
                "food-saver-master-key-secret-2025"

        val digest = MessageDigest.getInstance("SHA-256").digest(base.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
    private fun Path.setPermission() {
        if (!isWindows()) {
            this.toFile().setReadable(true, true)
            this.toFile().setWritable(true, true)
        }
    }
}
