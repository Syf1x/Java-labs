# Нагрузочное тестирование (JMeter)

Test plan: `employee-load-test.jmx`. Три группы потоков нацелены на `localhost:8080` (запусти приложение перед тестом):

1. **Unsafe Counter (race condition)** — 60 потоков × 50 итераций (3000 запросов) на `POST /api/v1/counter/unsafe/increment`. Включена по умолчанию.
2. **Safe Counter (AtomicInteger)** — тот же сценарий на `POST /api/v1/counter/safe/increment`. Выключена по умолчанию (отдельный прогон, чтобы не путать результаты).
3. **Employees List Load** — 50 потоков × 20 итераций на `GET /api/v1/employees?page=0&size=10`, общая нагрузка на кэшированный эндпоинт.

## Установка JMeter

1. Скачать с https://jmeter.apache.org/download_jmeter.cgi (Binaries → `apache-jmeter-X.X.zip`)
2. Распаковать, требуется Java 17+ (уже есть в проекте)
3. Запустить GUI: `bin\jmeter.bat` (Windows) из папки JMeter

## Запуск сценария 1 — демонстрация race condition

1. Перед запуском сбрось счётчики: `POST http://localhost:8080/api/v1/counter/reset` (например через Swagger UI)
2. Открой `employee-load-test.jmx` в JMeter GUI
3. Проверь, что включена только группа **"1 - Unsafe Counter (race condition)"**
4. Запусти (зелёная кнопка Start), дождись завершения
5. Открой **Summary Report** — увидишь количество отправленных запросов (должно быть 3000)
6. Проверь реальное значение счётчика: `GET http://localhost:8080/api/v1/counter` → поле `unsafeCount`
7. **Ожидаемый результат:** `unsafeCount` будет **меньше 3000** — часть инкрементов потеряна из-за гонки потоков на невайтомик `int++`

## Запуск сценария 2 — тот же тест на безопасном счётчике

1. `POST /api/v1/counter/reset`
2. В JMeter выключи группу 1 (правый клик → Disable) и включи группу **"2 - Safe Counter (AtomicInteger)"**
3. Запусти тест заново
4. Проверь `GET /api/v1/counter` → `safeCount`
5. **Ожидаемый результат:** `safeCount` равен ровно **3000** — `AtomicInteger` не теряет инкременты под той же нагрузкой

## Запуск сценария 3 — общая нагрузка

Группа **"3 - Employees List Load"** включена по умолчанию и может выполняться одновременно со сценариями 1/2. В Summary Report смотри `Average`/`90% Line`/`Throughput` — по ним можно оценить производительность кэшированного эндпоинта под нагрузкой.

## Альтернатива — headless-режим (без GUI)

```
bin\jmeter.bat -n -t employee-load-test.jmx -l results.jtl -e -o report
```
Отчёт в HTML появится в папке `report/`.
