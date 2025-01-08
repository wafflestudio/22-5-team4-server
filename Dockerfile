FROM ubuntu:latest
LABEL authors="LG"

ENTRYPOINT ["top", "-b"]