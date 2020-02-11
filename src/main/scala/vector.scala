import scala.math

class Vect(m_x:Int,m_y:Int)
{
	var x = m_x
	var y = m_y
	
	def +(that : Vect):Vect =
	{
		new Vect(x+that.x,y+that.y)
	}
	def ==(that : Vect):Boolean =
	{
		return ((x == that.x) && (y == that.y))
	}
	def *(lambda : Double):Vect =
	{
		new Vect((x*lambda).toInt,(y*lambda).toInt)
	}
	def -(that : Vect):Vect =
	{
		new Vect(x - that.x, y - that.y)
	}
	def length():Double =
	{
		(math.sqrt(x*x+y*y))
	}
}