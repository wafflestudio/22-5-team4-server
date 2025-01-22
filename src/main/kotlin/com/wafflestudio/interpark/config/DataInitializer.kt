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
import java.time.LocalDateTime

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
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/24013928-21.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24013928_p.gif",
            backdropImageUri = "http://example.com/backdrop/jekyll.jpg"
        )
        performanceService.createPerformance(
            title = "마타하리",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/L0000106-08.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/L0/L0000106_p.gif",
            backdropImageUri = "http://example.com/backdrop/phantom.jpg"
        )
        performanceService.createPerformance(
            title = "웃는남자",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24016737-04.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24016737_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "2025 기리보이 콘서트",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24018543-01.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24018543_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "2025 검정치마 단독공연",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000084-01.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000084_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "콜드플레이 내한공연",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24013437-06.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24013437_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "브루스 리우 피아노 리사이틀",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24016119-01.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24016119_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "크리스티안 테츨라프 바이올린 리사이틀",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24015137-01.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24015137_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "발레의 별빛, 글로벌 발레스타 초청 갈라공연",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/P0004046-06.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/P0/P0004046_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "연극 애나엑스",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/L0000107-02.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/L0/L0000107_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "연극 타인의 삶",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/L0000104-05.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/L0/L0000104_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "세일즈맨의 죽음",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/24017573-06.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24017573_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )

        // 3) Performance Event 데이터 넣기
        val performanceEvents = listOf(
            Triple(
                "뮤지컬 지킬앤하이드",
                "블루스퀘어 신한카드홀",
                listOf(
                    listOf("2024-11-29T16:00:00", "2024-11-29T18:00:00"),
                    listOf("2025-05-18T16:00:00", "2025-05-18T18:00:00")
                )
            ),
            Triple(
                "마타하리",
                "LG아트센터 서울 SIGNATURE홀",
                listOf(
                    listOf("2024-12-05T16:00:00", "2024-12-05T18:00:00"),
                    listOf("2025-03-02T16:00:00", "2025-03-02T18:00:00")
                )
            ),
            Triple(
                "웃는남자",
                "예술의전당 오페라극장",
                listOf(
                    listOf("2025-01-09T16:00:00", "2025-01-09T18:00:00"),
                    listOf("2025-03-09T16:00:00", "2025-03-09T18:00:00")
                )
            ),
            Triple(
                "2025 기리보이 콘서트",
                "블루스퀘어 마스터카드홀",
                listOf(
                    listOf("2025-02-01T16:00:00", "2025-02-01T18:00:00"),
                    listOf("2025-02-02T16:00:00", "2025-02-02T18:00:00")
                )
            ),
            Triple(
                "2025 검정치마 단독공연",
                "올림픽공원 올림픽홀",
                listOf(
                    listOf("2025-02-01T16:00:00", "2025-02-01T18:00:00"),
                    listOf("2025-02-02T16:00:00", "2025-02-02T18:00:00")
                )
            ),
            Triple(
                "콜드플레이 내한공연",
                "고양종합운동장 주경기장",
                listOf(
                    listOf("2025-04-16T16:00:00", "2025-04-16T18:00:00"),
                    listOf("2025-04-25T16:00:00", "2025-04-25T18:00:00")
                )
            ),
            Triple(
                "브루스 리우 피아노 리사이틀",
                "예술의전당 콘서트홀",
                listOf(
                    listOf("2025-05-11T16:00:00", "2025-05-11T18:00:00")
                )
            ),
            Triple(
                "크리스티안 테츨라프 바이올린 리사이틀",
                "예술의전당 콘서트홀",
                listOf(
                    listOf("2025-05-01T16:00:00", "2025-05-01T18:00:00")
                )
            ),
            Triple(
                "발레의 별빛, 글로벌 발레스타 초청 갈라공연",
                "세종문화회관 대극장",
                listOf(
                    listOf("2025-01-11T16:00:00", "2025-01-11T18:00:00"),
                    listOf("2025-01-12T16:00:00", "2025-01-12T18:00:00")
                )
            ),
            Triple(
                "연극 애나엑스",
                "LG아트센터 서울 U+ 스테이지",
                listOf(
                    listOf("2025-01-28T16:00:00", "2025-01-28T18:00:00"),
                    listOf("2025-03-16T16:00:00", "2025-03-16T18:00:00")
                )
            ),
            Triple(
                "연극 타인의 삶",
                "LG아트센터 서울 U+ 스테이지",
                listOf(
                    listOf("2025-01-28T16:00:00", "2025-01-28T18:00:00"),
                    listOf("2025-03-16T16:00:00", "2025-03-16T18:00:00")
                )
            ),
            Triple(
                "세일즈맨의 죽음",
                "세종문화회관 M씨어터",
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
                    startAt = LocalDateTime.parse(startAt),
                    endAt = LocalDateTime.parse(endAt)
                )
            }
        }
    }
}