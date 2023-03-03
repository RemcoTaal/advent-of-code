import java.io.File as TextFile
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

fun getFile(name: String) = TextFile("src/main/resources", "$name.txt")

class FileSystem(root: DefaultMutableTreeNode): DefaultTreeModel(root, true) {
    private val totalDiskSpace = 70000000

    private var cwd: Directory = root as Directory
    private var directories: ArrayList<Directory> = ArrayList()
    override fun getRoot(): Directory {
        return root as Directory
    }
    fun changeDirectory(name: String) {
        getCwd().getSubDirectory(name)?.let { setCwd(it) } ?: throw InternalError("Directory not found")
    }
    fun prevDirectory(){
        if (cwd.parent == null) {
            setCwd(getRoot())
            return
        }
        setCwd(cwd.parent as Directory)
    }
    fun getCwd(): Directory {
        return cwd
    }
    private fun setCwd(cwd: Directory) {
        this.cwd = cwd
    }

    fun getDirectories(cwd: Directory): List<Directory> {
        cwd.getSubDirectories().forEach { directory ->
            directories.add(directory)
            getDirectories(directory)
        }
        return directories
    }
    fun getFreeSpace(): Int {
        return totalDiskSpace - getRoot().getSize()
    }

    class Directory(val name: String): DefaultMutableTreeNode() {
        fun getFiles(): List<File> {
            val files = mutableListOf<File>()
            this.children?.filter { it.isLeaf || !it.allowsChildren }?.forEach { files.add(it as File) }
            return files
        }

        fun getSize(): Int {
            return getFiles().sumOf { it.size } + getSubDirectories().sumOf { it.getSize() }
        }

        fun createFile(file: File): File {
            this.add(file)
            return file
        }
        fun getSubDirectories(): List<Directory> {
            val subDirectories = mutableListOf<Directory>()
            this.children?.filter { it.allowsChildren }?.forEach { subDirectories.add(it as Directory) }
            return subDirectories
        }
        fun getSubDirectory(name: String): Directory? {
            return getSubDirectories().firstOrNull { it.name == name }
        }
        fun createSubDirectory(directory: Directory): Directory {
            this.add(directory)
            return directory
        }

    }
    class File(val name: String, val size: Int) : DefaultMutableTreeNode(null, false)
}

