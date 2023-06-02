class Day10 : Day("day10") {
    private val cycleWithRegister = mutableMapOf<Int, Int>()

    override fun executePartOne(): Int {
        var cycle = 0
        var x = 1

        file.forEachLine {
            val line = it.split(" ")
            val instruction = line[0]

            when (INSTRUCTION.valueOf(instruction.uppercase())) {
                INSTRUCTION.NOOP -> {
                    cycle++
                    cycleWithRegister[cycle] = x
                }

                INSTRUCTION.ADDX -> {
                    val xValue = line[1].toInt()
                    repeat(2) {
                        cycle++
                        cycleWithRegister[cycle] = x
                    }
                    x += xValue
                }
            }
        }

        val cycleRange = IntProgression.fromClosedRange(20, 220, 40)
        return cycleWithRegister
            .entries
            .filter { it.key in cycleRange }
            .sumOf { calculateSignalStrength(it.key, it.value) }
    }

    override fun executePartTwo(): String {
        val result = StringBuilder("\n")

        cycleWithRegister
            .values
            .chunked(40)
            .forEach { row ->
                row.forEachIndexed { index, register ->
                    val cycle = index + 1
                    when (cycle) {
                        in register..register + 2 -> result.append('#')
                        else -> result.append('.')
                    }
                }
                result.append('\n')
            }

        return result.toString()
    }

    private fun calculateSignalStrength(cycle: Int, x: Int): Int {
        return cycle * x
    }

    enum class INSTRUCTION {
        NOOP,
        ADDX
    }
}