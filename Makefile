OS := $(shell uname -s)

ifeq ($(OS), MINGW32_NT-6.2)
    GRADLE_CMD := gradlew
else
    GRADLE_CMD := ./gradlew
endif

# 실행 명령어
all:
	$(GRADLE_CMD) build
	docker build -t myapp:1.0 .
	docker compose up