import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Tests{
    @Test
    fun testNestedMultipleTimes(){
        val expected = this::class.java.getResource("testNestedExpected.kt").readText()

        val creator = DataClassCreator(File(this::class.java.getResource("testNested.json").toURI()),"Test")
        assertEquals(expected,creator.toString())
    }

    @Test
    fun testTopLevel(){
        val expected = this::class.java.getResource("testTopLevelExpected.kt").readText()

        val creator = DataClassCreator(File(this::class.java.getResource("testTopLevel.json").toURI()),"TestTopLevel")
        assertEquals(expected,creator.toString())
    }
}