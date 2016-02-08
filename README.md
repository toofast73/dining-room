# dining-room
Моей задачей было описать симулятор столовой, в котором не будут возникать deadlocks.

Входными данными для симуляция являются:
- Количество посетителей
- Количество столовых приборов (ножей, вилок, ложек)

У каждого посетителей есть любимое блюдо. Разные блюда имеют разные требования -- суп едят ложной, пасту - вилкой.

При помощи фабрики создаем DiningRoom -- объект в котором будет происходить симуляция.
На каждого из участников мы асинхронно запускаем задачу -- получаем CompletableFuture.
К future добавляем callback -- он обновит данные на frontend, когда посетитель доел и сдал посуду.

Deadlocks избегаются следующим образом:
- Вначале отсеиваются посетители, для которых задача не может быть решена.
Если в столовой нет ни одной ложки -- суп не съешь. Такие заказы получают статус FAILURE.

- Если заказ прошел первичную валидацию, значит рано или поздно его можно выполнить.
Посетитель запрашивает столовые приборы. Метод синхронизирован при помощи Reentrant lock.
Если приборов нет -- посетитель уходит в очередь ожидания и через определенное время повторяет попытку(Spring-retry).
Когда посетитель закончил обед -- он сдает столовые приборы в пул.

Для того, чтобы продемонстрировать симуляцию я создал простейший пользовательский интерфейс на ZK.
Он не красив, но демонстрирует MVVM data-binding.

Я создал два сценария. Happy case, проходит без проблем. И сценарий без ложек -- часть людей останется без обеда. Deadlock не происходит.

Простейший симулятор готов. Сегодня вечером я планирую его улучшить:
- Сделать UI более интерактивным, добавить форму создания сценария.
- Код покрыт тестами на ~55%. Улучшить показатель.

Чтобы запустить проект, необходимо скачать исходный код, перейти в папку проекта и исполнить команду

sh mvnw jetty:run

mvnw.cmd jetty:run 
для Windows

