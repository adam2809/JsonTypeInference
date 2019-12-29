import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.PrintWriter

fun main(args:Array<String>){
    val config = jacksonObjectMapper().readValue<List<Config>>(File("${System.getProperty("user.dir")}/src/main/resources/config.json"))
    config.forEach {
        val creator = DataClassCreator(File(it.source),it.className)
        File(it.dest).writeText(creator.toString())
    }
}

data class Config(val source:String,val dest:String,val className:String)

