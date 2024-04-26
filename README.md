<h1 align="center">Тестовое задание Junior Java разработчика Solva.kz</h1>

<div align="center">

<p align="center">
  <a href="https://alibekbirlikbai.github.io/projects" target="_blank"><b>Другие Проекты</b></a>
  ·
  <a href="https://alibekbirlikbai.github.io/resume">Мой CV</a>
  ·
  <a href="https://alibekbirlikbai.github.io/">Контакты</a>
  <br>
  <br>
</p>

<hr>

</div>


## Documentation
- [Intro](#intro) (краткое введение)
- [Features](#features) (пункты ТЗ / реализации)
- [Implementation](#implementation) (описание кода)
- [API](#api) (endpoints)
- [Quickstart](#quickstart) (настройка запуска)
- [Branch info](#branch-info)
- [Stack](#stack) (технологии)





## Intro
<p align="justify">
  Целью данного проекта является разработка прототипа микросервиса для анализа и обработки Транзакций Клиента в реальном времени в разных валютах (KZT, RUB и другие), с возможностью установления месячного Лимита на определенную сумму (USD), для интеграции в существующую банковскую систему
</p>

Всего проект состоит из 5 сервисов

Сервисы конфигурации:
1. **`service-registry`**: Обеспечивает механизм регистрации и обнаружения микросервисов
     - использует сервер **_Eureka_** (`@EnableEurekaServer` / для обнаружения сервиса `@EnableDiscoveryClient`)
     - **[docs:]** [Service Registry / Discovery Pattern](https://medium.com/design-microservices-architecture-with-patterns/service-registry-pattern-75f9c4e50d09)

2. **`config-server`**: Позволяет управлять конфигурациями микросервисов в одном месте
    - использует `spring-cloud-config-server` _(maven)_
    - **[docs:]** [Spring Cloud Config Server](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_spring_cloud_config_server)

3. **`api-gateway`**: Позволяет использовать общий порт (`port:8060`) для всех сервисов
    - использует `spring-cloud-starter-gateway` _(maven)_
    - **[docs:]** [API Gateway Pattern](https://microservices.io/patterns/apigateway.html#solution)

Сервисы с бизнес-логикой:

4. **`transaction-service`**: отвечает за прием и обработку _"Транзакций"_, и запросов от _"Клиента"_
5. **`currency-service`**: отвечает за получение актуальных _"курсов валют"_ (обращяется к `Внешнему API`) 


**`Внешний API`**:
> **Info:** Это API к которому обращяется проект для  получения актуальных курсов валют
- [https://openexchangerates.org/api](https://docs.openexchangerates.org/reference/api-introduction)






## Features
Пункты из ТЗ + Алгоритм выполнения для каждого пункта:
<div align="justify"> 
  
- [x] [п.1] <span id="task-1" align="justify">Получать информацию о каждой расходной операции в тенге (KZT), рублях (RUB) и других валютах в реальном времени и сохранять ее в своей собственной базе данных (БД);</span>
  - _**Сохранение получаемой "Транзакции"** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-1-implement-transaction) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/9b4c4576de5eb7ac72dbac92aad2e05892b01d53)_


- [x] [п.2] <span id="task-2" align="justify">Хранить месячный лимит по расходам в долларах США (USD) раздельно для двух категорий расходов: товаров и услуг. Если не установлен, принимать лимит равным 1000 USD;</span>

  - _**Реализовать логику разделение "Транзакций" по категориям расходов** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-2_implement_transaction) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/f8470e7d47fe0a39d1b0bf4e69fa06acb7358638)_
  - _**Реализовать логику установления "Лимита" по-умолчанию** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-3-implement-transaction) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/e4c22511347e7337829a02c7f2c0c49b353d831c)_


- [x] [п.3] <span id="task-3" align="justify">Запрашивать данные биржевых курсов валютных пар KZT/USD, RUB/USD по дневному интервалу (1day/daily) и хранить их в собственной базе данных. При расчете курсов использовать данные закрытия (close). В случае, если таковые недоступны на текущий день (выходной или праздничный день), то использовать данные последнего закрытия (previous_close);</span>

  - _**Установить соединение с `Внешним API`** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-1-implement-currency) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/88988e066e4af806268c11419eedc5c8c9a006db)_
  - _**Запрашивать "курсы валют" к USD** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-1-implement-currency) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/88dfb566f9f89e205c7d547e886ae2298fa72008)_
  - _**Обработать полученные валюты от `Внешнего API`** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-2-implement-currency) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/88dfb566f9f89e205c7d547e886ae2298fa72008)_  
  - _**Реализовать логику получения "курсов валют" последнего закрытия** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-3-implement-currency) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/8bfedec481d93c055f50bdd69364cbd2e44ef7b3)_
    > если **_"Транзакция"_** совершена в выходной или праздничный день
  - _**[--Доп. features--]:**_
      - [x] [п.3.1] <span id="task-3" align="justify">Рассчитывать сумму расходов в USD нужно по биржевому курсу на день расхода или по последнему курсу закрытия. За каждый запрос внешних данных нужно платить, и, к тому же, на выполнение внешнего запроса тратится дополнительное время. В связи с этим, полученные обменные курсы валют нужно хранить в своей базе данных и преимущественно использовать их;</span>
          > **Info:** Данные биржевых торгов получать из внешнего источника данных (<a href="https://twelvedata.com/docs#time-series">twelvedata.com</a>, <a href="https://www.alphavantage.co/">alphavantage.co</a>, <a href="https://docs.openexchangerates.org/reference/convert">openexchangerates.org</a> или из другого по своему усмотрению. 
      
        - _**Сохранение полученных "курсов валют" на определенную дату в `локальном БД`** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-2-implement-currency) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/ca6424b7a23903f9e826fd2701c2ce826c6b3bd0)_
        - _**Реализовать логику проверки наличия "курсов валют" на определенную дату в `локальном БД`** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-2-implement-currency) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/e10864162d65d8ea2ecf55d29310789b11a297ca)_
          > при следующей **_"Транзакция"_** на тот же день, данные будут взяты из БД, запрос не будет оправляться на внешний API если они уже есть в БД


