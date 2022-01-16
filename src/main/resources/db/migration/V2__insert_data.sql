-- ARTIST data --

INSERT INTO public.artist (id, name)
VALUES (1, 'Церковь Детства');
INSERT INTO public.artist (id, name)
VALUES (2, 'Адаптация');
INSERT INTO public.artist (id, name)
VALUES (3, 'Алес');
INSERT INTO public.artist (id, name)
VALUES (4, 'Олег Коврига');
INSERT INTO public.artist (id, name)
VALUES (5, 'Леонид Фёдоров');

-- PUBLISHER data --

INSERT INTO public.publisher (id, name)
VALUES (1, 'Выргород');
INSERT INTO public.publisher (id, name)
VALUES (2, 'Полдень Music');
INSERT INTO public.publisher (id, name)
VALUES (3, 'Время');

-- CUSTOMER data --

INSERT INTO public.customer (id, first_name, last_name, email)
VALUES (1, 'Последний', 'Меломан', 'pm@last.fm');
INSERT INTO public.customer (id, first_name, last_name, email)
VALUES (2, 'Кто-то', 'Другой', 'somebody@to.love');

-- IMAGE data --

INSERT INTO public.image (id, name, content, content_type)
VALUES (1, 'Джут', lo_import('C:\mag\test_automation\term\wyrgorod\src\main\resources\images\1.jpg', 'image/jpeg'));
INSERT INTO public.image (id, name, content, content_type)
VALUES (2, 'Лаокоон', lo_import('C:\mag\test_automation\term\wyrgorod\src\main\resources\images\2.jpg', 'image/jpeg'));
INSERT INTO public.image (id, name, content, content_type)
VALUES (3, 'Минные поля', lo_import('C:\mag\test_automation\term\wyrgorod\src\main\resources\images\3.jpg', 'image/jpeg'));
INSERT INTO public.image (id, name, content, content_type)
VALUES (4, 'Часть текста отсутствует',
        lo_import('C:\mag\test_automation\term\wyrgorod\src\main\resources\images\4.jpg', 'image/jpeg'));
INSERT INTO public.image (id, name, content, content_type)
VALUES (5, 'Что я видел', lo_import('C:\mag\test_automation\term\wyrgorod\src\main\resources\images\5.jpg', 'image/jpeg'));
INSERT INTO public.image (id, name, content, content_type)
VALUES (6, 'Таял', lo_import('C:\mag\test_automation\term\wyrgorod\src\main\resources\images\6.jpg', 'image/jpeg'));

-- ITEM data --

