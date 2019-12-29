import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.lang.StringBuilder

class DataClassCreator(val sourceFile: File){

    val dataClassBuilder = StringBuilder()

    val jsonTypeToActionMapping = mapOf<JsonNodeType,(JsonNode) -> String>(
        JsonNodeType.ARRAY to { str:JsonNode -> "String" },
        JsonNodeType.BOOLEAN to {bool:JsonNode -> "Boolean" },
        JsonNodeType.NUMBER to ::resolveIntOrDouble,
        JsonNodeType.ARRAY to ::resolveArrayType,
        JsonNodeType.OBJECT to ::addObjectJsonNode
    )

    init {
        makeDataClass()
    }

    private fun makeDataClass() {
        val mapper = jacksonObjectMapper()
        val root: JsonNode = mapper.readTree(sourceFile)


    }

    private fun addObjectJsonNode(node:JsonNode):String{
        node.fields().forEach {

        }
    }

    private fun resolveIntOrDouble(num:JsonNode):String{

    }

    private fun resolveArrayType(arr:JsonNode):String{

    }
}