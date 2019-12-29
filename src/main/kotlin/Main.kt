import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

fun main(args:Array<String>){
    val creator = DataClassCreator(File("/home/adam/code/JsonTypeInference/src/main/resources/test.json"),"Test")
    println(creator)
}