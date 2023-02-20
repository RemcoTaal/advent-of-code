package day1.part1

import java.io.File

fun main(args: Array<String>) {
    print(determineMostCalories("day1.txt"))
}

fun determineMostCalories(fileName : String): Int {
    var currentCalories = 0
    var mostCalories = 0
    File(fileName).forEachLine {
        currentCalories = if (it != "") {
            currentCalories.plus(it.toInt())
        } else {
            mostCalories = checkMostCalories(currentCalories, mostCalories)
            0
        }
    }
    return mostCalories
}

fun checkMostCalories(currentCalories : Int, mostCalories: Int): Int {
    if (currentCalories > mostCalories) {
        return currentCalories
    }
    return mostCalories
}