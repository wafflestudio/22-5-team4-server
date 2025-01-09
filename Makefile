all:
	docker compose up -d mysql
	./gradlew build
	docker build -t myapp:1.0 .
	docker compose up