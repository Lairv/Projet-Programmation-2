import scala.math

class Cannon (offset : Vect, direction : Vect, ammo : String)
{
	var m_originalOffset : Vect = offset// Offset par rapport au centre de gravité de la tourelle
	var m_originalDirection : Vect = direction // Direction de tir du cannon dans l'état initial de la tourelle
	var m_currentOffset : Vect = offset // Position actuelle du cannon après rotation
	var m_currentDirection : Vect = direction // Direction actuelle du cannon après rotation
	var m_ammoType : String = ammo // type à modifier potentiellement
	
	def getNewDirection(t : Turret) : Unit =
	{
		var matRot = new Matrice(math.cos(t.m_rotation), -math.sin(t.m_rotation),
							 math.sin(t.m_rotation),  math.cos(t.m_rotation))
		m_currentDirection = matRot * m_originalDirection
		m_currentOffset = matRot * m_originalOffset
	}
	
	def shotAmmo() : Unit =
	{
	
	}
}

