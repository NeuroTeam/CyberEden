# CyberEden
# Волчий остров: киберпанк

 
## Описание:
Имеется мир (представляет из себя ячеистый круг, с порталами по границе, если пройдешь в одну сторону, выйдешь с другой, диаметрально противоположной). На картинках пример простого мира размера 100, где есть вода, трава и земля. Трава растет только на земле, на каждой клеточке мира может расти разное кол-во травы. Клетки, где мало или нет травы помечены коричневым (земля видна).
 
В этом мире живут два вида животных (сперва, потом можно еще добавить): зайцы и волки. И одно растение – трава.
Трава растет. Зайцы едят траву, размножаются и бояться волков. Волки едят зайцев, размножаются и никого не бояться.
Звери ходят по очереди.
### Вода
Никто по воде ходить не умеет, и в данном случае, вода – просто ограничивающий фактор. Потом можно сделать, чтобы рост травы зависел от воды рядом, и чтобы ее надо было пить и еще много чего. Порталы по краям нужны на случай добавления рыб /летающих зайцев.
### Зверье
Звери умные. У них есть интеллект, нейронная сеть (для начала – многослойный перцептрон). Еще звери умеют видеть – из ячейки, где сидит зверь, простираются лучи до каждой ячейки на границе его зрения, а цифровое значение каждого обратно пропорционально расстоянию до того, где он пересечет первый встречный объект. Вот как зверь видит «в общем». Зеленое и салатовое – все трава, но разных клеток
 
У зверя есть отдельное зрение для каждого вида объектов на карте. Т.е. пока для травы, для воды, для зайцев и для волков. Инфа от зрения подается на вход нейронной сети, а на выход он получает, чем ему заняться. Тут есть два варианта: либо сделать две нейронные сети, первая будет определять, ЧТО делать (идти-кушать-размножаться), а вторая КУДА делать (ячейку, в которую сделать ход, все звери ходят на одну ячейку), либо сделать одну сеть, которая будет выдавать все КУДА для всех ЧТО. Нужно попробовать. На картинке – пример общей НС
 
Звери бесполые, т.е. каждый может размножаться с каждым. Чтобы не нарушать моральные устои, можно считать, что это не звери, а группы зверей.
Звери умирают от голода и от старости.
Гены
Коэффициенты НС записаны бинарным кодом, конкретнее, кодом Грея, но не в прямом порядке, а вот так: предположим, мы выделяем два бита на коэффициент, который от 1 до 0 (на самом деле другие цифры), и тогда будет такое соотвествие
00  01  11 10
0  0.3  1  0.6
Т.е. задача – обеспечить минимальную разницу в числах при изменении одного бита.
Таким образом мы выписываем все коэффициенты в ряд и получаем генотип животного. Он используется при размножении. Сначала происходит кроссинговер (гены скрещивающихся животных «ломаются» в одинаковых позициях а потом сращиваются наоборот), а потом мутация (рандомные биты гена меняют значение)
 
В результате рождается новый детенышь, который чем-то похож на родителей, а чем-то уникален. И т.к. выживут наиболее приспособленные родители, то вероятнее детенышь будет также или лучше приспособлен к выживанию.
## Цель
Посмотреть, как все это будет работать. В идеале должно получиться биологическое равновесие, с становящимися умнее зайцами и волками.
Реализация
В планах использовать libGDX (советы по другим либам будут кстати, особенно если есть такие, которые не использует нэйтив апи, или поддерживают RPi) и повесить приложение на сервер, имея веб интерфейс, чтобы наблюдать.
## Улучшательства
Добавить цикл Зима-Лето (влияет на кол-во травы)
Усложнить территорию (добавить горы и радиационные аномалии, влияющие на мутацию)
Добавить необходимость пить и дождь (на траву тоже влияет)
Рандомная генерация красивых миров
Рисовалка для мира
 