INSERT INTO public.item (id, title, description, artist_id, publisher_id, price, medium, image_id)
VALUES (1, 'Джут', 'Трек-лист альбома "Джут"
1. Мой город будет стоять
2. По дороге домой
3. Встретимся
4. Лето
5. Там, где рождается боль
6. Грязь
7. Жизнь в полицейском государстве
8. Улицы города
9. Тысячи долгих дней пустоты

BONUS ("Live in Бишкек"):
10. Феодализм
11. Политкорректность
12. Дезертир
13. Убиваю себя
14. Алдар Косе
15. Телефонный разговор с Н.Н.

Ермен Анти - вокал, гитара
Ник Вдовиченко - бас
Саня Шиза - гитара
Сергей Щанкин - барабаны

В записи также принимали участие:
Владимир Белканов - клавиши (4)
Виталий Ширко - бас (2, 4)
Олег Панченко - гитара (8)
Алексей Вертоградов - шанкобыз (3)

Записано и сведено Алексеем Марковым и Алексеем Вертоградовым в Актюбинске и Москве с марта 2000 по март 2001 года.

Бонус-треки с "Live in Бишкек":
Ермен Анти - вокал, гитара
Ник Вдовиченко - бас
Сергей Щанкин - барабаны

Записано в клубе "Текила" (Бишкек, Киргизия)

Мастеринг - Сергей Летов, 2001
Фото: Наталья Мальгина, Павел Алексеев, Павел Клищенко, Ян Штраль
Помощь при подготовке издания: А. Вертоградов, О. Грибченков, Д. Гришин, А. Гусева, Е. Колесов, А. Кушимова
Оформление - Выргород, 2002', 2, 1, 500.00, 0, 1);
INSERT INTO public.item (id, title, description, artist_id, publisher_id, price, medium, image_id)
VALUES (2, 'Лаокоон', '1. Пескарь
2. Пух
3. Поля
4. Папа
5. Огни
6. Деревья
7. Лаокоон
8. Маша
9. У любви
10. Пузырь

Время звучания — 52''55''''.

Первый студийный альбом группы.
Запись, сведение: 2004-2005 и 2008-2009.

Дата выпуска — 09.06.2009.', 1, 1, 500.00, 0, 2);
INSERT INTO public.item (id, title, description, artist_id, publisher_id, price, medium, image_id)
VALUES (3, 'Минные поля', '1.Поля
2.Деревья
3.Нос
4.Мама
5.Папа
6.Ворон
7.Лесополоса
8.Маша
9.Лаокоон
10.Нежность
Бонус:
11.Поля Деня - Леонид Сойбельман
Видеобонус:
12. Ворон

Концерт в клубе “Форпост”, ноябрь 2003 г.

Женёк — барабаны;
Никки — скрипка;
Деня — гитара, пение;
Олежек — бас.

Все песни — Денис Третьяков, кроме "Мама" — американская народная песня, перевод Д.Третьякова;
"Нежность" — песня группы "Апрельский марш".

Бонус — записан в Берлине в мае 2003 г.

Оформление: рисунок для обложки — Антон Очиров, окончательная вёрстка — Александр Репьёв.

Дата выхода — 20.07.2004.', 1, 1, 500.00, 1, 3);
INSERT INTO public.item (id, title, description, artist_id, publisher_id, price, medium, image_id)
VALUES (4, 'Часть текста отсутствует', 'Твёрдый переплёт, 320 стр,, формат 170x132.
Серия "Поэтическая библиотека".
Редактор — Вера Копылова.
Фото на обложке: Игорь «Лётчик» Каргин.

Книгу составляют стихотворения 2012—2017 гг.', 3, 3, 400.00, 0, 4);
INSERT INTO public.item (id, title, description, artist_id, publisher_id, price, medium, image_id)
VALUES (5, 'Что я видел', 'Твёрдый переплёт, 352 стр., врезка с фотографиями. Формат 15х20 см.

Сборник мини-повестей Олега Ковриги - организатора многих «подпольных рок-концертов» в предперестроечные годы, а впоследствии основателя (совместно с Евгением Гапеевым) музыкального издательства «Отделение ВЫХОД» - книга во многом уникальная. Её части писались в разное время и по разным поводам, переписывались, дополнялись, менялись местами… И так около двадцати лет.
Герои книги - Пётр Мамонов, Андрей «Свин» Панов, Аня «Умка» Герасимова, Ольга Арефьева, Сергей «СиЛя» Селюнин, Алексей Хвостенко, Майк Науменко, Борис Гребенщиков и многие другие из числа тех, кого обычно называют «культовыми персонажами» и «мэтрами» русского рока и музыкального андеграунда.
Несколько историй посвящены чуть менее известным, но от того не менее интересным близким друзьям и хорошим знакомым Олега - тем, с кем приходилось работать, учиться, жить, кто так или иначе повлиял на мировоззрение автора.
В самом большом произведении Олег рассказывает о своей семье, однако это не просто личные мемуары, а описание целой эпохи: от дореволюционных до позднесоветских лет.

Книга адресована широкому кругу совершеннолетних читателей.', 4, 1, 500.00, 4, 5);
INSERT INTO public.item (id, title, description, artist_id, publisher_id, price, medium, image_id)
VALUES (6, 'Таял', 'Чёрный винил, 180 гр, вкладка. Тираж пластинки напечатан в США на заводе Quality Record Pressings, специализирующемся на аудиофильских виниловых изданиях.

Сторона A:
1. Титры 1
2. Бен Ладен
3. Полька Кого
4. Таял 1
5. Последняя видимость
6. Отец

Сторона B:
1. Холода
2. Дребезжать
3. Полька
4. Франциск
5. Титры 2
6. Таял 2

Мастеринг - Евгений Гапеев, 2015.

Музыка:
В. Волков (A1, A3, A5, A6, B2, B3, B4, B5); Л. Фёдоров (A2, A4, A5, B1, B6); В. Мартынов (A2, B4)
Тексты:
А. Волохонский (A3, A5, B2, B4); Д. Озерский (A4, B1, B6); А. Смуров и А. Молев (A2)
Исполнители:
В. Волков - контабас, синтезатор, фортепьяно, перкуссия, вокал
Л. Фёдоров - вокал, гитара, перкуссия, синтезатор
Ансамбль OPUS POSTH под управлением Т. Гринденко (A2, B4, B6)
Хор СИРИН под управлением А. Котова (B4)
С. Щураков - аккордеон (B6)
В оформлении использован фрагмент фрески Джотто "Св. Франциск Ассизский"
', 5, 2, 4000.00, 3, 6);

-- NEWS ITEM data --

INSERT INTO public.news_item (id, date, title, description, artist_id)
VALUES (1, '2021-12-16', 'Новогодняя акция', 'Дорогие друзья! Наш давний друг и автор Бранимир оставил новогодние поздравления на нескольких экземплярах своей книги "Цветы пустыни". У вас есть возможность приобрести экземпляр с автографом в комплекте с альбомами героев книги. В данный момент доступны следующие варианты:
Бранимир "Цветы пустыни" + Церковь Детства (2 CD)', 1);

-- ORDER data --

INSERT INTO public.order_ (id, customer_id, amount)
VALUES (1, 1, 6400.00);

-- ORDER ITEM data --

INSERT INTO public.order_item (id, item_id, quantity, order_id)
VALUES (1, 1, 1, 1);
INSERT INTO public.order_item (id, item_id, quantity, order_id)
VALUES (2, 2, 1, 1);
INSERT INTO public.order_item (id, item_id, quantity, order_id)
VALUES (3, 3, 1, 1);
INSERT INTO public.order_item (id, item_id, quantity, order_id)
VALUES (4, 4, 1, 1);
INSERT INTO public.order_item (id, item_id, quantity, order_id)
VALUES (5, 5, 1, 1);
INSERT INTO public.order_item (id, item_id, quantity, order_id)
VALUES (6, 6, 1, 1);