package com.youtubekids.app.utils

import java.security.MessageDigest
import java.security.SecureRandom

object SecurityUtils {

    /**
     * Generates a random salt
     */
    fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return bytesToHex(salt)
    }

    /**
     * Hashes a PIN code with a salt using SHA-256
     */
    fun hashPin(pin: String, salt: String): String {
        val saltedPin = pin + salt
        return sha256(saltedPin)
    }

    /**
     * Verifies a PIN against a stored hash
     */
    fun verifyPin(pin: String, storedHash: String, salt: String): Boolean {
        val hashToVerify = hashPin(pin, salt)
        return hashToVerify == storedHash
    }

    /**
     * SHA-256 hashing
     */
    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytesToHex(bytes)
    }

    /**
     * Converts byte array to hex string
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = "0123456789abcdef"
        val result = StringBuilder(bytes.size * 2)
        bytes.forEach { byte ->
            val i = byte.toInt()
            result.append(hexChars[i shr 4 and 0x0f])
            result.append(hexChars[i and 0x0f])
        }
        return result.toString()
    }
}
