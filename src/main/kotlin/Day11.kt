class Day11 : Day("day11") {
    private var monkeys: ArrayList<Monkey> = initMonkeys()

    operator fun List<Monkey>.get(range: IntRange): List<Monkey> {
        return this.subList(range.first, range.last + 1)
    }

    override fun executePartOne(): Long {
        execute(rounds = 20) { operation, item -> operation(item) / 3 }
        return calculateMonkeyBusinessLevel()
    }

    override fun executePartTwo(): Long {
        resetMonkeys()
        execute(rounds = 10_000) { operation, item -> operation(item) }
        return calculateMonkeyBusinessLevel()
    }

    private fun resetMonkeys() {
        monkeys = initMonkeys()
    }

    private fun calculateMonkeyBusinessLevel(): Long {
        return monkeys
            .sortedByDescending { it.itemsInspected }[0..1]
            .fold(initial = 1) { acc, monkey ->
                acc * monkey.itemsInspected
            }
    }

    private fun execute(rounds: Int, inspectItem: (operation: Monkey.Operation, item: Item) -> Int) {
        repeat(rounds) {
            monkeys.forEach { monkey ->
                repeat(monkey.items.size) {
                    val monkeyIndexWithItem = monkey.inspectAndThrowItem(inspectItem)
                    monkeys[monkeyIndexWithItem.first].items.addLast(monkeyIndexWithItem.second)
                }
            }
        }
    }

    private fun initMonkeys(): ArrayList<Monkey> {
        val monkeys = ArrayList<Monkey>()
        file.readLines()
            .filter { it != "" && !it.contains("Monkey") }
            .chunked(5)
            .forEach { lines ->
                val items = ArrayDeque<Item>()
                lines[0]
                    .substringAfter("Starting items:")
                    .split(",")
                    .forEach {
                        val worryLevel = it.trim().toInt()
                        items.add(Item(worryLevel))
                    }

                val operator: OPERATOR
                var operand: Int? = null
                lines[1]
                    .substringAfter("Operation: new = old ")
                    .split(" ")
                    .let {
                        operator = OPERATOR.parse(it[0])

                        val operandString = it[1]
                        if (operandString != "old") operand = operandString.toInt()
                    }

                val operation = Monkey.Operation(operator, operand)

                val divisibleBy = lines[2].substringAfter("Test: divisible by ").toInt()
                val trueToMonkeyWithIndex = lines[3].substringAfter("If true: throw to monkey ").toInt()
                val falseToMonkeyWithIndex = lines[4].substringAfter("If false: throw to monkey ").toInt()

                val test = Monkey.Test(divisibleBy, trueToMonkeyWithIndex, falseToMonkeyWithIndex)

                monkeys.add(Monkey(items, operation, test))
            }
        return monkeys
    }

    class Monkey(val items: ArrayDeque<Item>, val operation: Operation, val test: Test) {
        var itemsInspected = 0

        fun inspectAndThrowItem(inspectItem: (operation: Operation, item: Item) -> Int): Pair<Int, Item> {
            val item = items.removeFirst()
            item.worryLevel = inspectItem(operation, item)
            itemsInspected++
            val monkeyIndex = test(item)
            return Pair(monkeyIndex, item)
        }

        class Operation(val operator: OPERATOR, val operand: Int?) {
            operator fun invoke(item: Item): Int {
                when (operator) {
                    OPERATOR.PLUS -> {
                        if (operand == null) {
                            return item.worryLevel + item.worryLevel
                        }
                        return item.worryLevel + operand
                    }

                    OPERATOR.MULTIPLY -> {
                        if (operand == null) {
                            return item.worryLevel * item.worryLevel
                        }
                        return item.worryLevel * operand
                    }
                }
            }
        }

        class Test(val divisibleBy: Int, val onTrueMonkeyIndex: Int, val onFalseMonkeyIndex: Int) {
            operator fun invoke(item: Item): Int {
                if (item.worryLevel % divisibleBy == 0) {
                    return onTrueMonkeyIndex
                }
                return onFalseMonkeyIndex
            }
        }
    }

    class Item(var worryLevel: Int)

    enum class OPERATOR(val type: String) {
        PLUS("+"),
        MULTIPLY("*");

        companion object {
            fun parse(type: String): OPERATOR {
                return OPERATOR.values().firstOrNull { it.type == type } ?: error("Unknown type: $type")
            }
        }
    }
}