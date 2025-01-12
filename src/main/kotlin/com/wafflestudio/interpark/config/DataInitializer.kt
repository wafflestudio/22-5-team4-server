package com.wafflestudio.interpark.config

import com.wafflestudio.interpark.performance.PerformanceHallNotFoundException
import com.wafflestudio.interpark.performance.PerformanceNotFoundException
import com.wafflestudio.interpark.performance.persistence.PerformanceCategory
import com.wafflestudio.interpark.performance.persistence.PerformanceHallRepository
import com.wafflestudio.interpark.performance.persistence.PerformanceRepository
import com.wafflestudio.interpark.performance.service.PerformanceEventService
import com.wafflestudio.interpark.performance.service.PerformanceHallService
import com.wafflestudio.interpark.performance.service.PerformanceService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer(
    private val performanceService: PerformanceService,
    private val performanceEventService: PerformanceEventService,
    private val performanceHallService: PerformanceHallService,
    private val performanceRepository: PerformanceRepository,
    private val performanceHallRepository: PerformanceHallRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {

        // 1) 공연장 데이터 넣기
        performanceHallService.createPerformanceHall(
            name = "블루스퀘어 신한카드홀",
            address = "서울 용산구 한남동 이태원로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "블루스퀘어 마스터카드홀",
            address = "서울 용산구 한남동 이태원로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "예술의전당 오페라극장",
            address = "서울 서초구 남부순환로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "예술의전당 콘서트홀",
            address = "서울 서초구 남부순환로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "LG아트센터 서울 SIGNATURE홀",
            address = "서울 강서구 마곡동 마곡중앙로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "LG아트센터 서울 U+ 스테이지",
            address = "서울 강서구 마곡동 마곡중앙로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "올림픽공원 올림픽홀",
            address = "서울 송파구 방이동",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "고양종합운동장 주경기장",
            address = "경기도 고양시 일산서구 대화동 중앙로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "세종문화회관 대극장",
            address = "서울 종로구 세종대로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "세종문화회관 M씨어터",
            address = "서울 종로구 세종대로",
            maxAudience = 100
        )

        // 2) Performance 데이터 넣기
        performanceService.createPerformance(
            title = "뮤지컬 지킬앤하이드",
            detail = "지금 이 순간, 끝나지 않는 신화",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24013928_p.gif",
            backdropImageUri = "http://example.com/backdrop/jekyll.jpg"
        )
        performanceService.createPerformance(
            title = "마타하리",
            detail = "She's BACK!",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/L0/L0000106_p.gif",
            backdropImageUri = "http://example.com/backdrop/phantom.jpg"
        )
        performanceService.createPerformance(
            title = "웃는남자",
            detail = "부자들의 낙원은 가난한 자들의 지옥으로 세워진 것이다",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24016737_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "2025 기리보이 콘서트",
            detail = "2252:2522",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24018543_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "2025 검정치마 단독공연",
            detail = "SONGS TO BRING YOU HOME",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000084_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "콜드플레이 내한공연",
            detail = "MUSIC of the SPHERES",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24013437_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "브루스 리우 피아노 리사이틀",
            detail = "TCHAIKOVSKY | MENDELSSOHN | SCRIABIN | PROKOFIEV",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24016119_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "크리스티안 테츨라프 바이올린 리사이틀",
            detail = "SUK | BRAHMS | SZYMANOWSKI | FRANCK",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24015137_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "발레의 별빛, 글로벌 발레스타 초청 갈라공연",
            detail = "전 세계가 먼저 찾는 한국 스타 무용수들의 향연!",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/P0/P0004046_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "연극 애나엑스",
            detail = "ANNA X",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/L0/L0000107_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "연극 타인의 삶",
            detail = "영화 타인의 삶 원작",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/L0/L0000104_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "세일즈맨의 죽음",
            detail = "현 희곡의 거장 '아서 밀러'의 대표작 연극<세일즈맨의 죽음>이 돌아왔다!",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24017573_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )

        // 3) Performance Event 데이터 넣기
        val performanceEvents = listOf(
            Triple(
                "블루스퀘어 신한카드홀",
                "뮤지컬 지킬앤하이드",
                listOf(
                    listOf("2024-11-29T16:00:00", "2024-11-29T18:00:00"),
                    listOf("2025-05-18T16:00:00", "2025-05-18T18:00:00")
                )
            ),
            Triple(
                "LG아트센터 서울 SIGNATURE홀",
                "마타하리",
                listOf(
                    listOf("2024-12-05T16:00:00", "2024-12-05T18:00:00"),
                    listOf("2025-03-02T16:00:00", "2025-03-02T18:00:00")
                )
            ),
            Triple(
                "예술의전당 오페라극장",
                "웃는남자",
                listOf(
                    listOf("2025-01-09T16:00:00", "2025-01-09T18:00:00"),
                    listOf("2025-03-09T16:00:00", "2025-03-09T18:00:00")
                )
            ),
            Triple(
                "블루스퀘어 마스터카드홀",
                "2025 기리보이 콘서트",
                listOf(
                    listOf("2025-02-01T16:00:00", "2025-02-01T18:00:00"),
                    listOf("2025-02-02T16:00:00", "2025-02-02T18:00:00")
                )
            ),
            Triple(
                "올림픽공원 올림픽홀",
                "2025 검정치마 단독공연",
                listOf(
                    listOf("2025-02-01T16:00:00", "2025-02-01T18:00:00"),
                    listOf("2025-02-02T16:00:00", "2025-02-02T18:00:00")
                )
            ),
            Triple(
                "고양종합운동장 주경기장",
                "콜드플레이 내한공연",
                listOf(
                    listOf("2025-04-16T16:00:00", "2025-04-16T18:00:00"),
                    listOf("2025-04-25T16:00:00", "2025-04-25T18:00:00")
                )
            ),
            Triple(
                "예술의전당 콘서트홀",
                "브루스 리우 피아노 리사이틀",
                listOf(
                    listOf("2025-05-11T16:00:00", "2025-05-11T18:00:00")
                )
            ),
            Triple(
                "예술의전당 콘서트홀",
                "크리스티안 테츨라프 바이올린 리사이틀",
                listOf(
                    listOf("2025-05-01T16:00:00", "2025-05-01T18:00:00")
                )
            ),
            Triple(
                "세종문화회관 대극장",
                "발레의 별빛, 글로벌 발레스타 초청 갈라공연",
                listOf(
                    listOf("2025-01-11T16:00:00", "2025-01-11T18:00:00"),
                    listOf("2025-01-12T16:00:00", "2025-01-12T18:00:00")
                )
            ),
            Triple(
                "LG아트센터 서울 U+ 스테이지",
                "연극 애나엑스",
                listOf(
                    listOf("2025-01-28T16:00:00", "2025-01-28T18:00:00"),
                    listOf("2025-03-16T16:00:00", "2025-03-16T18:00:00")
                )
            ),
            Triple(
                "LG아트센터 서울 U+ 스테이지",
                "연극 타인의 삶",
                listOf(
                    listOf("2025-01-28T16:00:00", "2025-01-28T18:00:00"),
                    listOf("2025-03-16T16:00:00", "2025-03-16T18:00:00")
                )
            ),
            Triple(
                "세종문화회관 M씨어터",
                "세일즈맨의 죽음",
                listOf(
                    listOf("2025-01-07T16:00:00", "2025-01-07T18:00:00"),
                    listOf("2025-03-03T16:00:00", "2025-03-03T18:00:00")
                )
            )
        )

        performanceEvents.forEach { (performanceTitle, hallName, eventTimes) ->
            // PerformanceRepository를 통해 공연 조회
            val performance = performanceRepository.findByTitle(performanceTitle)
                ?: throw PerformanceNotFoundException()

            // PerformanceHallRepository를 통해 공연장 조회
            val hall = performanceHallRepository.findByName(hallName)
                ?: throw PerformanceHallNotFoundException()

            // 이벤트 생성
            eventTimes.forEach { (startAt, endAt) ->
                performanceEventService.createPerformanceEvent(
                    performanceId = performance.id!!,
                    performanceHallId = hall.id!!,
                    startAt = startAt,
                    endAt = endAt
                )
            }
        }
    }
}