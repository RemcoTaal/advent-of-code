package day3.part1

import java.io.File

fun main(args: Array<String>) {
    print(getSumOfPriorities("day3.txt"))
}

class ItemType(val char: Char, val priority: Int)

fun getSumOfPriorities(fileName : String): Int {
    val itemTypes = getItemTypes(1, 'a', 'z') + getItemTypes(27, 'A', 'Z')
    var prioritySum = 0
    File(fileName).forEachLine { rucksack ->
        val divideInteger = rucksack.length/2
        val firstCompartment = rucksack.substring(0, divideInteger).toCharArray()
        val secondCompartment = rucksack.substring(divideInteger, rucksack.length).toCharArray()
        val charInBothCompartments = firstCompartment.intersect(secondCompartment.toSet()).first().toChar()
        val itemType = toItemType(charInBothCompartments, itemTypes)
        prioritySum += itemType.priority
    }
    return prioritySum
}

fun toItemType(char: Char, itemTypes: List<ItemType>): ItemType {
    return itemTypes.firstOrNull { it.char == char}?: throw InternalError("Uknown char for item type")
}

fun getItemTypes(startPriority: Int, startCharacter: Char, endCharacter: Char): List<ItemType> {
    if(startCharacter > endCharacter) {
        throw InternalError("startCharacter should be lower then or equal to endCharacter")
    }
    val itemTypes = ArrayList<ItemType>()
    var currentCharacter = startCharacter
    var currentPriority = startPriority
    while (currentCharacter <= endCharacter) {
        itemTypes.add(ItemType(currentCharacter, currentPriority))
        currentCharacter++
        currentPriority++
    }
    return itemTypes
}