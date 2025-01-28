package com.wafflestudio.interpark.pagination

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.awt.Cursor

abstract class CursorPageService<T>(
    private val repository: JpaSpecificationExecutor<T>
) {
    fun findAllWithCursor(
        cursorPageable: CursorPageable,
        specification: Specification<T>? = null,
    ): List<T> {
        val cursor = cursorPageable.decodeCursor()

        val cursorSpec = CursorSpecification.withCursor<T>(
            cursor = cursor,
            sortFieldName = cursorPageable.sortFieldName ?: "id",
            isDescending = cursorPageable.isDescending,
        )

        val combinedSpec = if(specification != null) {
            Specification.where(cursorSpec).and(specification)
        } else {
            cursorSpec
        }

        val sortDirection = if(cursorPageable.isDescending) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable = PageRequest.of(0, cursorPageable.size, Sort.by(sortDirection, cursorPageable.sortFieldName, "id"))

        return repository.findAll(combinedSpec, pageable).content
    }
}

data class CursorPageable(
    val cursor: String?,
    val sortFieldName: String? = null,
    val isDescending: Boolean = true,
    val size: Int = 5,
) {
    fun decodeCursor(): Pair<Any, String>? {
        return cursor?.let {CursorEncoder.decodeCursor(it) }
    }
}