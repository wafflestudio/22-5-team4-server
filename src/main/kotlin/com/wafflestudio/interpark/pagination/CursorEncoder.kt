package com.wafflestudio.interpark.pagination

import java.util.Base64
import kotlin.text.Charsets.UTF_8

object CursorEncoder {
    fun encodeCursor(fieldCursor: Any, idCursor: String): String {
        val cursorString = "$fieldCursor,$idCursor"
        return Base64.getEncoder().encodeToString(cursorString.toByteArray(UTF_8))
    }

    fun decodeCursor(encodedCursor: String): Pair<Any, String>? {
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