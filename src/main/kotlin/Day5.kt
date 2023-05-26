import java.util.*
import kotlin.collections.ArrayList

class Day5 : Day("day5") {
    private lateinit var crates: MutableMap<Int, Stack<Char>>
    private lateinit var procedures: ArrayList<Triple<Int, Int, Int>>

    private fun MutableMap<Int, Stack<Char>>.setup(line: String): MutableMap<Int, Stack<Char>> {
        CRATE_REGEX.findAll(line).forEach { crate ->
            val stackIndex = (crate.range.first / 4) + 1
            val stack = this.getOrPut(stackIndex) { Stack<Char>() }
            stack.push(crate.groupValues[2].first())
        }
        return this
    }

    private fun List<String>.toTriple(): Triple<Int, Int, Int> {
        return Triple(this[0].toInt(), this[1].toInt(), this[2].toInt())
    }

    private fun ArrayList<Triple<Int, Int, Int>>.setup(line: String): ArrayList<Triple<Int, Int, Int>> {
        PROCEDURE_REGEX.findAll(line).forEach { procedure ->
            this.add(procedure.groupValues.subList(1, 4).toTriple())
        }
        return this
    }

    override fun executePartOne(): List<Char> {
        initialize()
        return getResult { amount, fromKey, toKey -> moveSingleCrate(amount, fromKey, toKey) }
    }

    override fun executePartTwo(): List<Char> {
        initialize()
        return getResult { amount, fromKey, toKey -> moveMultipleCratesAtOnce(amount, fromKey, toKey) }
    }

    private fun initialize() {
        crates = mutableMapOf()
        procedures = ArrayList()
        file.readLines().forEach { line ->
            if (CRATE_REGEX.containsMatchIn(line)) crates.setup(line) else procedures.setup(line)
        }

        crates.map { it.key to it.value.reverse() }
        crates = crates.toSortedMap()
    }

    private fun getResult(executeProcedure: (amount: Int, fromKey: Int, toKey: Int) -> Unit): List<Char> {
        val topOfEachStack = mutableListOf<Char>()
        procedures.forEach { executeProcedure(it.first, it.second, it.third) }
        crates.forEach { topOfEachStack.add(it.value.lastElement()) }
        return topOfEachStack
    }

    private fun moveSingleCrate(times: Int, fromKey: Int, toKey: Int) {
        repeat(times) {
            val crateToMove = crates.getValue(fromKey).pop()
            crates.getValue(toKey).push(crateToMove)
        }
    }

    private fun moveMultipleCratesAtOnce(amount: Int, fromKey: Int, toKey: Int) {
        val cratesToMove = crates.getValue(fromKey).takeLast(amount)
        repeat(cratesToMove.size) { crates.getValue(fromKey).pop() }
        cratesToMove.forEach { crates.getValue(toKey).push(it) }
    }

    companion object {
        val CRATE_REGEX = """(\[([A-Z])\])""".toRegex()
        val PROCEDURE_REGEX = """move (\d+) from (\d+) to (\d+)""".toRegex()
    }
}





