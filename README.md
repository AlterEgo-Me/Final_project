Финальный проект, этап 1

Реализовано 3 операции: получение баланса, пополнение, списание. База данных PostgreSQL, одна таблица accounts с полями user_id и balance.

REST API:  
GET /balance?user_id=1 - получить баланс пользователя  
POST /put?user_id=1&bigDecimal=100 - пополнить баланс на указанную сумму  
POST /take?user_id=1&bigDecimal=100 - списать сумму с баланса  
Все параметры передаются через query-параметры. Настройки подключения вынесены в database.properties (файл не включен в репозиторий)

Структура базы данных

<img width="369" height="100" alt="image" src="https://github.com/user-attachments/assets/12f96934-eff1-4c8c-89bf-603078406a76" />
