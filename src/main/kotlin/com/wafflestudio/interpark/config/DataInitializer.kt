package com.wafflestudio.interpark.config

import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
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
        val BlueSquare_ShinHanCardHall = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "블루스퀘어 신한카드홀",
                address = "서울 용산구 한남동 이태원로"
            )
        )
        val BlueSquare_MasterCardHall = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "블루스퀘어 마스터카드홀",
                address = "서울 용산구 한남동 이태원로"
            )
        )
        val SeoulArtsCenter_OperaHouse = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "예술의전당 오페라극장",
                address = "서울 서초구 남부순환로"
            )
        )
        val LGArtCenterSeoul_SIGNATUREHAll = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "LG아트센터 서울 SIGNATURE홀",
                address = "서울 강서구 마곡동 마곡중앙로"
            )
        )
        val OlympicPark_OlympicHall = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "올림픽공원 올림픽홀",
                address = "서울 송파구 방이동"
            )
        )

        // 2) PerformanceEntity 여러개 생성
        val performanceList = listOf(
            // 뮤지컬
            PerformanceEntity(
                hall = BlueSquare_ShinHanCardHall,
                title = "뮤지컬 지킬앤하이드",
                detail = "지금 이 순간, 끝나지 않는 신화",
                category = PerformanceCategory.MUSICAL,
                sales = 1000,
                dates = listOf(LocalDate.of(2024, 11, 29), LocalDate.of(2025, 5, 18)),
                posterUrl = "http://example.com/poster/jekyll.jpg",
                backdropUrl = "http://example.com/backdrop/jekyll.jpg",
                seatIds = listOf("A1", "A2", "A3"),
                reviewIds = listOf("review1", "review2")
            ),
            PerformanceEntity(
                hall = LGArtCenterSeoul_SIGNATUREHAll,
                title = "마타하리",
                detail = "She's BACK!",
                category = PerformanceCategory.MUSICAL,
                sales = 2000,
                dates = listOf(LocalDate.of(2024, 12, 5), LocalDate.of(2025, 3, 2)),
                posterUrl = "http://example.com/poster/phantom.jpg",
                backdropUrl = "http://example.com/backdrop/phantom.jpg",
                seatIds = listOf("B1", "B2", "B3"),
                reviewIds = listOf("review3", "review4")
            ),
            PerformanceEntity(
                hall = SeoulArtsCenter_OperaHouse,
                title = "웃는남자",
                detail = "부자들의 낙원은 가난한 자들의 지옥으로 세워진 것이다",
                category = PerformanceCategory.MUSICAL,
                sales = 500,
                dates = listOf(LocalDate.of(2025, 1, 9), LocalDate.of(2025, 3, 9)),
                posterUrl = "http://example.com/poster/mom.jpg",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = listOf("C1", "C2", "C3"),
                reviewIds = listOf("review5")
            ),
            // 콘서트
            PerformanceEntity(
                hall = BlueSquare_MasterCardHall,
                title = "2025 기리보이 콘서트",
                detail = "2252:2522",
                category = PerformanceCategory.CONCERT,
                sales = 0,
                dates = listOf(LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 2)),
                posterUrl = "http://example.com/poster/mom.jpg",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),
            PerformanceEntity(
                hall = BlueSquare_MasterCardHall,
                title = "2025 검정치마 단독공연",
                detail = "SONGS TO BRING YOU HOME",
                category = PerformanceCategory.CONCERT,
                sales = 0,
                dates = listOf(LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 2)),
                posterUrl = "http://example.com/poster/mom.jpg",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),
            // 클래식
            // 연극
        )

        // 3) 한번에 저장
        performanceRepository.saveAll(performanceList)

        // 이제 DB에 3개의 PerformanceEntity가 insert됨
    }
}