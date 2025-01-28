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
        val fieldCursor = cursor.first
        val idCursor = cursor.second

        return Specification {root, _, cb ->
            val fieldPath = root.get<Comparable<Any>>(sortFieldName)
            val idPath = root.get<String>("id")

            if(isDescending) {
                cb.or(
                    cb.lessThan(fieldPath, fieldCursor as Comparable<Any>),
                    cb.and(
                        cb.equal(fieldPath, fieldCursor),
                        cb.lessThan(idPath, idCursor)
                    )
                )
            }
            else {
                cb.or(
                    cb.greaterThan(fieldPath, fieldCursor as Comparable<Any>),
                    cb.and(
                        cb.equal(fieldPath, fieldCursor),
                        cb.greaterThan(idPath, idCursor)
                    )
                )
            }

        }
    }
}