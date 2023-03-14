import java.io.File

class Day8 : Day("day8") {
    private val grid: Grid = Grid(file)

    override fun executePartOne(): Int {
        return getTotalVisibleTrees()
    }

    override fun executePartTwo(): Int {
        return getMaxScenicScore()
    }

    private fun getTotalVisibleTrees(): Int {
        val leftToRight = grid.content.sortedBy { it.coordinates.y }.chunked(grid.maxX + 1)
        val rightToLeft = leftToRight.map { it.reversed() }
        val topToBottom = grid.content.sortedBy { it.coordinates.x }.chunked(grid.maxY + 1)
        val bottomToTop = topToBottom.map { it.reversed() }

        val visibleTrees = getVisibleTrees(leftToRight) + getVisibleTrees(rightToLeft) + getVisibleTrees(topToBottom) + getVisibleTrees(bottomToTop)
        return visibleTrees.distinct().size
    }

    private fun getVisibleTrees(sortedRows: List<List<Grid.Tree>>): List<Grid.Tree> {
        val visibleTrees = ArrayList<Grid.Tree>()
        sortedRows.forEach { row ->
            var latestHighestValue = 0
            row.forEachIndexed { index, tree ->
                if (index == 0 || tree.value > latestHighestValue) {
                    visibleTrees.add(tree)
                    latestHighestValue = tree.value
                }
            }
        }
        return visibleTrees
    }

    private fun getMaxScenicScore(): Int {
        // Skip edges because max score will always be 0
        return grid.content.filter { it.coordinates.x != 0 && it.coordinates.x != grid.maxX && it.coordinates.y != 0 && it.coordinates.y != grid.maxY }
            .maxOf { tree ->
                val horizontalRow = grid.content.filter { it.coordinates.y == tree.coordinates.y }.sortedBy { it.coordinates.x }
                val verticalRow = grid.content.filter { it.coordinates.x == tree.coordinates.x }.sortedBy { it.coordinates.y }

                // Use lastOrNull to get the blocking tree with the highest index
                val upScenicScore = verticalRow.lastOrNull { it.coordinates.y < tree.coordinates.y && it.value >= tree.value }?.let { tree.coordinates.y - it.coordinates.y } ?: tree.coordinates.y
                val leftScenicScore = horizontalRow.lastOrNull { it.coordinates.x < tree.coordinates.x && it.value >= tree.value }?.let { tree.coordinates.x - it.coordinates.x } ?: tree.coordinates.x

                // Use firstOrNull to get the blocking tree with the lowest index
                val rightScenicScore = horizontalRow.firstOrNull { it.coordinates.x > tree.coordinates.x && it.value >= tree.value }?.let { it.coordinates.x - tree.coordinates.x } ?: (grid.maxX - tree.coordinates.x)
                val downScenicScore = verticalRow.firstOrNull { it.coordinates.y > tree.coordinates.y && it.value >= tree.value }?.let { it.coordinates.y - tree.coordinates.y } ?: (grid.maxY - tree.coordinates.y)

                upScenicScore * leftScenicScore * rightScenicScore * downScenicScore
            }
    }

    class Grid(val file: File) {
        val content: List<Tree> = setupGrid()
        val maxX = content.maxOf { it.coordinates.x }
        val maxY = content.maxOf { it.coordinates.y }

        private fun setupGrid(): List<Tree> {
            return file.readLines().flatMapIndexed { y, line ->
                line.mapIndexed { x, char ->
                    Tree(Character.getNumericValue(char), Coordinates(x, y))
                }
            }
        }

        data class Tree(val value: Int, val coordinates: Coordinates)
        data class Coordinates(val x: Int, val y: Int)
    }
}






