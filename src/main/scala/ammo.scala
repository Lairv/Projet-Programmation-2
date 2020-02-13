import javax.imageio.ImageIO
import scala.math

trait Ammo extends Entity
{
	var m_target:Entity
	var m_speed:Int
	var m_dmg:Int
	
	def init(g:Game):Unit = {}
	def update(g:Game)
	{
		var inter = m_target.m_pos-m_pos
		if (inter.length > (m_target.m_radius + m_radius))
		{
			m_pos = m_pos + inter*(m_speed/inter.length)
		}
		else
		{
			m_target.rmvHp(m_dmg)
			m_hp = 0
		}
	}
}

class BasicAmmo(p : Vect, target : Entity) extends Ammo
{
	var m_sprite = ImageIO.read(getClass().getResource("basicammo.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(25,25)
	var m_type = "ammo"
	var m_rotation = 0.0
	var m_radius = 14
	var m_target = target
	var m_speed = 25
	var m_dmg = 30
}
