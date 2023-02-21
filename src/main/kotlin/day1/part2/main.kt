package day1.part2

import java.io.File

fun main(args: Array<String>) {
    print(determineTopThree("day1.txt"))
}

fun determineTopThree(fileName: String): Int {
    val calories = mutableListOf<Int>()
    var currentCalories = 0
    File(fileName).forEachLine { calorie ->
        currentCalories = if (calorie != "") {
            currentCalories.plus(calorie.toInt())
        } else {
            calories.add(currentCalories)
            0
        }
    }
    return getTopThreeCalories(calories)
}

fun getTopThreeCalories(calories : MutableList<Int>): Int {
    return calories.sortedByDescending { it }.subList(0, 3).sum()
}