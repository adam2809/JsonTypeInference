import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.lang.StringBuilder

class DataClassCreator(private val root:JsonNode, private val topLevelClassName: String){
    constructor(sourceFile:File,topLevelClassName: String):this(jacksonObjectMapper().readTree(sourceFile),topLevelClassName)

    val dataClassList = mutableListOf<DataClass>()

    val jsonTypeToActionMapping = mapOf(
        JsonNodeType.STRING to ::resolveString,
        JsonNodeType.BOOLEAN to ::resolveBool,
        JsonNodeType.NUMBER to ::resolveIntOrDouble,
        JsonNodeType.ARRAY to ::resolveArrayType,
        JsonNodeType.OBJECT to ::addObjectJsonNode
    )

    init {
        makeDataClass()
    }

    private fun makeDataClass() {
        addObjectJsonNode(mapOf(topLevelClassName to root).entries.first(),-1)
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

    private fun resolveIntOrDouble(num:Map.Entry<String,JsonNode>,index:Int){
        val numStr = num.value.toPrettyString()

        val numType = if (numStr.contains(".")) "Double" else "Int"
        addPropertyToDataClass(index,numType,num.key)
    }

    private fun resolveArrayType(arr:Map.Entry<String,JsonNode>,index:Int){

    }

    private fun resolveBool(bool:Map.Entry<String,JsonNode>,index:Int){
        addPropertyToDataClass(index,"Boolean",bool.key)
    }

    private fun resolveString(str:Map.Entry<String,JsonNode>,index:Int){
        addPropertyToDataClass(index,"String",str.key)
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