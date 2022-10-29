import java.util.*
import kotlin.math.roundToInt

val hashMap = mutableMapOf<Int, Int>()

const val blue = "\u001B[34m"
const val cyan = "\u001B[36m"
const val yellow = "\u001B[33m"
const val reset = "\u001B[0m"

const val n = 51
const val numbersCountInLine = 15
val t = (n * 1.5).roundToInt()

val scanner = Scanner(System.`in`)

private var operationsNeededMiddleValue = 0

fun main() {
    var chosenMenuIndex: Int

    repeat(t) {
        hashMap.set(it, 0)
    }

    println("${cyan}26 вариант $reset| ${cyan}Шайхльбарин Денис")

    println("${blue}Изначальный список:${reset} ${generateRandomSourceValues()}")
    hashMapOutput()

    while (true) {
        println("${blue}Выберите следующее действие:")
        println("${cyan}1)$reset Добавить элемент вручную")
        println("${cyan}2)$reset Добавить случайный элемент")
        println("${cyan}3)$reset Удалить элемент по значению")
        println("${cyan}4)$reset Найти элемент по значению")
        println("${cyan}5)$reset Заменить элемент")
        println("${cyan}6)$reset Вывести таблицу")
        println("${cyan}0)$reset Завершить работу программы")
        chosenMenuIndex = customIntInput(0..6)
        when (chosenMenuIndex) {
            1 -> inputValueInterface()
            2 -> addRandomValue()
            3 -> removeValueInterface()
            4 -> findValueInterface()
            5 -> replaceValueInterface()
            6 -> hashMapOutput()
            0 -> break
        }
    }

    print("${yellow}Работа программы завершена.")
}

fun customIntInput(range: IntRange): Int {
    var input: Int
    do {
        print("${yellow}> $reset")
        input = scanner.nextInt()
    } while (input !in range)
    println()
    return input
}

fun indexOfElement(e: Int): Int? {
    val index0 = firstIndex(e)
    var index = index0
    var i = 1

    while (hashMap[index] != 0 && hashMap[index] != e)
        index = collisionEliminationMethod(index0, i++)

    return if (hashMap[index] == 0)
        null
    else
        index
}

fun addRandomValue() {
    var currentOperableValue: Int

    do {
        currentOperableValue = getRandomNumberInCorrectRange()
    } while (hashMap.values.contains(currentOperableValue))

    val index = hashFunction(currentOperableValue)
    hashMap[index] = currentOperableValue

    println("Помещено значение $cyan$currentOperableValue$reset по ключу $cyan$index$reset.")
}

fun inputValueInterface() {
    println("Введите элемент:")
    var currentOperableValue: Int

    while (true) {
        currentOperableValue = customIntInput(1000..9999)
        if (hashMap.values.contains(currentOperableValue).not())
            break
        println("Такой элемент ${cyan}уже есть$reset на позиции $cyan${indexOfElement(currentOperableValue)}$reset. Введите другое число.")
    }

    val index = hashFunction(currentOperableValue)
    hashMap[index] = currentOperableValue

    println("Помещено значение $cyan$currentOperableValue$reset по ключу $cyan$index$reset.")
}

fun removeValueInterface() {
    println("${blue}Введите значение, которое необходимо удалить из таблицы:")
    deleteElement(
        customIntInput(1000..9999)
    )
}

fun findValueInterface() {
    println("${blue}Введите значение, которое необходимо найти:")
    val temporalInputValue = customIntInput(1000..9999)
    val temporalResult = indexOfElement(temporalInputValue)
    when (temporalResult) {
        null ->
            println("Такой элемент не был найден.")

        else ->
            println("Индекс искомого элемента $cyan$temporalInputValue$reset: $cyan$temporalResult")
    }
}

fun replaceValueInterface() {
    println("${blue}Введите заменяемое значение:")
    val from = customIntInput(1000..9999)

    println("${blue}Введите заменяющее значение:")
    val to = customIntInput(1000..9999)

    val fromIndex = indexOfElement(from)
    val toIndex = indexOfElement(to)

    if (fromIndex == null) {
        println("${cyan}Невозможно выполнить замену:$reset Заменяемого элемента не существует в таблице.")
        return
    }

    if (toIndex != null) {
        println("${cyan}Невозможно выполнить замену:$reset Заменяющий элемент уже существует в таблице.")
        return
    }

    deleteElement(from)
    addElement(to)
}

fun generateRandomSourceValues(): ArrayList<Int> {
    var currentOperableValue: Int

    val resultList = arrayListOf<Int>()

    repeat(n) {
        do {
            currentOperableValue = getRandomNumberInCorrectRange()
        } while (resultList.contains(currentOperableValue))
        resultList.add(currentOperableValue)
        hashMap[hashFunction(currentOperableValue)] = currentOperableValue
    }

    return resultList
}

fun addElement(e: Int) {
    val it = hashFunction(e).let {
        if (indexOfElement(e) == null) {
            println("Добавлен элемент $cyan$e$reset на позицию $cyan$it$reset.")
            hashMap[it] = e
        } else
            println("${cyan}Невозможно добавить элемент $e:$reset Элемент с таким значением уже существует.")
    }
}

fun deleteElement(e: Int) {
    indexOfElement(e).let {
        if (it != null) {
            println("Удалён элемент $cyan$e$reset с позиции $cyan$it$reset.")
            hashMap[it] = -1
        } else
            println("${cyan}Невозможно добавить элемент $e:$reset Элемента с таким значением не существует.")
    }
}

fun getRandomNumberInCorrectRange() =
    (1000..9999).random()

fun hashMapOutput() {
    for (i in 0..t / numbersCountInLine) {
        println((blue.padEnd(7 * numbersCountInLine + 13, '-')))
        print("  ${cyan}Key  $blue|$reset")
        for (j in i * numbersCountInLine until (i + 1) * numbersCountInLine)
            print(" ${j.let { if (!hashMap.containsKey(j)) "N/A" else it }.toString().padEnd(4, ' ')} $blue|$reset")
        println()
        println((blue.padEnd(7 * numbersCountInLine + 13, '-')))
        print(" ${cyan}Value $blue|$reset")
        for (j in i * numbersCountInLine until (i + 1) * numbersCountInLine)
            print(
                " ${
                    hashMap[j].let { if (!hashMap.containsKey(j)) "N/A" else if (it == null) 0 else it }.toString()
                        .padEnd(4, ' ')
                } $blue|$reset"
            )
        println()
        println((blue.padEnd(7 * numbersCountInLine + 13, '-')))
        println()
    }

    println("${blue}Процент заполнения таблицы:$reset ${hashMap.values.count { it != 0 } * 100 / hashMap.values.size}%")
    println("${blue}Среднее число проб:$reset ${String.format("%.3f", operationsNeededMiddleValue.toDouble() / n)}")
}

fun firstIndex(e: Int) = e * e % 100 % t
fun collisionEliminationMethod(index0: Int, i: Int) = (index0 + i) % t

fun hashFunction(element: Int): Int {
    val index0 = firstIndex(element)
    var index = index0
    var i = 1
    var operationsCount = 1

    while (hashMap[index] != 0 && hashMap[index] != -1) {
        index = collisionEliminationMethod(index0, i++)
        operationsCount++
    }

    operationsNeededMiddleValue += operationsCount
    return index
}