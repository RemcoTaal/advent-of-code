import java.nio.CharBuffer

class Day6 : Day("day6") {

    private fun List<Byte>.onlyUnique(): Boolean {
        return this.size == this.toSet().size
    }

    override fun executePartOne(): Int {
        return getResult(4)
    }

    override fun executePartTwo(): Int {
        return getResult(14)
    }

    private fun getResult(windowSize: Int): Int {
        val bytes = file.readBytes()
        bytes.indices.forEach { i ->
            if (bytes.slice(i until i+windowSize).onlyUnique()) return i+windowSize
        }
        throw InternalError("No unique set of size $windowSize found")
    }
}




