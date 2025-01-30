import io.gatling.core.Predef._
import io.gatling.http.Predef._
import java.util.UUID

class DynamicReservationSimulation extends Simulation {

  // HTTP 설정
  val httpProtocol = http
    .baseUrl("http://localhost") // 테스트 서버 URL
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .acceptLanguageHeader("en-US,en;q=0.5")

  // 🔹 공유할 값 저장 (Scala 변수)
  val sharedUsername = UUID.randomUUID().toString.take(8) // 회원가입은 한 번만 수행

  val setup = exec { session =>
    val newSession = session.set("username", UUID.randomUUID().toString.take(8))
    newSession
  }

  // 🔹 회원가입 및 로그인 (한 번만 실행)
  val signupAndLogin = exec(
    http("회원가입 요청")
      .post("/api/v1/local/signup")
      .body(StringBody(s"""
        {
          "username": "#{username}",
          "password": "password123",
          "nickname": "reviewer",
          "phoneNumber": "010-0000-0000",
          "email": "reviewer@example.com",
          "role": "ADMIN"
        }
      """))
      .check(status.is(200))
  ).exec(
    http("로그인 요청")
      .post("/api/v1/local/signin")
      .body(StringBody(s"""
        {
          "username": "#{username}",
          "password": "password123"
        }
      """))
      .check(status.is(200))
      .check(jsonPath("$.accessToken").saveAs("accessToken")) // 액세스 토큰 저장
  )
  // 공연 정보 ID (고정 값)
  val performanceEventId: String = "0d450f32-e85b-4a20-b7c2-fa9c3d6e4036"

  val fetchPerformanceEventId = exec(
    http("무작위 performanceEventId 가져오기")
      .get("/api/v1/performance-event")
      .check(status.is(200))
      .check(jsonPath("$[0].id").saveAs("performanceEventId"))
  ).exitHereIfFailed

  val fetchSeatInfo = exec(
    http("가능한 좌석 조회")
      .get(s"/api/v1/seat/#{performanceEventId}/available") // 변수 활용
      .header("Authorization", "Bearer #{accessToken}") // 공유된 accessToken 사용
      .check(status.is(200))
      .check(jsonPath("$.availableSeats[*].id").findRandom.saveAs("seatId")) // 좌석 예약 ID 저장
  ).exitHereIfFailed

  val reserveSeatScenario = exec(
    http("좌석 예약 요청")
      .post("/api/v1/reservation/reserve")
      .header("Authorization", "Bearer #{accessToken}") // 공유된 accessToken 사용
      .body(StringBody("""
        {
          "performanceEventId": "#{performanceEventId}",
          "seatId": "#{seatId}"
        }
      """))
      .check(status.in(201, 409)) // 성공(201) 또는 충돌(409)
  )


  val virtualUserCount = 1000

  // 🔹 시나리오: 회원가입 및 로그인은 한 번만 실행, 이후는 모든 사용자 공유
  val scn = scenario("동적 좌석 예약 시나리오")
    .exec(setup)
    .exec(signupAndLogin)
    .exec(fetchPerformanceEventId)
    .exec(fetchSeatInfo) // 좌석 정보 조회
    .rendezVous(virtualUserCount) // 🔹 1000명의 사용자가 이 지점까지 도착할 때까지 대기 후 동시 실행
    .repeat(10) { exec(reserveSeatScenario) } // 좌석 예약 요청 반복

  // 🔹 전체 실행 설정
  setUp(
    scn.inject(
      constantUsersPerSec(virtualUserCount).during(1)
    )
  ).protocols(httpProtocol)

  // // 🔹 회원가입 & 로그인은 한 번만 실행
  // before {
  //   println("🚀 Running pre-test setup: Signing up and logging in...")
  //   scenario("동적 좌석 예약 시나리오")
  //     .exec(signupAndLogin)
  //     .inject(
  //       constantUsersPerSec(1).during(1)
  //     )
  // }
}
