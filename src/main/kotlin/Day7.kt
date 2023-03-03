import FileSystem.Directory as Directory
import FileSystem.File as File

class Day7 : Day("day7") {
    private var fileSystem: FileSystem = FileSystem(Directory("/"))

    init {
        buildFileSystem()
    }

    private fun buildFileSystem() {
        file.readLines().forEach {line ->
            // Run commands
            if (line.startsWith("$")) {
                val lineParts = line.split(" ")
                val command = lineParts[1]
                val argument = if (lineParts.size == 3) lineParts[2] else null
                when (command) {
                    "cd" -> {
                        when (argument) {
                            "/" -> if (fileSystem.getCwd().name == argument) return@forEach else fileSystem.changeDirectory(argument)
                            ".." -> fileSystem.prevDirectory()
                            null -> return@forEach
                            else -> fileSystem.changeDirectory(argument)
                        }
                    }
                }
            }
            // Add directories/files
            else {
                val lineParts = line.split(" ")
                if (lineParts[0] == "dir") {
                    val directoryName = lineParts[1]
                    fileSystem.getCwd().createSubDirectory(Directory(directoryName))
                } else {
                    val fileSize = lineParts[0].toInt()
                    val fileName = lineParts[1]
                    fileSystem.getCwd().createFile(File(fileName, fileSize))
                }
            }
        }
    }

    override fun executePartOne(): Int {
        return fileSystem.getDirectories(fileSystem.root).filter { it.getSize() <= 100000 }.sumOf { it.getSize() }
    }

    override fun executePartTwo(): Int {
        val requiredFreeSpaceForUpdate = 30000000

        val freeSpace = fileSystem.getFreeSpace()
        val spaceToFreeUp = requiredFreeSpaceForUpdate - freeSpace

        return fileSystem.getDirectories(fileSystem.root).filter { it.getSize() >= spaceToFreeUp }.minOf { it.getSize() }
    }
}




