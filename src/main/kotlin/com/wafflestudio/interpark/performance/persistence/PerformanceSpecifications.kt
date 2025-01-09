package com.wafflestudio.interpark.performance.persistence

import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate

object PerformanceSpecifications {
    /**
     * 키워드(검색어) 필수: PerformanceEntity.title 또는 detail 에 매칭되는지
     * - 정확도 순이라는 것이 '키워드 매칭'을 의미한다고 가정
     */
    fun withTitle(title: String?): Specification<PerformanceEntity>? {
        if (title.isNullOrBlank()) {
            // title이 없으면 조건없이(conjunction) 반환
            return null
        }
        return Specification { root, _, cb ->
            cb.like(root.get("title"), "%$title%")
        }
    }

    /**
     * 날짜 필터(선택): 해당 날짜를 포함하는 공연이 있는지
     * - date가 공연 dates 컬렉션에 포함되어 있어야 함
     * - 특정 날짜가 아닌, 날짜 범위가 필요하다면 추가로 수정
     */
    fun withDate(date: LocalDate?): Specification<PerformanceEntity>? {
        if (date == null) return null

        return Specification { root, query, cb ->
            // dates 컬렉션에 date 값이 포함되어 있으면 true
            cb.isMember(date, root.get("dates"))
        }
    }

    /**
     * 지역 필터(선택): PerformanceHallEntity의 address 컬럼 안에 region 문자열이 포함
     */
    fun withRegion(region: String?): Specification<PerformanceEntity>? {
        if (region.isNullOrBlank()) return null

        return Specification { root, query, cb ->
            val hallJoin = root.join<PerformanceEntity, PerformanceHallEntity>("hall", JoinType.LEFT)
            cb.like(hallJoin.get("address"), "%$region%")
        }
    }

    /**
     * 장르 필터(선택): 이제 PerformanceEntity의 genre 컬럼을 직접 비교
     *  (예: 부분 매칭으로 하려면 cb.like(root.get("genre"), "%$genre%") 로 수정)
     */
    fun withCategory(category: String?): Specification<PerformanceEntity>? {
        if (category.isNullOrBlank()) return null

        return Specification { root, _, cb ->
            cb.equal(root.get<String>("category"), category)
        }
    }
}
