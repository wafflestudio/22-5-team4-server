package com.wafflestudio.interpark.config

import com.wafflestudio.interpark.performance.persistence.PerformanceEntity
import com.wafflestudio.interpark.performance.persistence.PerformanceHallEntity
import com.wafflestudio.interpark.performance.persistence.PerformanceRepository
import com.wafflestudio.interpark.performance.persistence.PerformanceHallRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
class DataInitializer(
    private val performanceHallRepository: PerformanceHallRepository,
    private val performanceRepository: PerformanceRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        // 1) 공연장(Hall) 데이터 넣기
        val hallA = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "명동예술극장",
                address = "서울 중구 명동"
            )
        )
        val hallB = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "대학로아트홀",
                address = "서울 종로구 대학로"
            )
        )
        val hallC = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "LG아트센터 서울",
                address = "서울 강서구 마곡중앙로"
            )
        )

        // 2) PerformanceEntity 여러개 생성
        val performanceList = listOf(
            PerformanceEntity(
                hall = hallA,
                title = "뮤지컬 지킬앤하이드",
                detail = "지킬과 하이드의 이중인격 이야기",
                category = "뮤지컬",
                sales = 1000,
                dates = listOf(LocalDate.of(2025, 12, 25), LocalDate.of(2025, 12, 26)),
                posterUrl = "http://example.com/poster/jekyll.jpg",
                backdropUrl = "http://example.com/backdrop/jekyll.jpg",
                seatIds = listOf("A1", "A2", "A3"),
                reviewIds = listOf("review1", "review2")
            ),
            PerformanceEntity(
                hall = hallA,
                title = "오페라의 유령",
                detail = "파리 오페라 극장에서 벌어지는 미스터리",
                category = "뮤지컬",
                sales = 2000,
                dates = listOf(LocalDate.of(2025, 11, 5), LocalDate.of(2025, 11, 6)),
                posterUrl = "http://example.com/poster/phantom.jpg",
                backdropUrl = "http://example.com/backdrop/phantom.jpg",
                seatIds = listOf("B1", "B2", "B3"),
                reviewIds = listOf("review3", "review4")
            ),
            PerformanceEntity(
                hall = hallB,
                title = "친정엄마",
                detail = "연극으로 만나는 엄마와 딸의 이야기",
                category = "연극",
                sales = 500,
                dates = listOf(LocalDate.of(2026, 1, 10)),
                posterUrl = "http://example.com/poster/mom.jpg",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = listOf("C1", "C2", "C3"),
                reviewIds = listOf("review5")
            )
            // 필요한 만큼 추가...
        )

        // 3) 한번에 저장
        performanceRepository.saveAll(performanceList)

        // 이제 DB에 3개의 PerformanceEntity가 insert됨
    }
}