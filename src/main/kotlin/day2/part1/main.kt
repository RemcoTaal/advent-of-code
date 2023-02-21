package day2.part1

import java.io.File

fun main(args: Array<String>) {
    print(getMyTotalScore("day2.txt"))
}

const val WIN_SCORE = 6
const val DRAW_SCORE = 3
const val LOSE_SCORE = 0

enum class SHAPES(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3),
    UNKNOWN(0)
}

fun getMyTotalScore(fileName : String): Int {
    var myScore = 0
    var theirScore = 0
    File(fileName).forEachLine { round ->
        val encryptedShapes = round.split(" ")
        val theirShape = decryptToShape(encryptedShapes[0])
        val myShape = decryptToShape(encryptedShapes[1])

        if (didIWin(myShape, theirShape)) {
            myScore += WIN_SCORE + myShape.score
            theirScore += LOSE_SCORE + theirShape.score
        } else if (isADraw(myShape, theirShape)) {
            myScore += DRAW_SCORE + myShape.score
            theirScore += DRAW_SCORE + theirShape.score
        } else {
            myScore += LOSE_SCORE + myShape.score
            theirScore += WIN_SCORE + theirShape.score
        }
    }
    return myScore
}

fun decryptToShape(encryptedShape: String): SHAPES {
    when (encryptedShape) {
        "A", "X" -> {
            return SHAPES.ROCK
        }
        "B", "Y" -> {
            return SHAPES.PAPER
        }
        "C", "Z" -> {
            return SHAPES.SCISSORS
        }
    }
    return SHAPES.UNKNOWN
}

fun didIWin(myShape: SHAPES, theirShape: SHAPES): Boolean {
    return if (myShape == SHAPES.ROCK && theirShape == SHAPES.SCISSORS) {
        true
    } else if(myShape == SHAPES.PAPER && theirShape == SHAPES.ROCK) {
        true
    } else myShape == SHAPES.SCISSORS && theirShape == SHAPES.PAPER
}

fun isADraw(myShape: SHAPES, theirShape: SHAPES): Boolean {
    return myShape == theirShape
}