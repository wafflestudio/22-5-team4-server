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
        val SeoulArtsCenter_ConcertHall = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "예술의전당 콘서트홀",
                address = "서울 서초구 남부순환로"
            )
        )
        val LGArtCenterSeoul_SIGNATUREHAll = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "LG아트센터 서울 SIGNATURE홀",
                address = "서울 강서구 마곡동 마곡중앙로"
            )
        )
        val LGArtCenterSeoul_UPlusStage = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "LG아트센터 서울 U+ 스테이지",
                address = "서울 강서구 마곡동 마곡중앙로"
            )
        )
        val OlympicPark_OlympicHall = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "올림픽공원 올림픽홀",
                address = "서울 송파구 방이동"
            )
        )
        val GoyangSportsComplex_MainStadium = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "고양종합운동장 주경기장",
                address = "경기도 고양시 일산서구 대화동 중앙로"
            )
        )
        val SejongCenter_GrandTheater = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "세종문화회관 대극장",
                address = "서울 종로구 세종대로"
            )
        )
        val SejongCenter_MTheater = performanceHallRepository.save(
            PerformanceHallEntity(
                name = "세종문화회관 M씨어터",
                address = "서울 종로구 세종대로"
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
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/24/24013928_p.gif",
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
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/L0/L0000106_p.gif",
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
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/24/24016737_p.gif",
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
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/24/24018543_p.gif",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),
            PerformanceEntity(
                hall = OlympicPark_OlympicHall,
                title = "2025 검정치마 단독공연",
                detail = "SONGS TO BRING YOU HOME",
                category = PerformanceCategory.CONCERT,
                sales = 0,
                dates = listOf(LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 2)),
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/25/25000084_p.gif",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),
            PerformanceEntity(
                hall = GoyangSportsComplex_MainStadium,
                title = "콜드플레이 내한공연",
                detail = "MUSIC of the SPHERES",
                category = PerformanceCategory.CONCERT,
                sales = 0,
                dates = listOf(LocalDate.of(2025, 4, 16), LocalDate.of(2025, 4, 25)),
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/24/24013437_p.gif",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),

            // 클래식
            PerformanceEntity(
                hall = SeoulArtsCenter_ConcertHall,
                title = "브루스 리우 피아노 리사이틀",
                detail = "TCHAIKOVSKY | MENDELSSOHN | SCRIABIN | PROKOFIEV",
                category = PerformanceCategory.CLASSIC,
                sales = 0,
                dates = listOf(LocalDate.of(2025, 5, 11)),
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/24/24016119_p.gif",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),
            PerformanceEntity(
                hall = SeoulArtsCenter_ConcertHall,
                title = "크리스티안 테츨라프 바이올린 리사이틀",
                detail = "SUK | BRAHMS | SZYMANOWSKI | FRANCK",
                category = PerformanceCategory.CLASSIC,
                sales = 0,
                dates = listOf(LocalDate.of(2025, 5, 1)),
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/24/24015137_p.gif",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),
            PerformanceEntity(
                hall = SejongCenter_GrandTheater,
                title = "발레의 별빛, 글로벌 발레스타 초청 갈라공연",
                detail = "전 세계가 먼저 찾는 한국 스타 무용수들의 향연!",
                category = PerformanceCategory.CLASSIC,
                sales = 0,
                dates = listOf(LocalDate.of(2025, 1, 11), LocalDate.of(2025, 1, 12)),
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/P0/P0004046_p.gif",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),

            // 연극
            PerformanceEntity(
                hall = LGArtCenterSeoul_UPlusStage,
                title = "연극 애나엑스",
                detail = "ANNA X",
                category = PerformanceCategory.PLAY,
                sales = 0,
                dates = listOf(LocalDate.of(2025, 1, 28), LocalDate.of(2025, 3, 16)),
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/L0/L0000107_p.gif",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),
            PerformanceEntity(
                hall = LGArtCenterSeoul_UPlusStage,
                title = "연극 타인의 삶",
                detail = "영화 타인의 삶 원작",
                category = PerformanceCategory.PLAY,
                sales = 0,
                dates = listOf(LocalDate.of(2025, 1, 28), LocalDate.of(2025, 3, 16)),
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/L0/L0000104_p.gif",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),
            PerformanceEntity(
                hall = SejongCenter_MTheater,
                title = "세일즈맨의 죽음",
                detail = "현 희곡의 거장 '아서 밀러'의 대표작 연극<세일즈맨의 죽음>이 돌아왔다!",
                category = PerformanceCategory.PLAY,
                sales = 0,
                dates = listOf(LocalDate.of(2025, 1, 7), LocalDate.of(2025, 3, 3)),
                posterUrl = "https://ticketimage.interpark.com/Play/image/large/24/24017573_p.gif",
                backdropUrl = "http://example.com/backdrop/mom.jpg",
                seatIds = emptyList(),
                reviewIds = emptyList()
            ),
        )

        // 3) 한번에 저장
        performanceRepository.saveAll(performanceList)
    }
}