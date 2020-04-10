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
	var m_dmg = 10
	var m_autoTarget = true
	var m_rotationSpeed = math.Pi / 360

	var m_baseSpeed = 0
	var m_speed = 0
	
	var m_droneCount = 0
	
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
		if (!m_autoTarget)
		{
			m_rotation += m_rotationSpeed
		}
		g.giveTargetToTurret(this) match
		{
			case None =>
				{}
			case Some(e) =>
				// La tourelle se tourne vers la cible
				if (m_autoTarget)
				{
					rotateToward(e)
				}
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

class Smasher(p : Vect) extends Entity
{
	var m_sprite = ImageIO.read(getClass().getResource("Smasher.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(40,40)

	var m_radius = 35
	var m_rotation = 0
	


	var m_type = "standingturret"
	var m_dmg = 10
	var m_autoTarget = false
	var m_rotationSpeed = math.Pi / 360

	var m_baseSpeed = 0
	var m_speed = 0
	
	def init(g:Game) = {}

	def update(g : Game):Unit =
	{
		m_rotation += m_rotationSpeed
		for (e <- g.getCollisions(this,"ennemy"))
		{
			if (m_hp > 0)
			{
				e.rmvHp(m_dmg)
				if (e.m_hp <= 0 && e.m_hp > -m_dmg)
				{
					g.m_player.m_gold += e.asInstanceOf[Ennemy].m_goldReward
				}
			}
			m_hp -= e.m_dmg
		}
	}
}

class Tank(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Tank.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(55,55)

	var m_cannonList = Array(new Cannon(new Vect(50,0), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0))

	var m_radius = 30
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "tank"
	var m_exp = 0
}

class Twin(p : Vect)extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Twin.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(70,70)

	var m_cannonList = Array(
				new Cannon(new Vect(55,-18), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(55,18), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.5))

	var m_radius = 35
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "twin"
	var m_exp = 0
}

class Assassin(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Assassin.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(70,70)

	var m_cannonList = Array(new Cannon(new Vect(55,0), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0))

	var m_radius = 30
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "assassin"
	var m_exp = 0
}

class Octotank(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Octotank.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(70,70)

	var m_cannonList = Array(
				new Cannon(new Vect(55,0), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(-55,0), new Vect(-50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(0,-55), new Vect(0,-50), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(0,55), new Vect(0,50), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(-40,-40), new Vect(-50,-50), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(40,40), new Vect(50,50), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(40,-40), new Vect(50,-50), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(-40,40), new Vect(-50,50), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0)
	)

	var m_radius = 35
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "octotank"
	var m_exp = 0
	m_autoTarget = false
}

class Pentashot(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Pentashot.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(80,80)

	var m_cannonList = Array(
				new Cannon(new Vect(60,0), new Vect(60,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(50,-22), new Vect(50,-22), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.9),
				new Cannon(new Vect(50,22), new Vect(50,22), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.9),
				new Cannon(new Vect(30,-35), new Vect(30,-35), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.8),
				new Cannon(new Vect(30,35), new Vect(30,35), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.8)
	)

	var m_radius = 40
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "pentashot"
	var m_exp = 0
}

class Quadtank(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Quadtank.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(65,65)

	var m_cannonList = Array(
				new Cannon(new Vect(50,0), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(-50,0), new Vect(-50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(0,50), new Vect(0,50), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(0,-50), new Vect(0,-50), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0)
	)

	var m_radius = 35
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "quadtank"
	var m_exp = 0
	m_autoTarget = false
}

class Sniper(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Sniper.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(75,75)

	var m_cannonList = Array(
				new Cannon(new Vect(60,0), new Vect(60,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0)
	)

	var m_radius = 35
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "sniper"
	var m_exp = 0
}

class Spreadshot(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Spreadshot.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(70,70)

	var m_cannonList = Array(
				new Cannon(new Vect(55,0), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(56,-16), new Vect(56,-16), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.9),
				new Cannon(new Vect(56,16), new Vect(56,16), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.9),
				new Cannon(new Vect(45,-25), new Vect(45,-25), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.8),
				new Cannon(new Vect(45,25), new Vect(45,25), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.8),
				new Cannon(new Vect(35,-35), new Vect(35,-35), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.7),
				new Cannon(new Vect(35,35), new Vect(35,35), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.7),
				new Cannon(new Vect(8,-40), new Vect(8,-40), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.6),
				new Cannon(new Vect(8,40), new Vect(8,40), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.6)
	)

	var m_radius = 35
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "spreadshot"
	var m_exp = 0
}

class Stalker(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Sniper.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(70,70)

	var m_cannonList = Array(
				new Cannon(new Vect(50,0), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0)
	)

	var m_radius = 30
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "stalker"
	var m_exp = 0
}

class Tripleshot(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Tripleshot.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(70,70)

	var m_cannonList = Array(
				new Cannon(new Vect(55,0), new Vect(55,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(40,-40), new Vect(40,-40), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(40,40), new Vect(40,40), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0)
	)

	var m_radius = 35
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "tripleshot"
	var m_exp = 0
}

class Triplet(p : Vect) extends Turret
{
	var m_sprite = ImageIO.read(getClass().getResource("Triplet.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = p
	var m_offset = new Vect(70,70)

	var m_cannonList = Array(
				new Cannon(new Vect(55,0), new Vect(55,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0),
				new Cannon(new Vect(45,-20), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.5),
				new Cannon(new Vect(45,20), new Vect(50,0), this, "bullet", "basicbullet.png", new Vect(19,19), 17, 1, 1, 1.0, 0.5)
	)

	var m_radius = 35
	var m_bulletPenetration = 10
	var m_bulletDamage = 10
	var m_bulletSpeed = 10
	var m_reload = 40
	var m_range = 400
	var m_rotation = 0
	var m_upgrade = "triplet"
	var m_exp = 0
}
