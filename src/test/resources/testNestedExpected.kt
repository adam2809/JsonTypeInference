data class Test(
	val name:String,
	val surname:String,
	val id:Int,
	val nested:DataClass1,
	val visable:Boolean,
	val money:Double,
	val fryty:List<String>,
	val nestednestednested:DataClass2
)

data class DataClass1(
	val inanest:Boolean,
	val level:Int
)

data class DataClass2(
	val num:Int,
	val nestednested:DataClass3
)

data class DataClass3(
	val iuy:Int,
	val dsa:Int
)

