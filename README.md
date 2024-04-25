<h1 align="center">Тестовое задание Junior Java разработчика Solva.kz</h1>

<div align="center">

<p align="center">
  <a href="https://alibekbirlikbai.github.io/projects" target="_blank">Другие Проекты</a>
  ·
  <a href="https://alibekbirlikbai.github.io/resume">Мой CV</a>
  ·
  <a href="https://alibekbirlikbai.github.io/">Контакты</a>
  <br>
  <br>
</p>

<hr>

</div>



Проект состоит из 2 основных сервисов:
- **_transaction service_** - 
- **_currency service_** - 






## Features

Пункты из ТЗ:

- [x] <details>
        <summary>
            1.	Получать информацию о каждой расходной операции в тенге (KZT), рублях (RUB) и других валютах в реальном времени и сохранять ее в своей собственной базе данных (БД);
        </summary>

          - [x] 1. ddd
             - [x] 1.1. ddd

    </details>


- [x] <details>
        <summary>
            2.	Хранить месячный лимит по расходам в долларах США (USD) раздельно для двух категорий расходов: товаров и услуг. Если не установлен, принимать лимит равным 1000 USD;
        </summary>

          - [x] 1. ddd
             - [x] 1.1. ddd

    </details>



- [x] <details>
        <summary>
            3.	Запрашивать данные биржевых курсов валютных пар KZT/USD, RUB/USD по дневному интервалу (1day/daily) и хранить их в собственной базе данных. При расчете курсов использовать данные закрытия (close). В случае, если таковые недоступны на текущий день (выходной или праздничный день), то использовать данные последнего закрытия (previous_close);
        </summary>

          - [x] 1. ddd
             - [x] 1.1. ddd

    </details>



- [x] <details>
        <summary>
            4.	Помечать транзакции, превысившие месячный лимит операций (технический флаг limit_exceeded);
        </summary>

          - [x] 1. ddd
             - [x] 1.1. ddd

    </details>



- [x] <details>
        <summary>
            5.	Дать возможность клиенту установить новый лимит. При установлении нового лимита микросервисом автоматически выставляется текущая дата, не позволяя выставить ее в прошедшем или будущем времени. Обновлять существующие лимиты запрещается;
        </summary>

          - [x] 1. ddd
             - [x] 1.1. ddd

    </details>



- [x] <details>
        <summary>
            6.	По запросу клиента возвращать список транзакций, превысивших лимит, с указанием лимита, который был превышен (дата установления, сумма лимита, валюта (USD));
        </summary>

          - [x] 1. ddd
             - [x] 1.1. ddd

    </details>



## API info







## Quickstart






## Branch info
- _**main**_ - _Production_ ветка, стабильная (завершенная) версия проекта;
- _**dev**_ - _Development_ ветка, интеграция всех _features_;






## Tech Stack

