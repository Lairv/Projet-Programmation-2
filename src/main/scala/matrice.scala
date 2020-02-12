class Matrice (a : Double, b : Double, c : Double, d : Double)
{
	var x0 = a
	var x1 = b
	var x2 = c
	var x3 = d
	
	def *(v : Vect) : Vect =
	{
		new Vect((v.x*x0 + v.y*x1).toInt, (v.x*x2 + v.y*x3).toInt)
	}
}
