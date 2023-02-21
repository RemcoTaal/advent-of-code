package day2.part2

import java.io.File

fun main(args: Array<String>) {
    print(getMyTotalScore("day2.txt"))
}

enum class SHAPE(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    val beats: SHAPE
        get() = when (this) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }

    val loses: SHAPE
        get() = when (this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }
}

enum class RESULT(val score: Int) {
    WIN(6),
    DRAW(3),
    LOSE(0)
}

fun getMyTotalScore(fileName: String): Int {
    var myScore = 0
    var theirScore = 0
    File(fileName).forEachLine { round ->
        val encryptedString = round.split(" ")
        val theirShape = decryptToShape(encryptedString[0])
        val expectedResult = decryptToResult(encryptedString[1])
        val myShape = determineShapeToPlay(theirShape, expectedResult)
        // Play round
        if (didIWin(myShape, theirShape)) {
            myScore += RESULT.WIN.score + myShape.score
            theirScore += RESULT.LOSE.score + theirShape.score
        } else if (isADraw(myShape, theirShape)) {
            myScore += RESULT.DRAW.score + myShape.score
            theirScore += RESULT.DRAW.score + theirShape.score
        } else {
            myScore += RESULT.LOSE.score + myShape.score
            theirScore += RESULT.WIN.score + theirShape.score
        }
    }
    return myScore
}

fun decryptToShape(encryptedString: String): SHAPE {
    when (encryptedString) {
        "A" -> {
            return SHAPE.ROCK
        }

        "B" -> {
            return SHAPE.PAPER
        }

        "C" -> {
            return SHAPE.SCISSORS
        }
    }
    throw InternalError("Encrypted string not supported")
}

fun decryptToResult(encryptedString: String): RESULT {
    when (encryptedString) {
        "Z" -> {
            return RESULT.WIN
        }

        "Y" -> {
            return RESULT.DRAW
        }

        "X" -> {
            return RESULT.LOSE
        }
    }
    throw InternalError("Encrypted string not supported")
}


fun didIWin(myShape: SHAPE, theirShape: SHAPE): Boolean {
    return if (myShape == SHAPE.ROCK && theirShape == SHAPE.SCISSORS) {
        true
    } else if (myShape == SHAPE.PAPER && theirShape == SHAPE.ROCK) {
        true
    } else myShape == SHAPE.SCISSORS && theirShape == SHAPE.PAPER
}

fun isADraw(myShape: SHAPE, theirShape: SHAPE): Boolean {
    return myShape == theirShape
}

fun determineShapeToPlay(theirShape: SHAPE, expectedResult: RESULT): SHAPE {
    return when (expectedResult) {
        RESULT.WIN -> {
            theirShape.loses
        }
        RESULT.DRAW -> {
            theirShape
        }
        RESULT.LOSE -> {
            theirShape.beats
        }
    }
}