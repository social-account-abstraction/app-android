package md.hackaton.aasocialrecovery.encryption

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class CryptographyUtils {

    companion object {

        fun encryptMessage(msg: String): String {
            val plaintext: ByteArray = msg.toByteArray()
            val keygen = KeyGenerator.getInstance("AES")
            keygen.init(256)
            val key: SecretKey = keygen.generateKey()
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val ciphertext: ByteArray = cipher.doFinal(plaintext)
            val iv: ByteArray = cipher.iv

            return String(ciphertext)
        }

        fun hashMessage(msg: String): String {
            val message: ByteArray = msg.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest: ByteArray = md.digest(message)

            return String(digest)
        }
    }
}