import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.lang.StringBuilder

class DataClassCreator(sourceFile:File,private val topLevelClassName: String){
    constructor(sourceFile:File):this(sourceFile,"DataClass0")

    private val dataClassList = mutableListOf<DataClass>()

    private val root = jacksonObjectMapper().readTree(sourceFile)

    private val jsonTypeToActionMapping = mapOf(
        JsonNodeType.STRING to fun(str:Map.Entry<String,JsonNode>,index:Int){
            addPropertyToDataClass(index,"String",str.key)
        },
        JsonNodeType.BOOLEAN to fun(entry:Map.Entry<String,JsonNode>,i:Int){
            addPropertyToDataClass(i,"Boolean",entry.key)
        },
        JsonNodeType.NUMBER to fun(num:Map.Entry<String,JsonNode>,index:Int){
            addPropertyToDataClass(index,resolveJsonNumberType(num.value),num.key)
        },
        JsonNodeType.ARRAY to fun(arr:Map.Entry<String,JsonNode>, index:Int){
            addPropertyToDataClass(index,resolveJsonArrayType(arr.value),arr.key)
        },
        JsonNodeType.OBJECT to ::addObjectJsonNode
    )

    init {
        if(root.nodeType != JsonNodeType.OBJECT){
            dataClassList.add(DataClass(topLevelClassName, mutableListOf()))
        }

        val dummyEntry = mapOf("topLevel" to root).entries.first()
        jsonTypeToActionMapping[root.nodeType]?.invoke(dummyEntry,dataClassList.size-1)
    }

    private fun addObjectJsonNode(node:Map.Entry<String,JsonNode>,index:Int){
        dataClassList.add(DataClass(node.key, mutableListOf()))
        val currLastIndex = dataClassList.lastIndex

        if(index >= 0){
            addPropertyToDataClass(index,"DataClass$currLastIndex",node.key)
        }

        node.value.fields().forEach {
            val action = jsonTypeToActionMapping[it.value.nodeType]
            action?.invoke(it,currLastIndex)
        }
    }

    private fun resolveJsonNumberType(num:JsonNode):String{
        return if(num.toPrettyString().contains(".")) "Double" else "Int"
    }

    private fun resolveJsonArrayType(arr:JsonNode):String{
        with(arr.first()) {
            return when(this.nodeType) {
                JsonNodeType.STRING -> "List<String>"
                JsonNodeType.NUMBER -> "List<${resolveJsonArrayType(this)}>"
                JsonNodeType.BOOLEAN -> "List<Boolean>"
                else -> "List<Any>"
            }
        }
    }

    private fun addPropertyToDataClass(index:Int,type:String,name:String){
        dataClassList[index].properties.add(DataClassProperty(type,name))
    }

    override fun toString(): String {
        val builder = StringBuilder()
        dataClassList.forEachIndexed { i, dataClass ->
            builder.append("data class ${if (i==0) topLevelClassName else "DataClass$i"}(\n")
            dataClass.properties.forEach {
                builder.append("\tval ${it.name}:${it.type},\n")
            }
            builder.setLength(builder.length-2)
            builder.append("\n)\n\n")
        }
        return builder.toString()
    }
}

data class DataClass(
    val name:String,
    var properties:MutableList<DataClassProperty>
)

data class DataClassProperty(
    val type:String,
    val name:String
)