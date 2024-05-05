# Ganeev-telegram-bot
## Введение
Приложение представляет собой "Мини-банк", состоящий из трех компонентов:
- Frontend(telegram-bot на Java)
- Middle-слой (Java-сервис)
- Backend (Java-сервис)


<p align="center">
Разделы 
</p>

<p align="center">&nbsp;&bull;&nbsp;
<a href="##tools">Tools</a> &nbsp;&bull;&nbsp;
<a href="#description-">Description</a> &nbsp;&bull;&nbsp;
<a href="#how-it-works">How it works</a> &nbsp;&bull;&nbsp;
<a href="#quick-start">Quick start</a> &nbsp;&bull;&nbsp;
</p>

## Tools
- Java
- Gradle
- Spring
- Posgresql


## Description 
### Frontend 
Принимает запросы пользователей через телеграм и отправляет их в промежуточный слой
### Middle-слой
Представляет собой промежуточный слой, который принимает запросы от telegram-бота, выполняет валидацию и маршрутизирует запросы в "Банк"
### Backend
Система, которая выступает в качестве автоматизированной банковской системы, выполняющей транзакции и хранящей клиентские данные

## How it works
```plantuml
@startUML
actor Клиент
participant Frontend
participant Middle
participant Backend

activate Клиент
Клиент -> Frontend : Запрос \n на действие со счетом
activate Frontend
Frontend -> Middle : Запрос \n на промежуточный слой 
activate Middle 
alt Запрос прошел валидацию
Middle -> Backend: Запрос в АБС
activate Backend
Backend -> Backend: Выполнение транзации
Backend --> Middle: Ответ от АБС
end
deactivate Backend
Middle --> Frontend  : Ответ \n от промежуточного слоя
deactivate Middle 


Frontend --> Клиент : Клиент получает ответ
deactivate Frontend

deactivate Клиент
@endUML
```


## Quick start
Will be here soon...

