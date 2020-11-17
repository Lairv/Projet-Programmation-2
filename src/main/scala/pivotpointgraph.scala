import scala.collection.mutable.ArrayBuffer
import scala.io.Source

class PivotPointGraph(mapName : String)
{
	var m_mapName = mapName
	var m_finals = new ArrayBuffer[Int]()
	var m_initials = new ArrayBuffer[Int]()
	var m_graph = new ArrayBuffer[(Vect,Vect,ArrayBuffer[Int])]()

	def init() : Unit =
	{	
		var lines = Source.fromFile("src/main/resources/"+m_mapName+".pvpoints.txt").getLines.toArray
		for (i <- lines(0).split(" "))
		{
			m_initials += i.toInt
		}
		for (i <- lines(1).split(" "))
		{
			m_finals += i.toInt
		}
		for (k <- 2 to (lines.length - 1) by 2)
		{
			var gates = lines(k).split(" ")
			var t = new ArrayBuffer[Int]()
			if (lines(k+1) != "")
			{
				for (i <- lines(k+1).split(" "))
				{
					t += i.toInt
				}
			}
			var inter = (new Vect(gates(0).toInt,gates(1).toInt),new Vect(gates(2).toInt,gates(3).toInt),t)
			m_graph += inter
		}
	}

	def printGraph() : Unit =
	{
		print("Etats initiaux : ")
		for (k <- m_initials) { print(k + " ") }
		print("Etats finaux : ")
		print("\n")
		for (k <- m_finals) { print(k + " ") }
		print("\n")
		for (k <- m_graph)
		{
			println("x : " + k._1.x + ", y : " + k._1.y + ", x : " + k._2.x + ", y : " + k._2.y)
			for (i <- k._3)
			{
				print(i + " ")
			}
			print("\n")
		}
	}

	def isInitial(i:Int) : Boolean =
	{
		var b = false
		for (k <- m_initials)
		{
			if (k == i) {b = true}
		}
		return b
	}

	def isFinal(i:Int) : Boolean =
	{
		var b = false
		for (k <- m_finals)
		{
			if (k == i) {b = true}
		}
		return b
	}
}
