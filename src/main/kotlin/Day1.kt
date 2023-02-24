import java.io.File

class Day1 : Day("day1") {
    private var allCalories = getAllCalories()
    override fun executePartOne(): Any {
        return getMostCalories()
    }
    override fun executePartTwo(): Any {
        return getTopThreeCalories()
    }
    private fun getAllCalories(): MutableList<Int> {
        var currentCalories = 0
        val allCalories = mutableListOf<Int>()
        file.forEachLine {calorie ->
            if (calorie.isBlank()) {
                allCalories.add(currentCalories)
                currentCalories = 0
                return@forEachLine
            }
            currentCalories = currentCalories.plus(calorie.toInt())
        }
        return allCalories
    }
    private fun getMostCalories(): Int {
        return allCalories.maxByOrNull { it } ?: throw InternalError("Empty array not supported")
    }
    private fun getTopThreeCalories(): Int {
        if (allCalories.size < 3) {
            throw InternalError("List should contain at least 3 items")
        }
        return allCalories.sortedByDescending { it }.subList(0, 3).sum()
    }
}