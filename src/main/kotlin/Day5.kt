import java.util.*
import kotlin.collections.ArrayList

class Day5 : Day("day5") {
    companion object {
        val CRATE_REGEX = """(\[([A-Z])\])""".toRegex()
        val PROCEDURE_REGEX = """move (\d+) from (\d+) to (\d+)""".toRegex()
    }
    private lateinit var crates: MutableMap<Int, Stack<Char>>
    private lateinit var procedures: ArrayList<Triple<Int, Int, Int>>

    private fun MutableMap<Int, Stack<Char>>.setup(line: String): MutableMap<Int, Stack<Char>> {
        CRATE_REGEX.findAll(line).forEach { crate ->
            val stackIndex = (crate.range.first / 4) + 1
            val stack  = this.getOrPut(stackIndex) {Stack<Char>()}
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

    init {
        initialize()
    }

    override fun executePartOne(): List<Char> {
        return getResult(false)
    }

    override fun executePartTwo(): List<Char> {
        initialize()
        return getResult(true)
    }

    private fun initialize() {
        crates = mutableMapOf()
        procedures = ArrayList()
        file.readLines().forEach {line ->
            if(CRATE_REGEX.containsMatchIn(line)) {
                crates.setup(line)
            } else {
                procedures.setup(line)
            }
        }

        crates.map { it.key to it.value.reverse()}
        crates = crates.toSortedMap()
    }

    private fun getResult(maintainOrder: Boolean): List<Char> {
        val topOfEachStack = mutableListOf<Char>()
        procedures.forEach { executeProcedure(it.first, it.second, it.third, maintainOrder)}
        crates.forEach{topOfEachStack.add(it.value.lastElement())}
        return topOfEachStack
    }

    private fun executeProcedure(amount: Int, fromKey: Int, toKey: Int, maintainOrder: Boolean) {
        if (maintainOrder) moveMultipleCrates(amount, fromKey, toKey) else repeat(amount) {this.moveSingleCrate(fromKey, toKey)}
    }

    private fun moveSingleCrate(fromKey: Int, toKey: Int) {
        val crateToMove = crates.getValue(fromKey).pop()
        crates.getValue(toKey).push(crateToMove)
    }

    private fun moveMultipleCrates(amount: Int, fromKey: Int, toKey: Int) {
        val cratesToMove = crates.getValue(fromKey).takeLast(amount)
        repeat(cratesToMove.size) { crates.getValue(fromKey).pop() }
        cratesToMove.forEach { crates.getValue(toKey).push(it) }
    }
}




