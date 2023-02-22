package day2.part1

import java.io.File

fun main(args: Array<String>) {
    print(getMyTotalScore("day2.txt"))
}

const val WIN_SCORE = 6
const val DRAW_SCORE = 3
const val LOSE_SCORE = 0

enum class SHAPE(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3)
}

fun getMyTotalScore(fileName : String): Int {
    var myScore = 0
    var theirScore = 0
    File(fileName).forEachLine { round ->
        val encryptedShapes = round.toCharArray().filter { it != ' '}
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

fun decryptToShape(encryptedShape: Char): SHAPE {
    val encryptedShapeHashMap = HashMap<SHAPE, CharArray>()
    encryptedShapeHashMap[SHAPE.ROCK] = charArrayOf('A', 'X')
    encryptedShapeHashMap[SHAPE.PAPER] = charArrayOf('B', 'Y')
    encryptedShapeHashMap[SHAPE.SCISSORS] = charArrayOf('C', 'Z')

    var decryptedShape: SHAPE? = null
    for (keyValue in encryptedShapeHashMap) {
        if (keyValue.value.contains(encryptedShape)) {
            decryptedShape = keyValue.key
        }
    }

    if (decryptedShape == null) {
        throw InternalError("Encrypted shape not supported")
    }

    return decryptedShape
}

fun didIWin(myShape: SHAPE, theirShape: SHAPE): Boolean {
    return if (myShape == SHAPE.ROCK && theirShape == SHAPE.SCISSORS) {
        true
    } else if(myShape == SHAPE.PAPER && theirShape == SHAPE.ROCK) {
        true
    } else myShape == SHAPE.SCISSORS && theirShape == SHAPE.PAPER
}

fun isADraw(myShape: SHAPE, theirShape: SHAPE): Boolean {
    return myShape.name == theirShape.name
}