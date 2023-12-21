package ru.kpfu.itis.paramonov.androidtasks.util

import java.math.BigInteger
import java.security.MessageDigest

class PasswordUtil {
    companion object {
        fun encrypt(password: String): String {
            val md = MessageDigest.getInstance("MD5")
            md.update(password.toByteArray());
            return BigInteger(1, md.digest()).toString(16)
        }
    }
}