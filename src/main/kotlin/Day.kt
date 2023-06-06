import java.io.File
import kotlin.system.measureTimeMillis

abstract class Day(fileName: String) {
    protected val file: File = getFile(fileName)

    fun execute() {
        val partOneResult: Any
        val executionTimeInMillisPartOne = measureTimeMillis {
            partOneResult = executePartOne()
        }
        printPartResultWithExecutionTime(1, partOneResult, executionTimeInMillisPartOne)

        val partTwoResult: Any
        val executionTimeInMillisPartTwo = measureTimeMillis {
            partTwoResult = executePartTwo()
        }
        printPartResultWithExecutionTime(2, partTwoResult, executionTimeInMillisPartTwo)
    }

    private fun printPartResultWithExecutionTime(partNumber: Int, partResult: Any, executionTimeInMillis: Long) {
        check(partNumber in 1..2) { "partNumber should be either 1 or 2" }

        val partResultMessage = "Part $partNumber: $partResult"
        val executionTimeMessage = "Execution time: $executionTimeInMillis ms"

        val line = StringBuilder().apply { repeat(executionTimeMessage.length) { this.append('_') } }
        if (partNumber == 1) println(line)
        println(partResultMessage)
        println(executionTimeMessage)
        println(line)
    }

    protected abstract fun executePartOne(): Any
    protected abstract fun executePartTwo(): Any
}