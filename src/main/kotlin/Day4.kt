class Day4 : Day("day4") {

    private val IntRange.min
        get() = minOf(start, endInclusive)

    private val IntRange.max
        get() = maxOf(start, endInclusive)

    private infix fun IntRange.fullyContains(o: IntRange): Boolean = (min >= o.min && max <= o.max)
    private infix fun IntRange.overlaps(o: IntRange): Boolean = (max >= o.min && min <= o.max)

    override fun executePartOne(): Int {
        return getResult { range1, range2 -> return@getResult (range1 fullyContains  range2) || range2 fullyContains  range1 }
    }

    override fun executePartTwo(): Int {
        return getResult { range1, range2 -> return@getResult (range1 overlaps range2) || range2 overlaps range1 }
    }

    private fun getResult(comparison: (IntRange, IntRange) -> Boolean): Int {
        return file.readLines().sumOf { line ->
            val ranges = line.split(",", "-").map { it.toInt() }
            val range1 = ranges[0]..ranges[1]
            val range2 = ranges[2]..(ranges[3])
            if (comparison(range1, range2)) 1 else 0 as Int
        }
    }
}