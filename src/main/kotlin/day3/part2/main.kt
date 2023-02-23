package day3.part2

import java.io.File

fun main(args: Array<String>) {
    print(getSumOfGroupBadges("day3.txt"))
}

class Group(val badge: ItemType)
class ItemType(val char: Char, val priority: Int)

fun getSumOfGroupBadges(fileName : String): Int {
    val itemTypes = getItemTypes(1, 'a', 'z') + getItemTypes(27, 'A', 'Z')
    var groups = ArrayList<Group>()
    File(fileName).readLines().windowed(3, 3).forEach{ group ->
        val char = getCharInAllGroups(group[0].toCharArray(), group[1].toCharArray(), group[2].toCharArray())
        groups.add(Group(toItemType(char, itemTypes)))
    }
    return groups.sumOf { it.badge.priority}
}

fun toItemType(char: Char, itemTypes: List<ItemType>): ItemType {
    return itemTypes.firstOrNull { it.char == char}?: throw InternalError("Unknown char for item type")
}

fun getCharInAllGroups(group1: CharArray, group2: CharArray, group3: CharArray): Char {
    return group1.intersect(group2.toSet()).intersect(group3.toSet()).firstOrNull() ?: throw InternalError("No intersection in all groups")
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