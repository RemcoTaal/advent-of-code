class Day4 : Day("day4") {
    private val IntRange.min
        get() = minOf(start, endInclusive)

    private val IntRange.max
        get() = maxOf(start, endInclusive)

    private infix fun IntRange.fullyContains(o: IntRange) = (min >= o.min && max <= o.max)
    private infix fun IntRange.overlaps(o: IntRange) = (max >= o.min && min <= o.max)

    override fun executePartOne(): Int {
        return getResult(true)
    }

    override fun executePartTwo(): Int {
        return getResult(false)
    }

    private fun getResult(useInfixOverlaps: Boolean): Int {
        var contained = 0
        file.readLines().forEach { line ->
            val ranges = line.split(",", "-").map { it.toInt() }
            val range1 = ranges[0]..ranges[1]
            val range2 = ranges[2]..(ranges[3])
            if (useInfixOverlaps) {
                if (range1 overlaps range2 || range2 overlaps range1) {
                    contained += 1
                }
            } else {
                if (range1 fullyContains  range2 || range2 fullyContains  range1) {
                    contained += 1
                }
            }

        }
        return contained
    }
}