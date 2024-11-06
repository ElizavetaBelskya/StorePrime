## Practice project StorePrime
Проект был разработан в рамках летней практики ИТИС в Тинькофф в 2023.
На данный момент является opensource.

## Технологический стек:
- Java 17
- Spring Boot 2.7.12 
- Spring Security, аутентификация с помощью JWT-токенов
- Spring Data JPA
- Spring Validation
- Swagger
- PostgreSQL 13
- JUnit 5 (Jupiter) + Mockito для тестирования
- Логирование
- Кэширование
- Redis для хранения устаревших JWT-токенов
- MongoDB для хранения изображений
- Docker-контейнеры для запуска Redis, MongoDB 

## Функционал
2 роли: админ (продавец) и покупатель (клиент).

Реализованы следующие процессы:
- Самостоятельная регистрация;
- Создание администратором карточки товара и заведение всех необходимых параметров;
- Поиск товара с возможностью фильтрации минимум по двум параметрам и текстовому поиску;
- Формирование корзины с товарами для покупки;
- "Покупка" товаров из корзины за бонусы на клиентском счете;
- Возврат (отмена) заказа;
  
Админ:
- создает товары;
- просматривает товары (с учетом фильтра);
- меняет статусы заказов;
- просматривает заказы.
  
Покупатель:
- осуществляет поиск и просмотр товаров;
- формирует корзину;
- добавляет/удаляет товары из корзины;
- покупает товары из корзины за бонусы на его счете, т.е. создает заказ;
- просматривает свои заказы;
- отменяет заказ;

Дополнительно реализованы:
- фильтрация по большему количеству параметров;
- покупка в корзине нескольких единиц одного товара;
- проверка на количество покупаемого товара и товара в наличии;
- отмена одного товара из корзины;
- просмотр собственной истории заказов клиентом;
