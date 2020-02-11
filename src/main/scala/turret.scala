import javax.imageio.ImageIO
import scala.math

trait Turret extends Entity
{
	var m_reloadSpeed:Int
	var m_currentReload:Int
	var m_bulletPenetration:Int
	var m_bulletDamage:Int
	var m_bulletSpeed:Int
	var m_range:Int
	var m_evolution:String
	var m_type = "turret"
}

class Tank extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("tank.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(50,50)

	var m_reloadSpeed = 10
	var m_currentReload = 0
	var m_bulletPenetration = 0
	var m_bulletDamage = 0
	var m_bulletSpeed = 0
	var m_range = 500
	var m_rotation = 0
	var m_evolution = "tank"
	
	def setPosition(p : Vect) =
	{
		m_pos = p
	}
	
	def rotateToward(e : Entity):Unit =
	{	
		var inter : Vect = (m_pos-e.m_pos)
		println(inter.x + " " + inter.y)
		if (inter.x < 0 && inter.y < 0)
		{
			m_rotation = math.Pi + math.atan(inter.y/inter.x)
		}
		if (inter.x < 0 && inter.y > 0)
		{
			m_rotation = math.Pi - math.atan(inter.y/inter.x)
		}
		else
		{
			m_rotation = math.atan(inter.y/inter.x)
		}
	}
	
	def init(g : Game):Unit = {}
	def update(g : Game):Unit =
	{
		g.giveTargetToTurret(this) match
		{
			case None =>
				{}
			case Some(e) =>
				rotateToward(e)
				
				// A FAIRE : Tirer sur la cible 
		}
	}
}
