import java.io.File

abstract class Day(fileName: String) {
    protected val file: File = getFile(fileName)

    fun execute() {
        println("Part 1: ${executePartOne()}")
        println("Part 2: ${executePartTwo()}")
    }
    protected abstract fun executePartOne(): Int
    protected abstract fun executePartTwo(): Int
}