FROM openjdk:24-slim-bullseye
WORKDIR /app
COPY build/libs/* build/lib/
ARG TOKEN
ENV TELEGRAM_BOT_TOKEN $TOKEN
COPY build/libs/ganeev-telegram-bot-0.0.1-SNAPSHOT.jar build/telegram-bot.jar
WORKDIR /app/build
ENTRYPOINT ["java", "-jar", "telegram-bot.jar"]