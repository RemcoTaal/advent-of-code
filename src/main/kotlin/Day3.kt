class Day3 : Day("day3") {
    private val itemTypes: Map<Char, Int> = getItemTypes(1, 'a', 'z') + getItemTypes(27, 'A', 'Z')

    override fun executePartOne(): Int {
        return getSumOfPriorities()
    }

    override fun executePartTwo(): Int {
        return getSumOfGroupBadges()
    }

    private fun getSumOfPriorities(): Int {
        return file.readLines().sumOf { rucksack ->
            val divideInteger = rucksack.length / 2
            val firstCompartment = rucksack.substring(0, divideInteger).toSet()
            val secondCompartment = rucksack.substring(divideInteger, rucksack.length).toSet()
            val charInBothCompartments = firstCompartment.intersect(secondCompartment).first()
            getPriorityOfItemTypeOrThrow(charInBothCompartments)
        }
    }

    private fun getPriorityOfItemTypeOrThrow(char: Char): Int {
        return itemTypes[char] ?: throw InternalError("Item type not found")
    }

    private fun getSumOfGroupBadges(): Int {
        return file.readLines().chunked(3).sumOf { group ->
            val char = getCharInAllGroupsOrThrow(group[0].toSet(), group[1].toSet(), group[2].toSet())
            getPriorityOfItemTypeOrThrow(char)
        }
    }

    private fun getCharInAllGroupsOrThrow(group1: Set<Char>, group2: Set<Char>, group3: Set<Char>): Char {
        return group1.intersect(group2).intersect(group3).firstOrNull() ?: throw InternalError("No char in all groups")
    }

    private fun getItemTypes(startPriority: Int, startCharacter: Char, endCharacter: Char): HashMap<Char, Int> {
        if (startCharacter > endCharacter) {
            throw InternalError("startCharacter should be lower then endCharacter")
        }
        val itemTypes = HashMap<Char, Int>()
        var currentCharacter = startCharacter
        var currentPriority = startPriority
        while (currentCharacter <= endCharacter) {
            itemTypes[currentCharacter] = currentPriority
            currentCharacter++
            currentPriority++
        }
        return itemTypes
    }
}