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
        performanceHallService.createPerformanceHall(
            name = "디큐브 링크아트센터",
            address = "서울시 구로구 경인로 662 7층",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "예술의전당 CJ 토월극장",
            address = " 서울특별시 서초구 서초동",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "인터파크 유니플렉스 1관",
            address = "서울특별시 종로구 동숭동",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "인터파크 유니플렉스 2관",
            address = "서울특별시 종로구 동숭동",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "콘텐츠박스",
            address = "서울시 종로구 동숭동",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "지인시어터",
            address = "서울특별시 종로구 동숭길 25",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "링크아트센터 벅스홀",
            address = "서울특별시 종로구 대학로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "라온아트홀",
            address = "서울특별시 종로구 대학로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "국립극장 달오름극장",
            address = "서울 중구 장충단로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "대학로 아트하우스",
            address = "서울특별시 종로구 대학로10길",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "대학로 제나아트홀",
            address = "서울특별시 종로구 대학로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "예술의전당 자유소극장",
            address = "서울특별시 서초구 서초동",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "KNN시어터",
            address = "부산광역시 해운대구",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "용인포은아트홀",
            address = "경기도 용인시 수지구 포은대로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "롯데콘서트홀",
            address = "서울특별시 송파구 올림픽로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "국립정동극장",
            address = "서울 중구 정동길",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "유니버셜아트센터",
            address = "서울시 광진구 능동",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "서울랜드",
            address = "경기도 과천시 광명로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "KSPO DOME",
            address = "서울특별시 송파구 올림픽로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "인스파이어 아레나",
            address = "인천광역시 중구 공항문화로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "광주예술의전당 대극장",
            address = "광주광역시 북구 북문대로",
            maxAudience = 100
        )
        performanceHallService.createPerformanceHall(
            name = "의정부예술의전당 대극장",
            address = "경기도 의정부시 의정로 1",
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
        performanceService.createPerformance(
            title = "베르테르 25주년 공연",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24017198-07.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24017198_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "시라노",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24014885-18.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24014885_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "여신님이 보고 계셔",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24014618-03.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24014618_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "빨래",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24006709-30.jpg",
            category = PerformanceCategory.MUSICAL,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24006709_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "쉬어매드니스",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24006928-20.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24006928_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "죽여주는 이야기",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/23008491-103.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/23/23008491_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "꽃의 비밀",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/24018192-06.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24018192_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "한뼘사이",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/22014277-143.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/22/22014277_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "붉은 낙엽",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/24016741-11.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24016741_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "사랑해 엄마",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/24015841-19.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24015841_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "사내연애 보고서",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24008626-24.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24008626_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "바닷마을 다이어리",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/24017992-12.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24017992_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "불편한 편의점",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24004660-31.jpg",
            category = PerformanceCategory.PLAY,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24004660_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "지브리 OST 콘서트 : 디 오케스트라",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000133-03.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000133_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "라흐마니노프",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000715-01.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000715_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "코리안챔버오케스트라 창단 60주년 기념",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24018282-02.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24018282_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "토요키즈클래식",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000321-02.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000321_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "IBK기업은행과 함께하는 예술의전당 토요콘서트",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000463-02.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000463_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "지브리＆디즈니 영화음악 FESTA",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000640-05.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000640_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "광대",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/24018235-04.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24018235_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "백건우와 모차르트 〈Program Ⅱ〉",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/24017702-06.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24017702_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "피아노 파 드 되 - Dancing with Pierrot",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24018007-04.jpg",
            category = PerformanceCategory.CLASSIC,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24018007_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "정동원棟동 이야기話화 3rd 전국투어 콘서트",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25001118-01.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25001118_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "2025 폴킴 소극장 콘서트",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000089-04.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000089_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "2025 World DJ Festival",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24010212-04.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24010212_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "j-hope Tour ‘HOPE ON THE STAGE’ in SEOUL",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000014-01.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000014_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "TAEYANG 2025 TOUR",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24018375-04.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000516_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "“TAK SHOW3” - 앙코르",
            detail = "https://ticketimage.interpark.com/Play/image/etc/24/24018317-05.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/24/24018317_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "2025 윤하 앵콜 콘서트 〈GROWTH THEORY : Final Edition〉",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000432-02.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000432_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "2024-25 Theatre 이문세",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000934-01.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000934_p.gif",
            backdropImageUri = "http://example.com/backdrop/mom.jpg"
        )
        performanceService.createPerformance(
            title = "김창옥 토크콘서트 시즌4",
            detail = "https://ticketimage.interpark.com/Play/image/etc/25/25000115-01.jpg",
            category = PerformanceCategory.CONCERT,
            posterUri = "https://ticketimage.interpark.com/Play/image/large/25/25000115_p.gif",
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
                generateDateRange("2025-02-05", "2025-02-10", "16:00:00","18:00:00")
            ),
            Triple(
                "2025 검정치마 단독공연",
                "올림픽공원 올림픽홀",
                generateDateRange("2025-02-03", "2025-02-04", "16:00:00","18:00:00")
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
            ),
            Triple(
                "베르테르 25주년 공연",
                "디큐브 링크아트센터",
                generateDateRange("2025-02-15", "2025-02-16", "16:00:00","18:00:00")
            ),
            Triple(
                "시라노",
                "예술의전당 CJ 토월극장",
                generateDateRange("2025-02-15", "2025-02-16", "16:00:00","18:00:00")
            ),
            Triple(
                "여신님이 보고 계셔",
                "인터파크 유니플렉스 1관",
                listOf(
                    listOf("2025-02-07T16:00:00", "2025-02-07T18:00:00"),
                    listOf("2025-02-08T16:00:00", "2025-02-08T18:00:00"),
                    listOf("2025-02-09T16:00:00", "2025-02-09T18:00:00"),
                    listOf("2025-02-14T16:00:00", "2025-02-14T18:00:00"),
                    listOf("2025-02-15T16:00:00", "2025-02-15T18:00:00"),
                    listOf("2025-02-16T16:00:00", "2025-02-16T18:00:00"),
                )
            ),
            Triple(
                "빨래",
                "인터파크 유니플렉스 2관",
                listOf(
                    listOf("2025-02-07T16:00:00", "2025-02-07T18:00:00"),
                    listOf("2025-02-08T16:00:00", "2025-02-08T18:00:00"),
                    listOf("2025-02-09T16:00:00", "2025-02-09T18:00:00"),
                    listOf("2025-02-14T16:00:00", "2025-02-14T18:00:00"),
                    listOf("2025-02-15T16:00:00", "2025-02-15T18:00:00"),
                    listOf("2025-02-16T16:00:00", "2025-02-16T18:00:00"),
                )
            ),
            Triple(
                "쉬어매드니스",
                "콘텐츠박스",
                generateDateRange("2025-02-15", "2025-02-18", "16:00:00","18:00:00")
            ),
            Triple(
                "죽여주는 이야기",
                "지인시어터",
                generateDateRange("2025-02-15", "2025-02-18", "16:00:00","18:00:00")
            ),
            Triple(
                "꽃의 비밀",
                "링크아트센터 벅스홀",
                generateDateRange("2025-02-15", "2025-02-28", "16:00:00","18:00:00")
            ),
            Triple(
                "한뼘사이",
                "라온아트홀",
                generateDateRange("2025-02-28", "2025-03-01", "16:00:00","18:00:00")
            ),
            Triple(
                "붉은 낙엽",
                "국립극장 달오름극장",
                generateDateRange("2025-02-20", "2025-02-25", "16:00:00","18:00:00")
            ),
            Triple(
                "사랑해 엄마",
                "대학로 아트하우스",
                generateDateRange("2025-02-20", "2025-02-25", "16:00:00","18:00:00")
            ),
            Triple(
                "사내연애 보고서",
                "대학로 제나아트홀",
                generateDateRange("2025-02-20", "2025-02-25", "16:00:00","18:00:00")
            ),
            Triple(
                "바닷마을 다이어리",
                "예술의전당 자유소극장",
                generateDateRange("2025-03-01", "2025-03-31", "16:00:00","18:00:00")
            ),
            Triple(
                "불편한 편의점",
                "KNN시어터",
                generateDateRange("2025-02-03", "2025-02-28", "16:00:00","18:00:00")
            ),
            Triple(
                "지브리 OST 콘서트 : 디 오케스트라",
                "예술의전당 콘서트홀",
                generateDateRange("2025-02-15", "2025-02-18", "16:00:00","18:00:00")
            ),
            Triple(
                "라흐마니노프",
                "예술의전당 콘서트홀",
                generateDateRange("2025-02-10", "2025-02-10", "16:00:00","18:00:00")
            ),
            Triple(
                "코리안챔버오케스트라 창단 60주년 기념",
                "예술의전당 콘서트홀",
                generateDateRange("2025-03-02", "2025-03-02", "16:00:00","18:00:00")
            ),
            Triple(
                "토요키즈클래식",
                "용인포은아트홀",
                generateDateRange("2025-02-15", "2025-06-21", "16:00:00","18:00:00")
            ),
            Triple(
                "IBK기업은행과 함께하는 예술의전당 토요콘서트",
                "예술의전당 콘서트홀",
                generateDateRange("2025-02-15", "2025-02-15", "16:00:00","18:00:00")
            ),
            Triple(
                "지브리＆디즈니 영화음악 FESTA",
                "롯데콘서트홀",
                generateDateRange("2025-03-05", "2025-03-05", "16:00:00","18:00:00")
            ),
            Triple(
                "광대",
                "국립정동극장",
                generateDateRange("2025-02-12", "2025-02-21", "16:00:00","18:00:00")
            ),
            Triple(
                "백건우와 모차르트 〈Program Ⅱ〉",
                "예술의전당 콘서트홀",
                generateDateRange("2025-03-10", "2025-03-10", "16:00:00","18:00:00")
            ),
            Triple(
                "피아노 파 드 되 - Dancing with Pierrot",
                "유니버셜아트센터",
                generateDateRange("2025-02-24", "2025-03-07", "16:00:00","18:00:00")
            ),
            Triple(
                "정동원棟동 이야기話화 3rd 전국투어 콘서트",
                "올림픽공원 올림픽홀",
                generateDateRange("2025-03-28", "2025-03-30", "16:00:00","18:00:00")
            ),
            Triple(
                "2025 폴킴 소극장 콘서트",
                "블루스퀘어 마스터카드홀",
                generateDateRange("2025-02-08", "2025-02-16", "16:00:00","18:00:00")
            ),
            Triple(
                "2025 World DJ Festival",
                "서울랜드",
                generateDateRange("2025-06-14", "2025-06-15", "16:00:00","18:00:00")
            ),
            Triple(
                "j-hope Tour ‘HOPE ON THE STAGE’ in SEOUL",
                "KSPO DOME",
                generateDateRange("2025-02-28", "2025-03-02", "16:00:00","18:00:00")
            ),
            Triple(
                "TAEYANG 2025 TOUR",
                "인스파이어 아레나",
                generateDateRange("2025-02-05", "2025-02-05", "16:00:00","18:00:00")
            ),
            Triple(
                "“TAK SHOW3” - 앙코르",
                "KSPO DOME",
                generateDateRange("2025-02-22", "2025-02-23", "16:00:00","18:00:00")
            ),
            Triple(
                "2025 윤하 앵콜 콘서트 〈GROWTH THEORY : Final Edition〉",
                "KSPO DOME",
                generateDateRange("2025-02-14", "2025-02-16", "16:00:00","18:00:00")
            ),
            Triple(
                "2024-25 Theatre 이문세",
                "광주예술의전당 대극장",
                generateDateRange("2025-04-11", "2025-04-12", "16:00:00","18:00:00")
            ),
            Triple(
                "김창옥 토크콘서트 시즌4",
                "의정부예술의전당 대극장",
                generateDateRange("2025-03-16", "2025-03-16", "16:00:00","18:00:00")
            ),
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