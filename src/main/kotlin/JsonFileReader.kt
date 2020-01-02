import java.io.File

class JsonReaderFileCreator(sourceFile:File,destFile:File){
    val dataClassCreator = DataClassCreator(sourceFile, sourceFile.nameWithoutExtension)

    companion object{
        private final val READ_FUNCTION_TEMPLATE =
"""
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

fun getFromFile():%s{
    return jacksonObjectMapper().readValue(File(%s))
}

"""
    }

    init {
        destFile.writeText(READ_FUNCTION_TEMPLATE.format(sourceFile.nameWithoutExtension,sourceFile.absolutePath) + dataClassCreator.toString())
    }
}