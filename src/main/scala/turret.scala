import javax.imageio.ImageIO
import scala.math

trait Turret extends Entity
{
	var m_bulletPenetration:Int
	var m_bulletDamage:Int
	var m_bulletSpeed:Int
	var m_reload:Int
	var m_range:Int
	var m_upgrade:String
	var m_exp:Int
	var m_cannonList:Array[Cannon]
	var m_type = "turret"
	
	def rotateToward(e : Entity):Unit =
	{	
		var inter : Vect = (e.m_pos-m_pos)
		if (inter.y > 0)
		{
			m_rotation = math.acos(inter.x/inter.length)
		}
		else
		{
			m_rotation = - math.acos(inter.x/inter.length)		
		}
	}
	
	def init(g : Game):Unit =
	{
		for (i <- m_cannonList)
		{
		i.refresh()
		}
	}
	
	def update(g : Game):Unit =
	{
		g.giveTargetToTurret(this) match
		{
			case None =>
				{}
			case Some(e) =>
				// La tourelle se tourne vers la cible
				rotateToward(e)
				
				// Les cannons se tournent vers la cible
				for (i <- m_cannonList)
				{
					i.getNewDirection(this)
					i.shootAmmo(g,this,e)
				}
		}
	}
	def gainExp(c : Int) =
	{
		m_exp += c
	}
}

class Tank(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("tank.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(60,60)

	var m_cannonList = Array(new Cannon(new Vect(40,0), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(25,25), 14, 1, 1, 1.0, 0))

	var m_radius = 60
	var m_bulletPenetration = 10
	var m_bulletDamage = 40
	var m_bulletSpeed = 25
	var m_reload = 30
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "tank"
	var m_exp = 0
}

class Twin(p : Vect)extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("twin.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(60,60)

	var m_cannonList = Array(
				new Cannon(new Vect(40,15), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(25,25), 14, 1, 1, 1.0, 0),
				new Cannon(new Vect(40,-15), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(25,25), 14, 1, 1, 1.0, 0.5))

	var m_radius = 60
	var m_bulletPenetration = 10
	var m_bulletDamage = 30
	var m_bulletSpeed = 25
	var m_reload = 30
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "twin"
	var m_exp = 0
}
