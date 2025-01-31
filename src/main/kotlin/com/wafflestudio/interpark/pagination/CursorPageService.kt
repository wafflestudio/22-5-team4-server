package com.wafflestudio.interpark.pagination

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.awt.Cursor
import java.time.Instant

abstract class CursorPageService<T>(
    private val repository: JpaSpecificationExecutor<T>
) {
    fun findAllWithCursor(
        cursorPageable: CursorPageable,
        specification: Specification<T>? = null,
    ): CursorPageResponse<T> {
        val cursor = cursorPageable.decodeCursor()

        val parsedCursor = cursor?.let {
            val parsedFieldCursor = when(cursorPageable.sortFieldName) {
                "createdAt" -> cursor.let { Instant.parse(cursor.first) }
                "id" -> cursor.first
                else -> throw InvalidFieldNameException()
            }
            parsedFieldCursor to cursor.second
        }

        val cursorSpec = CursorSpecification.withCursor<T>(
            cursor = parsedCursor,
            sortFieldName = cursorPageable.sortFieldName,
            isDescending = cursorPageable.isDescending,
        )

        val combinedSpec = if(specification != null) {
            Specification.where(cursorSpec).and(specification)
        } else {
            cursorSpec
        }

        val sortDirection = if(cursorPageable.isDescending) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable = PageRequest.of(0, cursorPageable.size+1, Sort.by(sortDirection, cursorPageable.sortFieldName, "id"))

        val results = repository.findAll(combinedSpec, pageable).content
        val hasNext = results.size > cursorPageable.size

        val returnData = if(hasNext) results.dropLast(1) else results
        val nextCursor = returnData.lastOrNull()?.let {
            CursorEncoder.encodeCursor(it, cursorPageable.sortFieldName)
        }

        return CursorPageResponse(
            data = returnData,
            nextCursor = nextCursor,
            hasNext = hasNext,
        )
    }
}

data class CursorPageable(
    val cursor: String?,
    val sortFieldName: String = "id", // 기준이 없다면 id만 가지고 정렬
    val isDescending: Boolean = true,
    val size: Int = 5,
) {
    fun decodeCursor(): Pair<String, String>? {
        return cursor?.let { CursorEncoder.decodeCursor(it) ?: throw InvalidCursorException() }
    }
}

data class CursorPageResponse<T>(
    val data: List<T>,
    val nextCursor: String?,
    val hasNext : Boolean,
)