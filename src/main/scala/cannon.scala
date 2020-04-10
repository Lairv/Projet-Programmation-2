import scala.math

class Cannon (offset : Vect, direction : Vect, source : Turret, ammo : String, ammoSprite : String, ammoOffset : Vect, ammoRadius : Int, damageMultiplier : Double, penetrationMultiplier : Double, reloadMultiplier:Double, initialReloadMultiplier:Double)
{
	var m_source : Turret = source

	var m_originalOffset : Vect = offset // Offset par rapport au centre de gravité de la tourelle
	var m_originalDirection : Vect = direction // Direction de tir du cannon dans l'état initial de la tourelle
	var m_currentOffset : Vect = offset // Position actuelle du cannon après rotation
	var m_currentDirection : Vect = direction // Direction actuelle du cannon après rotation

	var m_ammoType : String = ammo
	var m_ammoSprite : String = ammoSprite
	var m_ammoOffset : Vect = ammoOffset
	var m_ammoRadius : Int = ammoRadius
	
	var m_damage:Int = (source.m_bulletDamage*damageMultiplier).toInt
	var m_penetration:Int = (source.m_bulletPenetration*penetrationMultiplier).toInt
	var m_reloadSpeed:Int = (source.m_reload*reloadMultiplier).toInt
	var m_currentReload:Int = (source.m_reload*initialReloadMultiplier).toInt

	def refresh() : Unit =
	{
		m_currentReload = (source.m_reload*initialReloadMultiplier).toInt
		m_damage = (source.m_bulletDamage*damageMultiplier).toInt
		m_penetration = (source.m_bulletPenetration*penetrationMultiplier).toInt
		m_reloadSpeed = (source.m_reload*reloadMultiplier).toInt
	}		
	
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
			if (ammo=="bullet")
			{
				g.addEntity(new Bullet(source.m_pos+m_currentOffset, m_ammoSprite, m_ammoOffset, m_ammoRadius, target, m_source, m_damage, m_penetration,m_currentDirection))
			}
		}
		m_currentReload = (m_currentReload + 1) % m_reloadSpeed
	}
}
