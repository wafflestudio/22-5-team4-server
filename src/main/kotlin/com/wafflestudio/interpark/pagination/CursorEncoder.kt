package com.wafflestudio.interpark.pagination

import java.util.Base64
import kotlin.text.Charsets.UTF_8

object CursorEncoder {
    fun encodeCursor(targetEntity: Any, fieldName: String): String {
        val idCursor = targetEntity.javaClass.getDeclaredField("id").apply { isAccessible = true }.get(targetEntity)
        val fieldCursor = targetEntity.javaClass.getDeclaredField(fieldName).apply { isAccessible = true }.get(targetEntity)

        val cursorString = "$fieldCursor,$idCursor"
        return Base64.getEncoder().encodeToString(cursorString.toByteArray(UTF_8))
    }

    fun decodeCursor(encodedCursor: String): Pair<String, String>? {
        return try {
            val decodedString = String(Base64.getDecoder().decode(encodedCursor), UTF_8)
            val parts = decodedString.split(",")
            if( parts.size == 2 ) {
                val fieldCursor = parts[0]
                val idCursor = parts[1]

                fieldCursor to idCursor
            }
            else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}