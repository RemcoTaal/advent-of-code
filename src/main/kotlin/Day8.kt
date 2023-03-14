import java.io.File

class Day8 : Day("day8") {
    private val grid: Grid = Grid(file)

    override fun executePartOne(): Int {
        return getTotalVisibleTrees()
    }

    private fun getTotalVisibleTrees(): Int {
        val leftToRight = grid.content.sortedBy { it.coordinates.y }.chunked(grid.maxX)
        val rightToLeft = leftToRight.map { it.reversed() }
        val topToBottom = grid.content.sortedBy { it.coordinates.x }.chunked(grid.maxY)
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



    override fun executePartTwo(): Int {
        return 0
    }

    class Grid(val file: File) {
        val content: List<Tree> = setupGrid()
        val maxX = content.maxOf { it.coordinates.x } + 1
        val maxY = content.maxOf { it.coordinates.y } + 1

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






