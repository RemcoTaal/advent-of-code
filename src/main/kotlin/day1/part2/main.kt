package day1.part2

import java.io.File

fun main(args: Array<String>) {
    print(determineTopThree("day1.txt"))
}

fun determineTopThree(fileName: String): Int {
    val allCalories = mutableListOf<Int>()
    var currentCalories = 0
    File(fileName).forEachLine { calorie ->
        if (calorie == "") {
            allCalories.add(currentCalories)
            currentCalories = 0
            return@forEachLine
        }
        currentCalories = currentCalories.plus(calorie.toInt())
    }
    return getTopThreeCalories(allCalories)
}

fun getTopThreeCalories(calories : MutableList<Int>): Int {
    if (calories.size < 3) {
        throw InternalError("List should contain at least 3 items")
    }
    return calories.sortedByDescending { it }.subList(0, 3).sum()
}