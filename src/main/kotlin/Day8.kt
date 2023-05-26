class Day8 : Day("day8") {
    private val grid: List<IntArray> = setupGrid()

    private fun String.toIntArray(): IntArray {
        val intArray = IntArray(this.length)
        this.forEachIndexed { i, c -> intArray[i] = Character.getNumericValue(c) }
        return intArray
    }

    override fun executePartOne(): Int {
        return getTotalVisibleTrees()
    }

    override fun executePartTwo(): Int {
        return getMaxTreeScenicScore()
    }

    private fun setupGrid(): List<IntArray> {
        val grid = arrayListOf<IntArray>()
        return file.readLines().mapTo(grid) { it.toIntArray() }
    }

    private fun getTotalVisibleTrees(): Int {
        return grid.sumOf { y ->
            y.filterIndexed { x, _ ->
                grid.isTreeVisible(x, grid.indexOf(y))
            }.count()
        }
    }

    private fun getMaxTreeScenicScore(): Int {
        return grid.maxOf { y ->
            y.mapIndexed { x, _ ->
                grid.getTreeScenicScore(x, grid.indexOf(y))
            }.max()
        }
    }

    private fun getColumn(x: Int): IntArray {
        return grid.map { it[x] }.toIntArray()
    }

    private fun getRow(y: Int): IntArray {
        return grid[y]
    }

    private fun getTopTrees(x: Int, y: Int): IntArray {
        return getColumn(x).slice(0 until y).toIntArray()
    }

    private fun getBottomTrees(x: Int, y: Int): IntArray {
        val column = getColumn(x)
        return column.slice(y + 1 until column.size).toIntArray()
    }

    private fun getLeftTrees(x: Int, y: Int): IntArray {
        return getRow(y).slice(0 until x).toIntArray()
    }

    private fun getRightTrees(x: Int, y: Int): IntArray {
        val row = getRow(y)
        return row.slice(x + 1 until row.size).toIntArray()
    }

    private fun List<IntArray>.isTreeVisible(x: Int, y: Int): Boolean {
        val tree = this[y][x]

        val isOnEdge = (x == 0 || x == this.lastIndex || y == 0 || y == this.lastIndex)
        val isVisibleFromTop = getTopTrees(x, y).all { it < tree }
        val isVisibleFromLeft = getLeftTrees(x, y).all { it < tree }
        val isVisibleFromRight = getRightTrees(x, y).all { it < tree }
        val isVisibleFromBottom = getBottomTrees(x, y).all { it < tree }

        return isOnEdge || isVisibleFromTop || isVisibleFromLeft || isVisibleFromRight || isVisibleFromBottom
    }

    private fun List<IntArray>.getTreeScenicScore(x: Int, y: Int): Int {
        val tree = this[y][x]

        // Use indexOfLast to get the blocking tree with the highest index
        val upScenicScore =
            getTopTrees(x, y)
                .indexOfLast { it >= tree }
                .let { visibility -> if (visibility > -1) y - visibility else y }
        val leftScenicScore =
            getLeftTrees(x, y)
                .indexOfLast { it >= tree }
                .let { visibility -> if (visibility > -1) x - visibility else x }

        // Use indexOfFirst to get the blocking tree with the lowest index
        val rightScenicScore =
            getRightTrees(x, y)
                .indexOfFirst { it >= tree }
                .let { visibility -> if (visibility > -1) visibility + 1 else grid.lastIndex - x }
        val bottomScenicScore =
            getBottomTrees(x, y)
                .indexOfFirst { it >= tree }
                .let { visibility -> if (visibility > -1) visibility + 1 else grid.lastIndex - y }

        return upScenicScore * leftScenicScore * rightScenicScore * bottomScenicScore
    }
}






