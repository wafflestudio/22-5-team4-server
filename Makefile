# 운영체제 감지
OS := $(shell uname)

# gradlew 명령어 설정
ifeq ($(OS),Linux)
	GRADLE_CMD = ./gradlew
else ifeq ($(OS),Darwin)
	GRADLE_CMD = ./gradlew
else ifneq (,$(findstring MINGW,$(OS)))
	GRADLE_CMD = gradlew
else
	GRADLE_CMD = ./gradlew
endif

# 실행 명령어
all:
	docker compose up -d mysql
	$(GRADLE_CMD) build
	docker build -t myapp:1.0 .
	docker compose up