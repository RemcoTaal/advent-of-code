class Day6 : Day("day6") {

    private fun List<Byte>.onlyUnique(): Boolean {
        return this.size == this.toSet().size
    }

    private fun List<Byte>.getIndexOfLatestRepeatedByte(): Int {
        return this.slice(1 until lastIndex).indexOf(this.last())
    }

    override fun executePartOne(): Int {
        return getResult(4)
    }

    override fun executePartTwo(): Int {
        return getResult(14)
    }

    private fun getResult(windowSize: Int): Int {
        val bytes = file.readBytes()
        var currentIndex = 0
        while (true) {
            val slice = bytes.slice(currentIndex until currentIndex+windowSize)
            if(slice.onlyUnique()) {
                return currentIndex+windowSize
            } else {
                val indexLatestRepeatedByte = slice.getIndexOfLatestRepeatedByte()
                if (indexLatestRepeatedByte == -1) currentIndex++ else currentIndex += indexLatestRepeatedByte + 1
                if (currentIndex+windowSize>bytes.lastIndex) throw InternalError("No unique set of size $windowSize found")
            }
        }
    }
}




