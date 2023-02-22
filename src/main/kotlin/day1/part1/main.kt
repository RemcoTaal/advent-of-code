package day1.part1

import java.io.File

fun main(args: Array<String>) {
    print(determineMostCalories("day1.txt"))
}

fun determineMostCalories(fileName : String): Int {
    var currentCalories = 0
    val allCalories = mutableListOf<Int>()
    File(fileName).forEachLine {calorie ->
        if (calorie == "") {
            allCalories.add(currentCalories)
            currentCalories = 0
            return@forEachLine
        }
        currentCalories = currentCalories.plus(calorie.toInt())
    }
    return getMostCalories(allCalories)
}

fun getMostCalories(allCalories : MutableList<Int>): Int {
    if (allCalories.size == 0) {
        throw InternalError("Empty array not supported")
    }
    return allCalories.maxByOrNull { it }!!
}