- [x] [п.4] <span id="task-4" align="justify">Помечать транзакции, превысившие месячный лимит операций (технический флаг limit_exceeded);</span>

  - _**Реализовать логику выставления флага `limit_exceeded` для "Транзакции"** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task_4-implement-transaction) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/f1c1c58c5c77a26e882a2f3fa2c51eee867f7d7a)_
  - _**Реализовать логику конвертации валюты "Транзакций"** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-5-implement-transaction) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/28ab4d4fcca011ae6a2555f5223fb78a1a9ced69)_
    > это логика должна учитываться при вычислении `Остатка месячного лимита`
  - _**Реализовать обращение к `currency-service` для получения списка валют на дату совершения "Транзакции"** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-6-implement-transaction) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/ea04972657cd0f5cc3cbd6062a9f78369836790d)_

  - _**[--Доп. features--]:**_
      - [x] [п.4.1] <span id="task-3" align="justify">Последний лимит не должен влиять на выставление флага limit_exceeded транзакциям, совершенным ранее установления последнего лимита;</span>
        > **Info:** Иными словами, если лимит, установленный 1.01.2022 в размере 1000 USD, превышен двумя транзакциями на суммы 500 и 600 USD, то второй транзакции должен быть выставлен флаг limit_exceeded = true. Если пользователь установил новый лимит 11.01.2022, и выполнил третью транзакцию 12.01.2022 на сумму 100 USD, она должна иметь флаг limit_exceeded = false.
      
        - _**Реализовать логику определения соответствующего "Лимита" для "Транзакции"** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-4-implement-transaction) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/f1c1c58c5c77a26e882a2f3fa2c51eee867f7d7a)_


- [x] [п.5] <span id="task-5" align="justify">Дать возможность клиенту установить новый лимит. При установлении нового лимита микросервисом автоматически выставляется текущая дата, не позволяя выставить ее в прошедшем или будущем времени. Обновлять существующие лимиты запрещается;</span>

  - _**Реализовать функцию установления "Лимита" Клиентом** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-7-implement-transaction) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/a100578184ff5bcbd4295f449098f8c336b2f3bf)_


