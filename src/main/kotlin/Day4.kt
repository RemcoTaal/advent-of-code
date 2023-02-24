class Day4 : Day("day4") {
    private val IntRange.min
        get() = minOf(start, endInclusive)

    private val IntRange.max
        get() = maxOf(start, endInclusive)

    private infix fun IntRange.fullyContains(o: IntRange) = (min >= o.min && max <= o.max)

    override fun executePartOne(): Int {
        return getFullyContained()
    }

    override fun executePartTwo(): Int {
        TODO("Not yet implemented")
    }

    private fun getFullyContained(): Int {
        var contained = 0
        file.readLines().forEach { line ->
            val ranges = line.split(",", "-").map { it.toInt() }
            val range1 = ranges[0]..ranges[1]
            val range2 = ranges[2]..(ranges[3])
            if (range1 fullyContains range2 || range2 fullyContains range1) {
                contained += 1
            }
        }
        return contained
    }
}