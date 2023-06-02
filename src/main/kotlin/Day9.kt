import java.io.File
import kotlin.math.abs

interface Publisher {
    fun subscribe(subscriber: Subscriber)
    fun unsubscribe(subscriber: Subscriber)
    fun notifySubscriber()
}

interface Subscriber {
    fun update(currentPositionPublisher: Day9.Knot.Position)
}

//  The following directions are allowed: ↖ ↑ ↗
//                                        ← · →
//                                        ↙ ↓ ↘
enum class Direction {
    UP_LEFT,
    UP,
    UP_RIGHT,
    LEFT,
    RIGHT,
    DOWN_LEFT,
    DOWN,
    DOWN_RIGHT
}

fun determineDirection(char: Char): Direction {
    return when (char) {
        'U' -> Direction.UP
        'D' -> Direction.DOWN
        'L' -> Direction.LEFT
        'R' -> Direction.RIGHT
        else -> throw InternalError("Unsupported char")
    }
}


class Day9 : Day("day9") {

    override fun executePartOne(): Any {
        val rope = Rope(2)
        rope.performMoves(file)
        return rope.getTotalUniquePositionsOfLastTailKnot()
    }

    override fun executePartTwo(): Any {
        val rope = Rope(10)
        rope.performMoves(file)
        return rope.getTotalUniquePositionsOfLastTailKnot()
    }

    class Rope(val totalKnots: Int) {

        init {
            check(totalKnots >= 2) { "A rope needs to have at least 2 knots (1 for head and 1 for tail)" }
        }

        private val head: Head = Head(0, 0)
        private val tail: Tail = Tail(head)

        fun performMoves(file: File) {
            file.forEachLine { line ->
                val splitted = line.split(" ")
                val direction = determineDirection(splitted[0].single())
                val times = splitted[1].toInt()
                head.move(direction, times)
            }
        }

        fun getTotalUniquePositionsOfLastTailKnot(): Int {
            return tail.knots.last().getUniquePositions().size + 1
        }

        inner class Head(x: Int, y: Int) : Knot(x, y)
        inner class Tail(val head: Head) {
            private val totalTailKnots = totalKnots - 1
            val knots = initKnots()

            private fun initKnots(): List<TailKnot> {
                var knot: TailKnot
                val knots = ArrayList<TailKnot>()

                var index = 0
                repeat(totalTailKnots) {
                    // Subscribe the first TailKnot to the Head of the Rope
                    if (index == 0) {
                        knot = TailKnot()
                        head.subscribe(knot)
                    }
                    // Subscribe every other knot to the previous knot in the array
                    else {
                        knot = TailKnot()
                        knots[index - 1].subscribe(knot)
                    }

                    knots.add(knot)
                    index++
                }
                return knots
            }

            inner class TailKnot : Knot(head.x, head.y)
        }
    }

    abstract class Knot(var x: Int, var y: Int) : Publisher, Subscriber {
        private var subscriber: Subscriber? = null
        private var historyPositions: ArrayList<Position> = arrayListOf()

        override fun subscribe(subscriber: Subscriber) {
            this.subscriber = subscriber
        }

        override fun unsubscribe(subscriber: Subscriber) {
            if (subscriber != this.subscriber) return
            this.subscriber = null
        }

        override fun notifySubscriber() {
            this.subscriber?.update(getCurrentPosition())
        }

        override fun update(currentPositionPublisher: Position) {
            if (!needsToMove(currentPositionPublisher)) return

            val direction = determineDirectionToMove(currentPositionPublisher)
            move(direction)
        }

        fun move(direction: Direction, times: Int = 1) {
            if (times < 1) throw IllegalArgumentException("times needs to be >= 1")
            when (direction) {
                Direction.UP_LEFT -> repeat(times) {
                    moveUpLeft()
                    notifySubscriber()
                }

                Direction.UP -> repeat(times) {
                    moveUp()
                    notifySubscriber()
                }

                Direction.UP_RIGHT -> repeat(times) {
                    moveUpRight()
                    notifySubscriber()
                }

                Direction.LEFT -> repeat(times) {
                    moveLeft()
                    notifySubscriber()
                }

                Direction.RIGHT -> repeat(times) {
                    moveRight()
                    notifySubscriber()
                }

                Direction.DOWN_LEFT -> repeat(times) {
                    moveDownLeft()
                    notifySubscriber()
                }

                Direction.DOWN -> repeat(times) {
                    moveDown()
                    notifySubscriber()
                }

                Direction.DOWN_RIGHT -> repeat(times) {
                    moveDownRight()
                    notifySubscriber()
                }
            }
        }

        private fun needsToMove(positionOtherPart: Position): Boolean {
            return !isAdjacentOrSamePosition(positionOtherPart)
        }

        fun getUniquePositions(): Set<Position> {
            return historyPositions.toSet()
        }

        private fun getCurrentPosition(): Position {
            return Position(x, y)
        }

        private fun moveUpLeft() {
            historyPositions.add(getCurrentPosition())
            y++
            x--
        }

        private fun moveUp() {
            historyPositions.add(getCurrentPosition())
            y++
        }

        private fun moveUpRight() {
            historyPositions.add(getCurrentPosition())
            y++
            x++
        }

        private fun moveLeft() {
            historyPositions.add(getCurrentPosition())
            x--
        }

        private fun moveRight() {
            historyPositions.add(getCurrentPosition())
            x++
        }

        private fun moveDownLeft() {
            historyPositions.add(getCurrentPosition())
            y--
            x--
        }

        private fun moveDown() {
            historyPositions.add(getCurrentPosition())
            y--
        }

        private fun moveDownRight() {
            historyPositions.add(getCurrentPosition())
            y--
            x++
        }

        private fun determineDirectionToMove(other: Position): Direction {
            check(!isAdjacentOrSamePosition(other)) { "Part is already adjacent to the other position" }

            return when {
                // Up
                other.y > y -> when {
                    other.x < x -> Direction.UP_LEFT
                    other.x == x -> Direction.UP
                    else -> Direction.UP_RIGHT
                }

                // Left or right
                other.y == y -> when {
                    other.x < x -> Direction.LEFT
                    other.x > x -> Direction.RIGHT
                    else -> error("")
                }

                // Down
                else -> when {
                    other.x < x -> Direction.DOWN_LEFT
                    other.x == x -> Direction.DOWN
                    else -> Direction.DOWN_RIGHT
                }
            }
        }

        private fun isAdjacentOrSamePosition(other: Position): Boolean {
            return abs(x - other.x) <= 1 && abs(y - other.y) <= 1
        }

        data class Position(val x: Int, val y: Int)
    }
}