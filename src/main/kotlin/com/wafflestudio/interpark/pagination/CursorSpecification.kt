package com.wafflestudio.interpark.pagination

import org.springframework.data.jpa.domain.Specification

// 정렬기준이 되는 field를 기준으로 조건을 설정
object CursorSpecification {
    fun <T> withCursor(
        cursor: Pair<Any, String>?,
        sortFieldName: String,
        isDescending: Boolean = true,
    ): Specification<T>? {
        if (cursor == null) return null
        val fieldCursor = cursor.first as? Comparable<Any>
            ?: throw CursorNotComparableException()
        val idCursor = cursor.second as? String
            ?: throw CursorNotComparableException()

        return Specification {root, _, cb ->
            val fieldPath = try {
                root.get<Comparable<Any>>(sortFieldName)
            } catch (e: IllegalArgumentException) {
                throw FieldNotFoundException()
            }
            val idPath = try {
                root.get<String>("id")
            } catch (e: IllegalArgumentException) {
                throw FieldNotFoundException()
            }

            if(isDescending) {
                cb.or(
                    cb.lessThan(fieldPath, fieldCursor),
                    cb.and(
                        cb.equal(fieldPath, fieldCursor),
                        cb.lessThan(idPath, idCursor)
                    )
                )
            }
            else {
                cb.or(
                    cb.greaterThan(fieldPath, fieldCursor),
                    cb.and(
                        cb.equal(fieldPath, fieldCursor),
                        cb.greaterThan(idPath, idCursor)
                    )
                )
            }

        }
    }
}