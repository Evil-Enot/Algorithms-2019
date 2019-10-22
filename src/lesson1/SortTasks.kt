@file:Suppress("UNUSED_PARAMETER", "DEPRECATION")

package lesson1

import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 * Трудоемкость: O(n + n + n*log(n)) = O(n*log(n))
 * Ресурсоемкость: O(n)
 */
fun sortTimes(inputName: String, outputName: String) {
    val inputDate = File(inputName).readLines()

    File(outputName).bufferedWriter().use { writer ->
        val result = mutableListOf<Date>()
        val dateFormat = SimpleDateFormat("hh:mm:ss a", Locale.US)

        for (line in inputDate) {
            val date = dateFormat.parse(line)
            result.add(date)
        }

        result.sort()

        for (date in result) {
            writer.write(dateFormat.format(date) + "\n")
        }

        writer.close()
    }
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 * Трудоемкость: O(n + n) = O(n)
 * Ресурсоемкость: O(1)
 */
fun sortTemperatures(inputName: String, outputName: String) {
    File(outputName).bufferedWriter().use { writer ->
        val minValue = 2730 // This number need for sorting
        val count = IntArray(minValue + 5001)

        for (line in File(inputName).readLines()) {
            val temp = line.toDouble()
            require(!(temp !in -273..500 && !Regex("""-?[0-9]{0,3}\.\d""").matches(line))) { "Wrong argument" }
            count[line.replace(".", "").toInt() + minValue]++
        }

        for (i in 0 until minValue + 5001) {
            val buffer = i - minValue

            while (count[i] > 0) {
                if (buffer < 0) writer.write("-")
                writer.write(abs(buffer / 10).toString() + "." + abs(buffer % 10).toString() + "\n")
                count[i]--
            }
        }

        writer.close()
    }
}


/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 *
 * Ресурсоемкость - O(n)
 * Трудоемкость - O(n)
 */
fun sortSequence(inputName: String, outputName: String) {
    val inputNumbers = File(inputName).readLines()
    File(outputName).bufferedWriter().use { writer ->
        val count = mutableMapOf<Int, Int>()
        var maxCount = Int.MIN_VALUE
        var min = Int.MIN_VALUE // Min Value

        for (line in inputNumbers) count[line.toInt()] = count.getOrDefault(line.toInt(), 0) + 1

        for ((key, value) in count) {
            if (value > maxCount || (key < min && value == maxCount)) {
                min = key
                maxCount = value
            }
        }

        for (line in inputNumbers) {
            if (line.toInt() != min) {
                writer.run {
                    write(line)
                    newLine()
                }
            }
        }

        for (i in 0 until maxCount) {
            writer.write(min.toString())
            writer.newLine()
        }

        writer.close()
    }
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 *
 * Трудоемкость: O(n), n- длина второго массива
 * Ресурсоемкость: O(1)
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    var firstIndex = 0
    var secondIndex = first.size

    for (i in second.indices) {
        if (firstIndex < first.size && (secondIndex == second.size || first[firstIndex] <= second[secondIndex]!!)) {
            second[i] = first[firstIndex++]
        } else {
            second[i] = second[secondIndex++]
        }
    }
}

