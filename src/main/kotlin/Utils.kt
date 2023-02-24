import java.io.File

fun getFile(name: String) = File("src/main/resources", "$name.txt")