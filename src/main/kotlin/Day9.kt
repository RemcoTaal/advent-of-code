import java.io.File
import kotlin.math.abs

fun determineDirection(char: Char): Direction {
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

class Day9 : Day("day9") {
    override fun executePartOne(): Any {
        val rope = Rope(2)
        rope.move(file)
        return rope.getTotalUniquePositionsOfTailKnots()
    }

    override fun executePartTwo(): Any {
        val rope = Rope(10)
        rope.move(file)
        return rope.getTotalUniquePositionsOfTailKnots()
    }

    class Rope(knots: Int) {

        init {
            if (knots < 2) throw IllegalArgumentException("A rope needs to have at least 2 knots (1 for head and 1 for tail)")
        }

        private val head: Head = Head(0, 0)
        private val tail: Tail = Tail(head, knots - 1)

        fun move(file: File) {
            file.forEachLine { line ->
                val splitted = line.split(" ")
                val direction = determineDirection(splitted[0].single())
                val times = splitted[1].toInt()
                move(direction, times)
            }
        }

        private fun move(direction: Direction, times: Int) {
            when (direction) {
                Direction.UP -> repeat(times) {
                    head.moveUp()
                }

                Direction.DOWN -> repeat(times) {
                    head.moveDown()
                }

                Direction.LEFT -> repeat(times) {
                    head.moveLeft()
                }

                Direction.RIGHT -> repeat(times) {
                    head.moveRight()
                }
            }
        }

        fun getTotalUniquePositionsOfTailKnots(): Int {
            return tail.knots.flatMap { it.getUniquePositions() }.toSet().size + 1
        }
    }

    abstract class Part(var x: Int, var y: Int) {
        private val historyPositions = ArrayList<Position>()

        open fun moveToPosition(position: Position) {
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

        fun needsToMove(positionOtherPart: Position): Boolean {
            return !isNeighborOrSame(positionOtherPart)
        }

        fun getUniquePositions(): Set<Position> {
            return historyPositions.toSet()
        }

        fun getPreviousPosition(): Position {
            return historyPositions.last()
        }

        fun getCurrentPosition(): Position {
            return Position(x, y)
        }

        open fun moveUp() {
            historyPositions.add(getCurrentPosition())
            y--
        }

        open fun moveDown() {
            historyPositions.add(getCurrentPosition())
            y++
        }

        open fun moveLeft() {
            historyPositions.add(getCurrentPosition())
            x--
        }

        open fun moveRight() {
            historyPositions.add(getCurrentPosition())
            x++
        }

        fun isNeighborOrSame(p: Position): Boolean {
            return abs(x - p.x) in 0..1 && abs(y - p.y) in 0..1
        }

        data class Position(val x: Int, val y: Int)
    }

    class Head(x: Int, y: Int) : Part(x, y), Publisher {
        private var subscriber: Subscriber? = null

        override fun subscribe(subscriber: Subscriber) {
            this.subscriber = subscriber
        }

        override fun unsubscribe(subscriber: Subscriber) {
            this.subscriber = null
        }

        override fun notifySubscriber() {
            this.subscriber?.update(getCurrentPosition(), getPreviousPosition())
        }

        override fun moveToPosition(position: Position) {
            super.moveToPosition(position)
            notifySubscriber()
        }

        override fun moveUp() {
            super.moveUp()
            notifySubscriber()
        }

        override fun moveDown() {
            super.moveDown()
            notifySubscriber()
        }

        override fun moveLeft() {
            super.moveLeft()
            notifySubscriber()
        }

        override fun moveRight() {
            super.moveRight()
            notifySubscriber()
        }
    }

    class Tail(val head: Head, private val totalKnots: Int) {
        val knots = constructKnots()

        private fun constructKnots(): List<Knot> {
            if (totalKnots <= 0) {
                throw IllegalArgumentException("Tail needs to have at least 1 knot")
            }

            val knots = arrayListOf<Knot>()
            var knot: Knot
            var index = 0
            repeat(totalKnots) {

                if (index == 0) {
                    knot = Knot()
                    head.subscribe(knot)
                } else {
                    val previousKnotIndex = index - 1
                    val previousKnot = knots[previousKnotIndex]
                    knot = Knot()
                    previousKnot.subscribe(knot)
                    knots[previousKnotIndex] = previousKnot
                }
                knots.add(knot)
                index++
            }
            return knots
        }

        inner class Knot : Part(head.x, head.y), Publisher, Subscriber {
            private var subscriber: Subscriber? = null

            override fun subscribe(subscriber: Subscriber) {
                this.subscriber = subscriber
            }

            override fun unsubscribe(subscriber: Subscriber) {
                if (subscriber != this.subscriber) return
                this.subscriber = null
            }

            override fun notifySubscriber() {
                this.subscriber?.update(getCurrentPosition(), getPreviousPosition())
            }

            override fun update(currentPosition: Position, previousPosition: Position) {
                if (needsToMove(currentPosition)) {
                    moveToPosition(previousPosition)
                }
            }

            override fun moveToPosition(position: Position) {
                super.moveToPosition(position)
                notifySubscriber()
            }

            override fun moveUp() {
                super.moveUp()
                notifySubscriber()
            }

            override fun moveDown() {
                super.moveDown()
                notifySubscriber()
            }

            override fun moveLeft() {
                super.moveLeft()
                notifySubscriber()
            }

            override fun moveRight() {
                super.moveRight()
                notifySubscriber()
            }
        }
    }

    interface Publisher {
        fun subscribe(subscriber: Subscriber)
        fun unsubscribe(subscriber: Subscriber)
        fun notifySubscriber()
    }

    interface Subscriber {
        fun update(currentPosition: Part.Position, previousPosition: Part.Position)
    }
}