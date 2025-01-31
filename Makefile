OS := $(shell uname)

ifeq ($(OS), MINGW32_NT-6.2)
    GRADLE_CMD := gradlew
else
    GRADLE_CMD := ./gradlew
endif

all:
	docker compose up -d mysql
	$(GRADLE_CMD) build
	docker build -t myapp:1.0 .
	docker compose up