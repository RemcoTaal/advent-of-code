class Day10 : Day("day10") {

    enum class INSTRUCTION {
        NOOP,
        ADDX
    }

    fun signalStrength(cycle: Int, x: Int): Int {
        return cycle * x
    }

    override fun executePartOne(): Int {
        var x = 1
        var cycle = 0
        val cyclesWithRegisters = ArrayList<Pair<Int, Int>>()

        file.forEachLine {
            val line = it.split(" ")
            val instruction = line[0]

            when (INSTRUCTION.valueOf(instruction.uppercase())) {
                INSTRUCTION.NOOP -> {
                    cycle++
                    cyclesWithRegisters.add(Pair(cycle, x))
                }

                INSTRUCTION.ADDX -> {
                    val xValue = line[1].toInt()
                    repeat(2) {
                        cycle++
                        cyclesWithRegisters.add(Pair(cycle, x))
                    }
                    x += xValue
                }
            }
        }

        val cycleRanges = IntProgression.fromClosedRange(20, 220, 40)
        return cyclesWithRegisters
            .filter { it.first in cycleRanges }
            .sumOf { signalStrength(it.first, it.second) }
    }

    override fun executePartTwo(): Any {
        TODO("Not yet implemented")
    }
}