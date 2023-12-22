package ru.kpfu.itis.paramonov.androidtasks.util

import java.math.BigInteger
import java.security.MessageDigest

class PasswordUtil {
    companion object {
        fun encrypt(password: String): String {
            val md = MessageDigest.getInstance("SHA-256")
            val bigInt = BigInteger(1, md.digest(password.toByteArray(Charsets.UTF_8)))
            return String.format("%032x", bigInt)
        }
    }
}