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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
        performanceHallService.createPerformanceHall(
            name = "동덕여자대학교 공연예술센터 코튼홀",
            address = "서울특별시 종로구 동숭길 134",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "대학로 자유극장",
            address = "서울특별시 종로구 대학로12길",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "인터파크 서경스퀘어",
            address = "서울 종로구 동숭길",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "샤롯데씨어터",
            address = "서울특별시 송파구 잠실동",
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
        performanceService.createPerformance(
            title = "종의 기원",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24016611-01.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24016611_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "하트셉수트",
            detail = "https://ticketimage.interpark.com/Play/ITM/Data/Modify/2025/1/2025013111034957.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25001185_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "그해 여름",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/24017986-08.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24017986_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "명성황후",
            detail = "https://ticketimage.interpark.com/P00041072025/01/20/762a8f19.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/P0/P0004107_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "알라딘",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/24012498-18.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24012498_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )

        // 3) Performance Event 데이터 넣기
        val performanceEvents = listOf(
            Triple(
                "뮤지컬 지킬앤하이드",
                "블루스퀘어 신한카드홀",
                generateDateRange("2025-02-05","2025-05-18","16:00:00","18:00:00")
            ),
            Triple(
                "마타하리",
                "LG아트센터 서울 SIGNATURE홀",
                generateDateRange("2025-02-03", "2025-03-02", "16:00:00","18:00:00")
            ),
            Triple(
                "웃는남자",
                "예술의전당 오페라극장",
                generateDateRange("2025-02-09", "2025-03-09", "16:00:00","18:00:00")
            ),
            Triple(
                "2025 기리보이 콘서트",
                "블루스퀘어 마스터카드홀",
                generateDateRange("2025-02-10", "2025-02-02", "16:00:00","18:00:00")
            ),
            Triple(
                "2025 검정치마 단독공연",
                "올림픽공원 올림픽홀",
                generateDateRange("2025-02-03", "2025-02-02", "16:00:00","18:00:00")
            ),
            Triple(
                "콜드플레이 내한공연",
                "고양종합운동장 주경기장",
                generateDateRange("2025-04-16", "2025-04-25", "16:00:00","18:00:00")
            ),
            Triple(
                "브루스 리우 피아노 리사이틀",
                "예술의전당 콘서트홀",
                generateDateRange("2025-05-11", "2025-05-11", "16:00:00","18:00:00")
            ),
            Triple(
                "크리스티안 테츨라프 바이올린 리사이틀",
                "예술의전당 콘서트홀",
                generateDateRange("2025-05-01", "2025-05-01", "16:00:00","18:00:00")
            ),
            Triple(
                "발레의 별빛, 글로벌 발레스타 초청 갈라공연",
                "세종문화회관 대극장",
                generateDateRange("2025-03-11", "2025-04-12", "16:00:00","18:00:00")
            ),
            Triple(
                "연극 애나엑스",
                "LG아트센터 서울 U+ 스테이지",
                generateDateRange("2025-03-11", "2025-04-12", "16:00:00","18:00:00")
            ),
            Triple(
                "연극 타인의 삶",
                "LG아트센터 서울 U+ 스테이지",
                generateDateRange("2025-02-28", "2025-03-16", "16:00:00","18:00:00")
            ),
            Triple(
                "세일즈맨의 죽음",
                "세종문화회관 M씨어터",
                generateDateRange("2025-02-07", "2025-03-03", "16:00:00","18:00:00")
            ),
            Triple(
                "종의 기원",
                "동덕여자대학교 공연예술센터 코튼홀",
                generateDateRange("2025-02-04", "2025-05-03", "16:00:00","18:00:00")
            ),
            Triple(
                "하트셉수트",
                "대학로 자유극장",
                generateDateRange("2025-02-04", "2025-06-01", "16:00:00","18:00:00")
            ),
            Triple(
                "그해 여름",
                "인터파크 서경스퀘어",
                generateDateRange("2025-02-12", "2025-03-02", "16:00:00","18:00:00")
            ),
            Triple(
                "명성황후",
                "세종문화회관 대극장",
                generateDateRange("2025-02-15", "2025-03-30", "16:00:00","18:00:00")
            ),
            Triple(
                "알라딘",
                "샤롯데씨어터",
                generateDateRange("2025-03-01", "2025-06-22", "16:00:00","18:00:00")
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

    fun generateDateRange(
        startDate: String, endDate: String,
        startTime: String, endTime: String
    ): List<List<String>> {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

        val startLocalDate = LocalDate.parse(startDate, dateFormatter)
        val endLocalDate = LocalDate.parse(endDate, dateFormatter)
        val startLocalTime = LocalTime.parse(startTime, timeFormatter)
        val endLocalTime = LocalTime.parse(endTime, timeFormatter)

        val dateList = mutableListOf<List<String>>()
        var currentDate = startLocalDate

        while (!currentDate.isAfter(endLocalDate)) {
            val startDateTime = LocalDateTime.of(currentDate, startLocalTime)
            val endDateTime = LocalDateTime.of(currentDate, endLocalTime)
            dateList.add(listOf(startDateTime.format(dateTimeFormatter), endDateTime.format(dateTimeFormatter)))

            currentDate = currentDate.plusDays(1) // 하루 증가
        }

        return dateList
    }
}