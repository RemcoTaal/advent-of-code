import kotlin.math.abs

class Day9 : Day("day9") {
    override fun executePartOne(): Any {
        return getTotalDistinctTailMoves()
    }

    override fun executePartTwo(): Any {
        TODO("Not yet implemented")
    }

    private fun getTotalDistinctTailMoves(): Int {
        val rope = Rope()
        file.forEachLine { line ->
            val splitted = line.split(" ")
            val direction = determineDirection(splitted[0].single())
            val times = splitted[1].toInt()
            rope.move(direction, times)
        }
        return rope.getTail().getHistoryPositions().distinct().size + 1
    }

    class Rope {
        private val head: Head = Head(0, 0)
        private val tail: Tail = Tail(0, 0)

        fun move(direction: Direction, times: Int) {
            when (direction) {
                Direction.UP -> repeat(times) {
                    head.moveUp()
                    if (tail.needsToMove(head.getCurrentPosition())) tail.moveToPosition(head.getPreviousPosition())
                }

                Direction.DOWN -> repeat(times) {
                    head.moveDown()
                    if (tail.needsToMove(head.getCurrentPosition())) tail.moveToPosition(head.getPreviousPosition())
                }

                Direction.LEFT -> repeat(times) {
                    head.moveLeft()
                    if (tail.needsToMove(head.getCurrentPosition())) tail.moveToPosition(head.getPreviousPosition())
                }

                Direction.RIGHT -> repeat(times) {
                    head.moveRight()
                    if (tail.needsToMove(head.getCurrentPosition())) tail.moveToPosition(head.getPreviousPosition())
                }
            }
        }

        fun getTail(): Tail {
            return tail
        }
    }

    open class Part(private var x: Int, private var y: Int) {
        private val historyPositions = ArrayList<Position>()

        fun moveToPosition(position: Position) {
            if (!moveIsAllowed(position)) throw InternalError("Move not allowed")

            historyPositions.add(getCurrentPosition())
            y = position.y
            x = position.x
        }

        // The following moves are allowed: ↖ ↑ ↗
        // (with a maximum of 1 per move)   ← · →
        //                                  ↙ ↓ ↘
        private fun moveIsAllowed(moveToPosition: Position): Boolean {
            val currentPosition = getCurrentPosition()
            return abs(currentPosition.x - moveToPosition.x) <= 1 && abs(currentPosition.y - moveToPosition.y) <= 1
        }

        fun getHistoryPositions(): ArrayList<Position> {
            return historyPositions
        }

        fun getPreviousPosition(): Position {
            return historyPositions.last()
        }

        fun getCurrentPosition(): Position {
            return Position(x, y)
        }

        fun moveUp() {
            historyPositions.add(getCurrentPosition())
            y--
        }

        fun moveDown() {
            historyPositions.add(getCurrentPosition())
            y++
        }

        fun moveLeft() {
            historyPositions.add(getCurrentPosition())
            x--
        }

        fun moveRight() {
            historyPositions.add(getCurrentPosition())
            x++
        }

        fun isNeighborOrSame(p: Position): Boolean {
            return abs(x - p.x) in 0..1 && abs(y - p.y) in 0..1
        }

        data class Position(val x: Int, val y: Int)

    }

    class Head(x: Int, y: Int) : Part(x, y)
    class Tail(x: Int, y: Int) : Part(x, y) {
        fun needsToMove(positionHead: Position): Boolean {
            return !isNeighborOrSame(positionHead)
        }
    }

    private fun determineDirection(char: Char): Direction {
        return when (char) {
            'U' -> Direction.UP
            'D' -> Direction.DOWN
            'L' -> Direction.LEFT
            'R' -> Direction.RIGHT
            else -> throw InternalError("Unsupported char")
        }
    }

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}