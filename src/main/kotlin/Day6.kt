class Day6 : Day("day6") {
    private fun List<IndexedValue<Char>>.hasOnlyDistinctValues(): Boolean {
        return this.size == this.distinctBy { it.value }.size
    }

    override fun executePartOne(): Int {
        return getResult(4)
    }

    override fun executePartTwo(): Int {
        return getResult(14)
    }

    private fun getResult(windowSize: Int): Int {
        return file.readText().withIndex().windowed(windowSize, 1).first{it.hasOnlyDistinctValues()}.last().index + 1
    }
}




