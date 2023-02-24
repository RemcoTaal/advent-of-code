class Day3 : Day("day3") {
    private val itemTypes: List<ItemType> = getItemTypes(1, 'a', 'z') + getItemTypes(27, 'A', 'Z')

    class Group(val badge: ItemType)
    class ItemType(val char: Char, val priority: Int)

    override fun executePartOne(): Int {
        return getSumOfPriorities()
    }

    override fun executePartTwo(): Int {
        return getSumOfGroupBadges()
    }

    private fun getSumOfPriorities(): Int {
        var prioritySum = 0
        file.forEachLine { rucksack ->
            val divideInteger = rucksack.length/2
            val firstCompartment = rucksack.substring(0, divideInteger).toCharArray()
            val secondCompartment = rucksack.substring(divideInteger, rucksack.length).toCharArray()
            val charInBothCompartments = firstCompartment.intersect(secondCompartment.toSet()).first().toChar()
            val itemType = toItemType(charInBothCompartments)
            prioritySum += itemType.priority
        }
        return prioritySum
    }

    private fun getSumOfGroupBadges(): Int {
        val groups = ArrayList<Group>()
        file.readLines().windowed(3, 3).forEach { group ->
            val char = getCharInAllGroups(group[0].toCharArray(), group[1].toCharArray(), group[2].toCharArray())
            groups.add(Group(toItemType(char)))
        }
        return groups.sumOf { it.badge.priority }
    }

    private fun toItemType(char: Char): ItemType {
        return itemTypes.firstOrNull { it.char == char } ?: throw InternalError("Unknown char for item type")
    }

    private fun getCharInAllGroups(group1: CharArray, group2: CharArray, group3: CharArray): Char {
        return group1.intersect(group2.toSet()).intersect(group3.toSet()).firstOrNull()
            ?: throw InternalError("No char in all groups")
    }

    private fun getItemTypes(startPriority: Int, startCharacter: Char, endCharacter: Char): List<ItemType> {
        if (startCharacter > endCharacter) {
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
}