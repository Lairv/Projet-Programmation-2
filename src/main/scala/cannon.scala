import scala.math

class Cannon (offset : Vect, direction : Vect, ammo : String)
{
	var m_originalOffset : Vect = offset// Offset par rapport au centre de gravité de la tourelle
	var m_originalDirection : Vect = direction // Direction de tir du cannon dans l'état initial de la tourelle
	var m_currentOffset : Vect = offset // Position actuelle du cannon après rotation
	var m_currentDirection : Vect = direction // Direction actuelle du cannon après rotation
	var m_ammoType : String = ammo // type à modifier potentiellement
	
	var m_reloadSpeed:Int = 30
	var m_currentReload:Int = 0
	
	def getNewDirection(t : Turret) : Unit =
	{
		var matRot = new Matrice(math.cos(t.m_rotation), -math.sin(t.m_rotation),
							 math.sin(t.m_rotation),  math.cos(t.m_rotation))
		m_currentDirection = matRot * m_originalDirection
		m_currentOffset = matRot * m_originalOffset
	}
	
	def shootAmmo(g : Game, source : Turret, target : Entity) : Unit =
	{
		if (m_currentReload == 0)
		{
			g.addEntity(new BasicAmmo(source.m_pos+m_currentOffset, target)) // Ligne à modifiée, il faut prendre en compte le type de munitions que tire le cannon
		}
		m_currentReload = (m_currentReload + 1) % m_reloadSpeed
	}
}

