import javax.imageio.ImageIO
import scala.math

trait Ammo extends Entity
{
	var m_source:Turret
	var m_target:Entity
	var m_speed:Int
	var m_direction:Vect
	
	def init(g:Game):Unit = {}
}

class Bullet(p : Vect, sprite : String, offset : Vect, radius : Int, target : Entity, source : Turret, damage : Int, penetration : Int,direction : Vect) extends Ammo
{
	var m_sprite = ImageIO.read(getClass().getResource(sprite))
	var m_maxHp = penetration
	var m_hp = penetration
	var m_pos = p
	var m_offset = offset
	var m_type = "bullet"
	var m_rotation = 0.0
	var m_rotationSpeed = 0.0
	var m_radius = radius
	
	var m_source = source
	var m_target = target
	var m_speed = source.m_bulletSpeed
	var m_direction = direction
	var m_dmg = damage
	
	def update(g:Game)
	{
		m_pos = m_pos + m_direction * (m_speed/m_direction.length)
		for (e <- g.getCollisions(this))
		{
			if (m_hp > 0)
			{
				e.rmvHp(m_dmg)
				if (m_target.m_hp <= 0 && m_target.m_hp > -m_dmg)
				{
					m_source.gainExp(m_target.asInstanceOf[Ennemy].m_expReward)
					g.m_player.m_gold += m_target.asInstanceOf[Ennemy].m_goldReward
				}
			}
			m_hp -= e.m_dmg
		}
	}
}
