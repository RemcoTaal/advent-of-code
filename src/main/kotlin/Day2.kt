class Day2 : Day("day2") {
    enum class Shape(val score: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        val beats: Shape
            get() = when (this) {
                ROCK -> SCISSORS
                PAPER -> ROCK
                SCISSORS -> PAPER
            }

        val loses: Shape
            get() = when (this) {
                ROCK -> PAPER
                PAPER -> SCISSORS
                SCISSORS -> ROCK
            }
    }

    enum class Result(val score: Int) {
        WIN(6),
        DRAW(3),
        LOSE(0)
    }

    override fun executePartOne(): Any {
        return getMyTotalScorePartOne()
    }

    override fun executePartTwo(): Any {
        return getMyTotalScorePartTwo()
    }

    private fun getMyTotalScorePartOne(): Int {
        var myScore = 0
        file.forEachLine { round ->
            val encryptedShapes = round.toCharArray().filter { it != ' '}
            val theirShape = decryptToShape(encryptedShapes[0])
            val myShape = decryptToShape(encryptedShapes[1])
            // Play round
            myScore += playRound(myShape, theirShape)
        }
        return myScore
    }

    private fun getMyTotalScorePartTwo(): Int {
        var myScore = 0
        file.forEachLine { round ->
            val encryptedShapes = round.toCharArray().filter { it != ' '}
            val theirShape = decryptToShape(encryptedShapes[0])
            val expectedResult = decryptToResult(encryptedShapes[1])
            val myShape = determineShapeToPlay(theirShape, expectedResult)
            // Play round
            myScore += playRound(myShape, theirShape)
        }
        return myScore
    }

    private fun decryptToShape(encryptedShape: Char): Shape {
        val encryptedShapeHashMap = HashMap<Shape, CharArray>()
        encryptedShapeHashMap[Shape.ROCK] = charArrayOf('A', 'X')
        encryptedShapeHashMap[Shape.PAPER] = charArrayOf('B', 'Y')
        encryptedShapeHashMap[Shape.SCISSORS] = charArrayOf('C', 'Z')

        var decryptedShape: Shape? = null
        for (keyValue in encryptedShapeHashMap) {
            if (keyValue.value.contains(encryptedShape)) {
                decryptedShape = keyValue.key
            }
        }
        return decryptedShape ?: throw InternalError("Encrypted shape not supported")
    }

    private fun decryptToResult(encryptedChar: Char): Result {
        when (encryptedChar) {
            'Z' -> {
                return Result.WIN
            }

            'Y' -> {
                return Result.DRAW
            }

            'X' -> {
                return Result.LOSE
            }
        }
        throw InternalError("Encrypted char not supported")
    }


    private fun didIWin(myShape: Shape, theirShape: Shape): Boolean {
        if (myShape.beats == theirShape) {
            return true
        }
        return false
    }

    private fun isADraw(myShape: Shape, theirShape: Shape): Boolean {
        return myShape == theirShape
    }

    private fun determineShapeToPlay(theirShape: Shape, expectedResult: Result): Shape {
        return when (expectedResult) {
            Result.WIN -> {
                theirShape.loses
            }
            Result.DRAW -> {
                theirShape
            }
            Result.LOSE -> {
                theirShape.beats
            }
        }
    }

    private fun playRound(myShape: Shape, theirShape: Shape): Int {
        return if (didIWin(myShape, theirShape)) {
            Result.WIN.score + myShape.score
        } else if (isADraw(myShape, theirShape)) {
            Result.DRAW.score + myShape.score
        } else {
            Result.LOSE.score + myShape.score
        }
    }

}