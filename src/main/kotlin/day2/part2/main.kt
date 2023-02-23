package day2.part2

import java.io.File

fun main(args: Array<String>) {
    print(getMyTotalScore("day2.txt"))
}

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

enum class RESULT(val score: Int) {
    WIN(6),
    DRAW(3),
    LOSE(0)
}

fun getMyTotalScore(fileName: String): Int {
    var myScore = 0
    var theirScore = 0
    File(fileName).forEachLine { round ->
        val encryptedShapes = round.toCharArray().filter { it != ' '}
        val theirShape = decryptToShape(encryptedShapes[0])
        val expectedResult = decryptToResult(encryptedShapes[1])
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

fun decryptToShape(encryptedShape: Char): Shape {
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

fun decryptToResult(encryptedChar: Char): RESULT {
    when (encryptedChar) {
        'Z' -> {
            return RESULT.WIN
        }

        'Y' -> {
            return RESULT.DRAW
        }

        'X' -> {
            return RESULT.LOSE
        }
    }
    throw InternalError("Encrypted char not supported")
}


fun didIWin(myShape: Shape, theirShape: Shape): Boolean {
    if (myShape.beats == theirShape) {
        return true
    }
    return false
}

fun isADraw(myShape: Shape, theirShape: Shape): Boolean {
    return myShape == theirShape
}

fun determineShapeToPlay(theirShape: Shape, expectedResult: RESULT): Shape {
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