- [x] [п.6] <span id="task-6" align="justify">По запросу клиента возвращать список транзакций, превысивших лимит, с указанием лимита, который был превышен (дата установления, сумма лимита, валюта (USD));</span>

  - _**Реализовать функцию получения "Транзакции", которые перевесили свой "Лимит"** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-8-implement-transaction) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/f1c1c58c5c77a26e882a2f3fa2c51eee867f7d7a)_
  - _**[--Доп. features--]:**_
      - [x] [п.5.1] <span id="task-3" align="justify">При получении лимитов, в SQL запросе пользуйтесь JOIN с подзапросом, агрегирующими функциями и группировками;</span>
      
        - _**Написать `SQL запрос`** [(описание Кода)](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#task-8-implement-transaction) [(Git commit)](https://github.com/alibekbirlikbai/microservice-expenses/commit/4f6253523d33b6a226977a004dca1d61de6bad74)_
          > для получения **_"Транзакция"_**, которые перевесили свой **_"Лимит"_**


</div>





## Implementation

<details>
  <summary><b><code>transaction-service</code></b></summary>


  - <ins>Основные Классы</ins>:
    
      **model**:
      * _class **`Transaction`**_: объект для работы с _**"Транзакциями"**_
      * _class **`Limit`**_: объект для работы с _**"Лимитами"**_
      * _enum **`ExpenseCategory`**_: для определения доступных категорий расходов _**"Транзакциями"**_
        > **Note:** _**"Транзакциями"**_ не сохраняется если не соответствует хотя-бы одной из категорий
      * _class **`Currency`**_: объект для работы с _**"курсами валют"**_ из **`currency-service`**
        > имеет доп параметр _**CurrencyRequest**_, котоый содержит доп информацию для конкретной валюты (валютная пара - `"base":"USD" `, дата курса - `"formatted_timestamp": "2024-04-26"`)
      * _class **`ExceededTransactionDTO`**_: _**DTO**_ объект (содержит  _**"Транзакциями"**_ и Лимит) для работы с _**"Транзакциями"**_ превысевшими свой _**"Лимит"**_
      
      **service**:
      * _interface **`TransactionService`**_: бизнес логика для работы с - _class **`Transaction`**_ 
      * _interface **`LimitService`**_: бизнес логика для работы с _class **`Limit`**_ 
      * _interface **`CurrencyService`**_: бизнес логика для работы с - _class **`Currency`**_ (взаимодействие с **`currency-service`**)   
  
      **external**:
      * _class **`CurrencyServiceClient`**_: отвечает за взаимодействие **`currency-service`**

<br>
  
  
  - <details>
    <summary><ins>Реализация Features</ins></summary>
  
    -	<span id="task-1-implement-transaction"><b><i>1. Сохранение получаемой "Транзакций"</i></b></span>
  
          Для сохранения транзакции используется метод - _**save()**_
  
          ```java
          @Override
          public Transaction save(Transaction transaction) {
              TransactionServiceUtils.validateTransactionData(transaction);
  
              //...
              
              return repository.save(transaction);
          }
          ```

    -	<span id="task-2-implement-transaction"><b><i>2. Реализовать логику разделение "Транзакций" по категориям расходов</b></i></span>
  
          За определение категории расходов для _**"Транзакций"**_ отвечает - _enum **`ExpenseCategory`**_
      
          ```java
          public enum ExpenseCategory {
              PRODUCT,
              SERVICE
          }
          ```

    -	<span id="task-3-implement-transaction"><b><i>3. Реализовать логику установления "Лимита" по умолчанию</i></b></span>
  
          За определение Лимита по-умолчанию отвечает метод - _**setDefaultLimit()**_
     	> **Note:** дата установления = 1st число месяца, в котором была совершена _**"Транзакций"**_
  
          ```java
          @Override
          public Limit setDefaultLimit(Transaction transaction) {
              Limit limit = new Limit();
              limit.setId(0);
              limit.setLimit_currency_shortname("USD");
              limit.setLimit_sum(BigDecimal.valueOf(1000.00));
              limit.setLimit_datetime(ServiceUtils.getStartOfMonthDateTime(transaction.getDatetime()));
              limit.setExpense_category(transaction.getExpense_category());
              return limit;
          }
          ```



    -	<span id="task-4-implement-transaction"><b><i>4. Реализовать логику выставления флага <code>limit_exceeded</code> для "Транзакции"</b></i></span>
  
          Для выставление флага `limit_exceeded` для _**"Транзакций"**_ отвечает метод - _**checkTransactionForExceed()**_
      
          ```java
          @Override
          public Transaction save(Transaction transaction) {
              //...

              transaction.setLimit_exceeded(checkTransactionForExceed(transaction, limit));
              return repository.save(transaction);
          }
          ```
          ```java
          @Override
          public boolean checkTransactionForExceed(Transaction transaction, Limit limit) {
              /* Чтобы не сохранять конвертированный (в USD) вариант как сумму для текущей транзакции,
               * при расчете limitSumLeft передаем в нее копию  */
              Transaction transactionCopy = transaction.clone();
      
              BigDecimal limitSumLeft = limitService.calculateLimitSumLeft(transactionCopy, limit);
      
              // Если limitSumLeft отрицательный или 0.0, то лимит был превышен
              if (limitSumLeft.compareTo(BigDecimal.ZERO) < 0) {
                  return true;
              }
              return false;
          }
          ```
          ```java
          @Override
          public BigDecimal calculateLimitSumLeft(Transaction transaction, Limit limit) {
              ZonedDateTime limitStartDate = limit.getLimit_datetime();
              ZonedDateTime transactionDateTime = transaction.getDatetime();
              ExpenseCategory transactionCategory = transaction.getExpense_category();
      
              // Обновляем лимит на начало месяца транзакции, если она произошла после установления лимита
              if (transactionDateTime.isAfter(limitStartDate)) {
                  limitStartDate = ZonedDateTime.of(transactionDateTime.getYear(), transactionDateTime.getMonthValue(), 1, 0, 0, 0, 0, transactionDateTime.getZone());
              }
      
              // Получаем все транзакции клиента за месяц
              List<Transaction> transactions = transactionService.getClientTransactionListForMonth(transactionDateTime);
              transactions.add(transaction);
      
              // Фильтруем транзакции, которые произошли после установления лимита в текущем месяце
              ZonedDateTime finalLimitStartDate = limitStartDate;
              List<Transaction> relevantTransactions = transactions.stream()
                      .filter(t -> t.getDatetime().isAfter(finalLimitStartDate))
                      .filter(t -> t.getExpense_category() == transactionCategory) // Учитываем категорию расходов
                      .map(t -> {
                          Transaction transactionCopy = t.clone();
      
                          // Конвертируем сумму транзакции в USD
                          transactionCopy.setSum(currencyService.convertToUSD(t.getCurrency_shortname(), t.getSum(), t.getDatetime())); // Конвертируем сумму транзакции в USD
                          return transactionCopy;
                      })
                      .collect(Collectors.toList());
      
              // Вычисляем остаток лимита
              BigDecimal remainingLimit = limit.getLimit_sum();
              for (Transaction t : relevantTransactions) {
                  remainingLimit = remainingLimit.subtract(t.getSum());
              }
              System.out.println("remainingLimit: " + remainingLimit);
              return remainingLimit;
          }
          ```


    -	<span id="task-5-implement-transaction"><b><i>5. Реализовать логику конвертации валюты "Транзакции"</i></b></span>
  
          Для конвертации параметра _**"Транзакций"**_ _`sum`_ к _'USD'_ используется метод - _**convertToUSD()**_
  
          ```java
          @Override
          public BigDecimal convertToUSD(String currency_shortname, BigDecimal transaction_sum, ZonedDateTime transaction_dateTime) {
              // Получаем список валют и их курсов (из API currency-service)
              List<Currency> currencyList = fetchCurrencyList(transaction_dateTime).block();
              Map<String, BigDecimal> currencyMap = getListOfCurrency(currencyList);
      
              // Проверяем, есть ли указанная валюта в списке
              if (!currencyMap.containsKey(currency_shortname)) {
                  return BigDecimal.ZERO;
              }
      
              // Получаем курс валюты к USD
              BigDecimal exchangeRate = currencyMap.get(currency_shortname);
      
              // Конвертируем сумму в USD по курсу
              return transaction_sum.divide(exchangeRate, 2, RoundingMode.HALF_UP);
          }
          ```
          Где метод _`fetchCurrencyList()`_ обращяется к **`currency-service`** для получения списка валют на дату совершения _**"Транзакций"**_, и метод _`getListOfCurrency()`_ обрабатывает этот список и возвращяет его в формате _`Map<"Валюта","Курс к USD">`_
          ```java
          @Override
          public Map<String, BigDecimal> getListOfCurrency(List<Currency> currencyList) {
              Map<String, BigDecimal> currencyRatesMap = new HashMap<>();
              // Добавляем все валюты и их курсы к USD из списка currencyList
              for (Currency currency : currencyList) {
                  currencyRatesMap.put(currency.getCurrency_shortname(), currency.getRate_to_USD());
              }
      
              return currencyRatesMap;
          }
          ```


    -	<span id="task-6-implement-transaction"><b><i>6. Реализовать обращение к **`currency-service`** для получения списка валют на дату совершения "Транзакции"</b></i></span>
  
          За обращение к **`currency-service`** отвечает - _class **`CurrencyServiceClient`**_, который использует _**WebClient**_ для выполнения `HTTP-запросов` к сервису
      
          ```java
          @Component
          public class CurrencyServiceClient {
              private final WebClient webClient;
          
              @Autowired
              public CurrencyServiceClient(WebClient.Builder webClientBuilder) {
                  this.webClient = webClientBuilder
                          .filter((request, next) -> {
                              System.out.println("Request: " + request.method() + " " + request.url());
                              return next.exchange(request);
                          })
                          .build();
              }
          
              // http://localhost:8082/currency-service/api/currencies/{dateTime}
              public Mono<List<Currency>> getCurrencyList(ZonedDateTime dateTime) {
                  return webClient.get()
                          .uri(uriBuilder ->
                                  uriBuilder.scheme("http")
                                          .host("localhost")
                                          .port(8082)
                                          .path("/currency-service/api/currencies/" + dateTime)
                                          .build())
                          .retrieve()
                          .bodyToFlux(Currency.class)  // Преобразуем ответ в поток объектов Currency
                          .collectList();              // Собираем объекты Currency в список
              }
          }
          ```
          Для получения и обработки списка валют отвечает метод - _**fetchCurrencyList()**_
          ```java
          @Override
          public Mono<List<Currency>> fetchCurrencyList(ZonedDateTime transaction_dateTime) {
              List<Currency> currencyList = new ArrayList<>();
              return currencyServiceClient.getCurrencyList(transaction_dateTime)
                      .doOnNext(response -> {
                          // Преобразование и добавление в currencyList
                          List<Currency> returnedCurrencies = response.stream()
                                  .map(currency -> {
                                      Currency newCurrency = new Currency();
                                      newCurrency.setCurrency_shortname(currency.getCurrency_shortname());
                                      newCurrency.setRate_to_USD(currency.getRate_to_USD());
      
                                      CurrencyRequest newCurrencyRequest = new CurrencyRequest();
                                      newCurrencyRequest.setBase(currency.getCurrencyRequest().getBase());
                                      newCurrencyRequest.setFormatted_timestamp(currency.getCurrencyRequest().getFormatted_timestamp());
      
                                      newCurrency.setCurrencyRequest(newCurrencyRequest);
      
                                      return newCurrency;
                                  })
                                  .collect(Collectors.toList());
                          currencyList.addAll(returnedCurrencies);
                      });
          }
          ```

    -	<span id="task-7-implement-transaction"><b><i>7. Реализовать функцию установления "Лимита" Клиентом</b></i></span>
  
          За сохранение _**"Лимита"**_ Клиента отвечает метод - _**setClientLimit()**_
      
          ```java
          @Override
          public Limit setClientLimit(Limit limit) {
                // Обновлять существующие лимиты запрещается
                checkLimitForExist(limit);
        
                /* автоматически выставляется текущая дата,
                * не позволяя выставить ее в прошедшем или будущем времени */
                limit.setLimit_datetime(ServiceUtils.getCurrentDateTime());
        
                // Лимит всегда USD
                limit.setLimit_currency_shortname("USD");
                return repository.save(limit);
            }
          ```

          После того как Клиент определит собственный _**"Лимит"**_, перед выставлением флага `limit_exceeded` определяется актуальный _**"Лимит"**_ для _**"Транзакций"**_. Сначала проверяется есть ли в БД хотябы 1 _**"Лимит"**_ удовлетворяющий параметру `expense_category` _**"Транзакций"**_, если есть то он используется при вычсилении _`"Остатка месячного Лимита"`_ для _**"Транзакций"**_, если его нет используется _**"Лимит"** по-умолчанию_ (1000.00 USD)
  
          ```java
          @Override
          public Transaction save(Transaction transaction) {
              //...

              Limit limit = new Limit();
              if (limitService.hasRecords()) {
                  Map<ExpenseCategory, Limit> latestLimits = limitService.getLatestLimitsForCategories();
      
                  // Проверяем, соответствует ли ExpenseCategory транзакции одной из категорий в возвращаемой Map
                  Limit limitForTransactionCategory = latestLimits.get(transaction.getExpense_category());
      
                  if (limitForTransactionCategory != null) {
                      /// Лимит Клиента
                      BeanUtils.copyProperties(limitForTransactionCategory, limit);
                      ServiceUtils.roundToHundredth(limit.getLimit_sum());
                  } else {
                      // Лимит по умолчанию (1000.00)
                      /* на тот случай если Клиент установил лимит
                       * только для 1 из категорий ExpenseCategory */
                      limit = limitService.setDefaultLimit(transaction);
                  }
                } else {
                    // Лимит по умолчанию (1000.00)
                    /* на тот случай если Клиент никогда не устанавливал своего лимита
                     * т.е. для всех ExpenseCategory в месяце в котором была совершена транзакция
                     * лимит = 1000.00 */
                    limit = limitService.setDefaultLimit(transaction);
                }

                //...
                  
                return repository.save(transaction);
          }
          ```


    -	<span id="task-8-implement-transaction"><b><i>8. Реализовать функцию получения "Транзакции", которые превысили свой "Лимит"</b></i></span>
       	
          - Реализация через SQL-запрос
             ```sql
             SELECT t.id AS t_id,
                  t.account_from,
                  t.account_to,
                  t.currency_shortname,
                  t.sum,
                  t.expense_category,
                  t.datetime,
                  t.limit_exceeded,
                  CASE
                      WHEN COALESCE(l.id, -1) = -1 THEN 0
                      ELSE l.id
                  END AS limit_id,
                  CASE
                      WHEN COALESCE(l.id, 0) = 0 THEN 1000.00
                      ELSE l.limit_sum
                  END AS limit_sum,
                  CASE
                      WHEN COALESCE(l.id, 0) = 0 THEN DATE_TRUNC('month', t.datetime) + INTERVAL '1 DAY'
                      ELSE l.limit_datetime
                  END AS limit_datetime,
                  CASE
                      WHEN COALESCE(l.id, 0) = 0 THEN 'USD'
                      ELSE l.limit_currency_shortname
                  END AS limit_currency_shortname,
                  CASE
                      WHEN COALESCE(l.id, 0) = 0 THEN t.expense_category
                      ELSE COALESCE(l.expense_category, t.expense_category)
                  END AS limit_expense_category
              FROM Transaction t
              LEFT JOIN (
                  SELECT t.id, MAX(l.limit_datetime) AS max_limit_datetime
                  FROM Transaction t
                  JOIN Limits l ON t.expense_category = l.expense_category AND t.datetime >= l.limit_datetime -- Фильтрация транзакции по (категории) + (времени)
                  WHERE t.limit_exceeded = true
                  GROUP BY t.id
              ) AS t_max_limit ON t.id = t_max_limit.id AND t.limit_exceeded = true
              LEFT JOIN Limits l ON t.expense_category = l.expense_category
                          AND l.limit_datetime = t_max_limit.max_limit_datetime
              WHERE t.limit_exceeded = true -- Добавляем фильтрацию по limit_exceeded
              ORDER BY t.datetime DESC; -- Сортировка ответа, чтобы сначала были самые актуальные транзакции
             ```
             ```java
             @Override
             public List<ExceededTransactionDTO> getAllExceededTransactions_SQL() {
                  String sqlQuery = "
                                    //... SQL-Запрос
                                     ";
          
                  Query query = entityManager.createNativeQuery(sqlQuery);
                  List<Object[]> resultList = query.getResultList();
          
                  List<ExceededTransactionDTO> exceededTransactionDTOs = new ArrayList<>();
                  for (Object[] result : resultList) {
                      // Проверяем значение limit_exceeded
                      if ((Boolean) result[7]) {
                          Map<String, Object> transactionMap = new LinkedHashMap<>();
                          transactionMap.put("id", result[0]);
                          transactionMap.put("account_from", result[1]);
                          transactionMap.put("account_to", result[2]);
                          transactionMap.put("currency_shortname", result[3]);
                          transactionMap.put("sum", result[4]);
                          transactionMap.put("expense_category", result[5]);
                          transactionMap.put("datetime", result[6]);
                          transactionMap.put("limit_exceeded", result[7]);
          
                          ExceededTransactionDTO dto = new ExceededTransactionDTO();
                          Map<String, Object> limitMap = new LinkedHashMap<>();
          
                          if ((Long) result[8] != 0) {
                              // Если есть Client limits
                              limitMap.put("id", result[8]);
                              limitMap.put("limit_sum", result[9]);
                              limitMap.put("limit_datetime", result[10]);
                              limitMap.put("limit_currency_shortname", result[11]);
                              limitMap.put("expense_category", result[12]);
          
                              dto.setLimit(LimitServiceUtils.convertMapToLimit(limitMap));
                          } else {
                              // Если нет Client limits
                              Limit defaultLimit = limitService.setDefaultLimit(TransactionServiceUtils.convertMapToTransaction(transactionMap));
          
                              limitMap.put("id", defaultLimit.getId());
                              limitMap.put("limit_sum", defaultLimit.getLimit_sum());
                              limitMap.put("limit_datetime", defaultLimit.getLimit_datetime());
                              limitMap.put("limit_currency_shortname", defaultLimit.getLimit_currency_shortname());
                              limitMap.put("expense_category", defaultLimit.getExpense_category());
                              dto.setLimit(defaultLimit);
                          }
          
                          dto.setTransaction(TransactionServiceUtils.convertMapToTransaction(transactionMap));
          
                          exceededTransactionDTOs.add(dto);
                      }
                  }
          
                  return exceededTransactionDTOs;
              }
              ```
            
          - Реализация через Java-код
             ```java
             @Override
             public List<ExceededTransactionDTO> getAllExceededTransactions_Java() {
                  // Получаем все транзакции, которые превысили лимиты
                  List<Transaction> exceededTransactions = repository.findByLimit_exceededTrue();
          
                  // Создаем список DTO для превышенных транзакций
                  List<ExceededTransactionDTO> exceededTransactionDTOs = new ArrayList<>();
          
                  // Проходим по каждой превышенной транзакции
                  for (Transaction transaction : exceededTransactions) {
                      // Получаем лимит для данной транзакции
                      Limit limit = limitService.getLimitForTransaction(transaction);
          
                      // Создаем DTO и устанавливаем транзакцию и соответствующий лимит
                      ExceededTransactionDTO dto = new ExceededTransactionDTO();
                      dto.setTransaction(transaction);
                      dto.setLimit(limit);
          
                      // Добавляем DTO в список
                      exceededTransactionDTOs.add(dto);
                  }
                  Collections.reverse(exceededTransactionDTOs);
                  return exceededTransactionDTOs;
              }
              ```  

  </details>

</details>



<details>
  <summary><b><code>currency-service</code></b></summary>


  - <ins>Основные Классы</ins>:
    
      **model**:
      * _class **`Currency`**_: объект для работы с _**"курсами валют"**_
      * _class **`CurrencyApiResponse`**_: объект для работы _`response`_ от `Внешнего API`
      * _class **`CurrencyRequest`**_: объект для сохранения информации о прошлых запросах к `Внешнему API`
      
      **service**:
      * _class **`CurrencyService`**_: бизнес логика для работы с - _class **`Currency`**_, _class **`CurrencyApiResponse`**_, _class **`CurrencyRequest`**_  
  
      **external**:
      * _class **`OpenExchangeRatesClient`**_: отвечает за взаимодействие c `Внешним API`

  <br>

  
  - <details>
    <summary><ins>Реализация Features</ins></summary>
  
    -	<span id="task-1-implement-currency"><b><i>1. Установить соединение с <code>Внешним API</code></i></b></span>
  
          За обеспечение соединения с `Внешним API` отвечает - _class **`OpenExchangeRatesClient`**_, в котором `app_id` и `basePath` определен в `config-server\src\main\resources\config\currency-service.yaml`
  
          ```java
          @Component
          public class OpenExchangeRatesClient {
              private final WebClient webClient;
          
              @Value("${my.API_id}")
              private String app_id;
          
              @Value("${my.API_basePath}")
              private String basePath;
          
              @Autowired
              public OpenExchangeRatesClient(WebClient.Builder webClientBuilder) {
                  this.webClient = webClientBuilder
                          .filter((request, next) -> {
                              System.out.println("Request: " + request.method() + " " + request.url());
                              return next.exchange(request);
                          })
                          .build();
              }

              // ...
          }
          ```
          В этом классе метод - _**getCurrencyList_Latest()**_ отвечает за получение списка "курсов валют" на текущий момент (up-to-date)
     	> **Note:** метод обращяется к _API_ `/latest.json` от [https://openexchangerates.org/api](https://docs.openexchangerates.org/reference/latest-json) 
          ```java
          // ...
          public Mono<CurrencyApiResponse> getCurrencyList_Latest() {
              return webClient.get()
                      .uri(uriBuilder ->
                              uriBuilder.scheme("https")
                                      .host(basePath)
                                      .path("/api/latest.json")
                                      .queryParam("app_id", app_id)
                                      .build())
                      .retrieve()
                      .bodyToMono(CurrencyApiResponse.class);
          }
          // ...
          ```
           и метод - _**getCurrencyList_Historical()**_ отвечает за получение списка "курсов валют" на определенную дату в прошлом
     	> **Note:** метод обращяется к _API_ `/historical/*.json` от [https://openexchangerates.org/api](https://docs.openexchangerates.org/reference/historical-json) 
          ```java
          // ...
          public Mono<CurrencyApiResponse> getCurrencyList_Historical(String dateTime) {
              return webClient.get()
                      .uri(uriBuilder ->
                              uriBuilder.scheme("https")
                                      .host(basePath)
                                      .path("/api/historical/" + dateTime + ".json")
                                      .queryParam("app_id", app_id)
                                      .build())
                      .retrieve()
                      .bodyToMono(CurrencyApiResponse.class);
          }
          // ...
          ```



    -	<span id="task-2-implement-currency"><b><i>2. Обработка полученных валют от <code>Внешнего API</code></i></b></span>
  
          За получение и обработку валют от `Внешнего API` отвечает метод - _**getCurrencyList()**_
     	> **Note:** в этом методе применяются принципы реактивного программирования от _**WebClient**_
  
          ```java
          @Override
          public Mono<List<Currency>> getCurrencyList(ZonedDateTime transaction_dateTime) {
              String currentDate_formatted = CurrencyServiceUtils.parseZoneDateTime(CurrencyServiceUtils.getCurrentDateTime());
              String transactionDate_formatted = CurrencyServiceUtils.parseZoneDateTime(transaction_dateTime);
              LocalDate parsedCurrentDate = LocalDate.parse(currentDate_formatted);
              LocalDate parsedTransactionDate = LocalDate.parse(transactionDate_formatted);
      
              CurrencyRequest pastCurrencyRequest = requestRepository.findByFormatted_timestamp(transactionDate_formatted);
              if (pastCurrencyRequest != null) {
                  System.out.println("Get data from local db!!! (currencyList at:" + pastCurrencyRequest.getFormatted_timestamp() + ")");
      
                  // Список валют взят из БД (запрос в openexchangerates.org/api/ НЕ делается)
                  currencyList = currencyRepository.findAllByCurrencyRequestID(pastCurrencyRequest.getId());
                  return checkForUnavailableRate(transaction_dateTime, currencyList);
              } else {
                  return Mono.defer(() -> {
                      if (parsedTransactionDate.isEqual(parsedCurrentDate)) {
                          return openExchangeRatesClient.getCurrencyList_Latest()
                                  .map(response -> {
                                      response.setTimestamp(currentDate_formatted);
                                      return response;
                                  })
                                  .flatMap(response -> createCurrenciesFromResponse(Mono.just(response)))
                                  .flatMap(currencyList -> checkForUnavailableRate(transaction_dateTime, currencyList));
                      } else if (parsedTransactionDate.isBefore(parsedCurrentDate)) {
                          return openExchangeRatesClient.getCurrencyList_Historical(transactionDate_formatted)
                                  .map(response -> {
                                      response.setTimestamp(transactionDate_formatted);
                                      return response;
                                  })
                                  .flatMap(response -> createCurrenciesFromResponse(Mono.just(response)))
                                  .flatMap(currencyList -> checkForUnavailableRate(transaction_dateTime, currencyList));
                      } else {
                          return Mono.error(new IllegalArgumentException("Transaction date cannot be in the future!!! (BACK-TO-THE-FUTURE)"));
                      }
                  });
              }
          }
          ```
          ```java
          @Override
          public Mono<List<Currency>> createCurrenciesFromResponse(Mono<CurrencyApiResponse> responseMono) {
              return responseMono.flatMap(response -> {
                  CurrencyRequest currencyRequest = getCurrencyRequest(response);
                  requestRepository.save(currencyRequest);
      
                  // Получаем список валют и сохраняем их в бд
                  Map<String, BigDecimal> currencyRates = response.getRates();
                  currencyList = new ArrayList<>();
                  for (Map.Entry<String, BigDecimal> entry : currencyRates.entrySet()) {
                      Currency currency = new Currency();
                      currency.setCurrency_shortname(entry.getKey());
                      currency.setRate_to_USD(entry.getValue());
                      currencyList.add(currency);
                  }
                  currencyList.forEach(currency -> currency.setCurrencyRequest(currencyRequest));
                  currencyRepository.saveAll(currencyList);
                  return Mono.just(currencyList);
              });
          }
          ```
          ```java
          @Override
          public CurrencyRequest getCurrencyRequest(CurrencyApiResponse response) {
              CurrencyRequest currencyRequest = new CurrencyRequest();
              currencyRequest.setBase(response.getBase());
              currencyRequest.setFormatted_timestamp(response.getTimestamp());
              return currencyRequest;
          }
          ```


    -	<span id="task-3-implement-currency"><b><i>3. Реализовать логику получения "курсов валют" последнего закрытия</b></i></span>
  
          За определение категории расходов для _**"Транзакций"**_ отвечает - _enum **`ExpenseCategory`**_
      
          ```java
          /* это немного условная реализация требования из ТЗ (пункт 3) ("использовать данные последнего закрытия (previous_close)")
           * потому что у меня free-plan от https://openexchangerates.org/api/,
           * но в документации говорится что для '/historical/*.json' + '/latest.json'
           * в качестве rates фактический берутся данные последнего закрытия,
           * а значит что если запрос будет пустой,
           * то мы можем сделать еще один запрос на предыдущий день (чтобы получить previous_close rates)
           *      '/latest.json'=(The latest rates will always be the most up-to-date data available on your plan)
           *      '/historical/*.json'=(The historical rates returned are the last values we published for a given UTC day)  */
          @Override
          public Mono<List<Currency>> checkForUnavailableRate(ZonedDateTime transaction_dateTime, List<Currency> currencyList) {
              /* Если currencyList пустой, вызываем getCurrencyList на предыдущий день,
               * и так по рекурсии, пока не найдется доступный курс */
              if (currencyList.isEmpty()) {
                  ZonedDateTime previousDate = transaction_dateTime.minusDays(1);
                  return getCurrencyList(previousDate);
              } else {
                  // Если currencyList не пустой, просто возвращаем значение
                  return Mono.just(currencyList);
              }
          }
          ```

</details>





## API
> [!NOTE]
> Если **`api-gateway`**:
>   - [x] запущен:
>     - _**port**: `8060`_ (Общий для всех сервисов приложения)
>   - [ ] не запущен:
>     - _**port**: `8081`_ (для **`transaction-service`**)
>     - _**port**: `8082`_ (для **`currency-service`**)

<details>
  <summary>API-запросы для - <b><code>transaction-service</code></b></summary>
  
  1. <details>
    
      <summary><b>POST</b> -> <a href="#task-1">Для приема <i>"Транзакций"</i></a> (условно интеграция с банковскими сервисами)</summary>

      **_[Request]:_** 

       ```
        http://localhost:8060/transaction-service/api/transactions
        ```
          
        ```yaml
        {
            "account_from": "0000000123",
            "account_to": "9999999999",
            "currency_shortname": "USD",
            "sum": "500",
            "expense_category": "SERVICE"
        }
        ```
        
        > `"currency_shortname":` _`KZT`, `RUB`, `USD`, ..._  
        > `"expense_category":` _`SERVICE` / `PRODUCT`_
      

        **_[Response]:_**

        ```yaml
        Successfully saved
        ```

     </details>


  2. <details>
    
      <summary><b>POST</b> -> <a href="#task-5">Для установки нового <i>"Лимита"</i> Клиентами</a></summary>

      **_[Request]:_**

       ```
        http://localhost:8060/transaction-service/api/client/limits
        ```
          
        ```yaml
        {
            "limit_sum": "2000",
            "limit_currency_shortname": "USD",
            "expense_category": "SERVICE"
        }
        ```
        
        > `"expense_category":` _`SERVICE` / `PRODUCT`_
      

        **_[Response]:_**

        ```yaml
        New limit Successfully saved
        ```

     </details>



  3. <details>
    
      <summary><b>GET</b> -> <a href="#task-5">Для получения всех <i>"Лимитов"</i> Клиентами</a></summary>

      **_[Request]:_**

       ```
        http://localhost:8060/transaction-service/api/client/limits
        ```

        **_[Response]:_**

        ```yaml
        [
          {
              "id": 1,
              "limit_sum": 2000.0,
              "limit_datetime": "2024-04-01T00:00:00Z",
              "limit_currency_shortname": "USD",
              "expense_category": "SERVICE"
          },
          ... // Получаем все установленные Лимиты Клиента
        ]
        ```

     </details>




  4. <details>
    
      <summary><b>GET</b> -> [<b>SQL query</b>] <a href="#task-6">Для получение <i>"Транзакций"</i> превысивших <i>"Лимит"</i></a> 
        (с указанием лимита, который был превышен)
      </summary> 
      
      <span> **_[Request]:_** </span>

       ```
       http://localhost:8060/transaction-service/api/client/transactions/exceeded/sql-query
        ```

        **_[Response]:_**
     
        - ~ [DEFAULT Limit] (1000.00 USD):
            ```yaml
            [
              {
                "transaction": {
                    "id": 2,
                    "account_from": 123,
                    "account_to": 9999999999,
                    "currency_shortname": "USD",
                    "sum": 600.00,
                    "expense_category": "SERVICE",
                    "datetime": "2024-04-25T11:23:50.236144Z",
                    "limit_exceeded": true
                },
                "limit": {
                    "id": 0, // Лимит по-умолчанию
                    "limit_sum": 1000.0,
                    "limit_datetime": "2024-04-01T00:00:00Z",
                    "limit_currency_shortname": "USD",
                    "expense_category": "SERVICE"
                }
              },
              ... // Получаем все Транзакции превысившие свой Лимит
            ]
            ```
          
        - Client Limit:
            ```yaml
            [
              {
                "transaction": {
                    "id": 6,
                    "account_from": 123,
                    "account_to": 9999999999,
                    "currency_shortname": "USD",
                    "sum": 100.00,
                    "expense_category": "SERVICE",
                    "datetime": "2024-04-25T11:23:50.236144Z",
                    "limit_exceeded": true
                },
                "limit": {
                    "id": 1, // Клиентский Лимит
                    "limit_sum": 2000.0,
                    "limit_datetime": "2024-04-01T00:00:00Z",
                    "limit_currency_shortname": "USD",
                    "expense_category": "SERVICE"
                }
              },
                ... // Получаем все Транзакции превысившие свой Лимит
            ]
            ```

     </details>



  5. <details>
    
      <summary><b>GET</b> -> [<b>Java code</b>] <a href="#task-6">Для получение <i>"Транзакций"</i> превысивших <i>"Лимит"</i></a> 
        (с указанием лимита, который был превышен)
      </summary> 
      
      **_[Request]:_**

       ```
        http://localhost:8060/transaction-service/api/client/transactions/exceeded/java-code
        ```

        **_[Response]:_**

        > Структура ответа такая же как для `/exceeded/sql-query`, но Java реализация логики

     </details>


</details>






<details>
  <summary>API-запросы для - <b><code>currency-service</code></b></summary>
  
  1. <details>
    
      <summary><b>GET</b> -> <a href="#task-3">Для получения <i>"Курсов-валют"</i> на определенную дату</a></summary>

      **_[Request]:_**

       ```
        http://localhost:8060/currency-service/api/currencies/2022-05-20T01:00:00+03:00
        ```
        
        > **Note:** Контроллер на стороне **`currency-service`**, принимает параметр типа **_ZoneDateTime_** - _`"2022-05-20T01:00:00+03:00"`_ от **`transaction-service`**,  после чего дата будет приведена в формат _`"yyyy-mm-dd"`_ для соответствия API-запросам к [https://openexchangerates.org/api/](https://docs.openexchangerates.org/reference/api-introduction)
      

        **_[Response]:_**

        ```yaml
        [
          {
              "id": 1,
              "currency_shortname": "AED",
              "rate_to_USD": 3.67,
              "currencyRequest": {
                  "id": 102,
                  "base": "USD",
                  "formatted_timestamp": "2022-05-20"
              }
          },
          {
              "id": 2,
              "currency_shortname": "AFN",
              "rate_to_USD": 90.50,
              "currencyRequest": {
                  "id": 102,
                  "base": "USD",
                  "formatted_timestamp": "2022-05-20"
              }
          },
          ... // Получаем все курсы валют на - "2022-05-20T01:00:00+03:00"
        ]
        ```

     </details>


  2. <details>
    
      <summary><b>GET</b> -> <a href="#task-3">Для получения списка запросов</a> (совершенных к внешнему API)</summary>

      **_[Request]:_**

       ```
        http://localhost:8060/currency-service/api/currencies/requests
        ```  

        **_[Response]:_**

        ```yaml
        [
            {
                "id": 1,
                "base": "USD",
                "formatted_timestamp": "2022-05-20"
            },
            ... // Получаем все - предыдущие запросы к 'https://openexchangerates.org/api/'
        ]
        ```    

     </details>



  3. <details>
    
      <summary><b>GET</b> -> <a href="#task-3">Для получения всех <i>"Курсов-валют"</i> для ранее совершенных запросов</a></summary>

      **_[Request]:_**

        ```
        http://localhost:8060/currency-service/api/currencies
        ```

        **_[Response]:_**

        ```yaml
        [
          {
              "id": 1,
              "currency_shortname": "AED",
              "rate_to_USD": 3.67,
              "currencyRequest": {
                  "id": 102,
                  "base": "USD",
                  "formatted_timestamp": "2022-05-20"
              }
          },
          {
              "id": 2,
              "currency_shortname": "AFN",
              "rate_to_USD": 90.50,
              "currencyRequest": {
                  "id": 102,
                  "base": "USD",
                  "formatted_timestamp": "2022-05-20"
              }
          },
          ... // Получаем все курсы валют хранящиеся на локальном БД (предыдущие запросы к 'https://openexchangerates.org/api/')
        ]
        ```

     </details>


</details>






## Quickstart
```bash
https://github.com/alibekbirlikbai/microservice-expenses.git
```

- <details>
    <summary id="">Настройка <b><i>Базы Данных</i></b></summary>

    - В _PostgreSQL_ создайте 2 отдельные _базы данных_: `currency_data` и `transaction_data`
    - Для подключения к своему _PostgreSQL_ пройдите до `config-server\src\main\resources\config` внутри заходите в `currency-service.yaml`/`transaction-service.yaml` и заменяете значения для `username`, `password`

      ![image](https://github.com/alibekbirlikbai/microservice-expenses/assets/87764579/96964224-98be-4bf7-aaf1-8307ceb00f61)
      ![image](https://github.com/alibekbirlikbai/microservice-expenses/assets/87764579/35c7fd79-dc6f-47f1-a706-19ac236383d6)

    - <details>
          <summary><b><i>(Опционально)</i></b></summary>

      Для **`currency-service`**, параметр `hibernate.ddl-auto: update`
      >при желании его можно заменить на другой, но тогда после каждого запуска микросервиса хранящиеся данные о курсах валют в бд будут потеряны (придется занова обращаться к '[`Внешнему API`](https://docs.openexchangerates.org/reference/api-introduction)')

      ![image](https://github.com/alibekbirlikbai/microservice-expenses/assets/87764579/2b5fcd73-04e2-42e3-8572-bd55872b930c)

      </details>

  </details>


- <details>
    <summary>Настройка <b><i>Сервера</i></b></summary>

    - Как только настроите бд, запустите по порядку каждый из сервисов:
        - **`service-registry`** (1)
        - **`config-server`** (2)
        - **`api-gateway`** (3)
        - **`currency-service`** (4)
        - **`transaction-service`** (5)

        После запуска проект должен выглядеть так:  
        ![Без имени](https://github.com/alibekbirlikbai/microservice-expenses/assets/87764579/2be73aeb-a802-4aee-bb7a-38c673d6445a)


    - После чего можете запускать **_API-запросы_** (детали смотрите в разделе [API](#api)

  </details>







## Branch info
- _**main**_ - _Production_ ветка, стабильная (завершенная) версия проекта;
- _**dev**_ - _Development_ ветка, интеграция всех [_features_](https://github.com/alibekbirlikbai/microservice-expenses?tab=readme-ov-file#features);






## Stack
- [Java](https://www.java.com/ru/)
- [Spring Framework](https://spring.io/)
- [PostgreSQL](https://www.postgresql.org/)

