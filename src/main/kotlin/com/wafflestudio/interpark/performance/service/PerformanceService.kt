package com.wafflestudio.interpark.performance.service

import com.wafflestudio.interpark.performance.controller.Performance
import com.wafflestudio.interpark.performance.persistence.PerformanceEntity
import com.wafflestudio.interpark.performance.persistence.PerformanceRepository
import com.wafflestudio.interpark.performance.persistence.PerformanceSpecifications
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate

class PerformanceService(
    private val performanceRepository: PerformanceRepository,
) {
    fun searchPerformance(
        keyword: String,
        sortType: Int,
        date: LocalDate?,
        region: String?,
        genre: String?,
    ): List<Performance> {
        // 기본 스펙(키워드 검색은 필수)
        var spec: Specification<PerformanceEntity> = Specification.where(PerformanceSpecifications.withKeyword(keyword))

        // 날짜가 있으면 스펙 추가
        PerformanceSpecifications.withDate(date)?.let {
            spec = spec.and(it)
        }

        // 지역이 있으면 스펙 추가
        PerformanceSpecifications.withRegion(region)?.let {
            spec = spec.and(it)
        }

        // 장르가 있으면 스펙 추가
        PerformanceSpecifications.withGenre(genre)?.let {
            spec = spec.and(it)
        }

        // 정렬 기준
        // 1. 정확도순: 일단 예시상 '정확도'라는 것은 키워드 매칭 스코어 기반 정렬이 필요하지만,
        //    여기서는 정렬로 처리하기 애매하므로 title 기준 정렬 or 별도 검색엔진(ElasticSearch)이 필요.
        //    간단히 title 오름차순 정렬로 가정하겠습니다.
        // 2. 공연임박순: dates 중 가장 빠른 날짜를 기준으로 오름차순 정렬
        // 3. 많이 팔린 순 : 판매량(sales) 기준 내림차순 정렬 (실제로 sales라는 컬럼이 있어야 함)
        //
        // 실무에서는 스펙으로 정렬을 구현할 수도 있고, Sort 객체를 사용할 수도 있습니다.
        val sort =
            when (sortType) {
                1 -> Sort.by(Sort.Direction.ASC, "title") // 간단히 title로 '정확도' 대체
                2 -> Sort.by(Sort.Direction.ASC, "dates") // 공연일자 중 가장 빠른 날짜
                3 -> Sort.by(Sort.Direction.DESC, "sales") // 'sales' 컬럼이 있다고 가정
                else -> Sort.by(Sort.Direction.ASC, "title") // 기본 정렬
            }

        // repository 호출
        val performanceEntities = performanceRepository.findAll(spec, sort)

        // DTO 변환
        return performanceEntities.map { Performance.fromEntity(it) }
    }

    fun createPerformance(performance: Performance): Performance {
        TODO()
    }

    fun editPerformance(performance: Performance): Performance {
        TODO()
    }

    fun deletePerformance(performance: Performance) {
        TODO()
    }
}
