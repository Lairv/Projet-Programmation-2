import javax.imageio.ImageIO
import scala.math
import java.awt.{Graphics2D,Color,Font}
import java.awt.geom.AffineTransform

trait Ennemy extends Entity
{
	var m_target:Vect
	var m_currPivP:Int
	var m_expReward:Int
	var m_goldReward:Int
	var m_lifeCost:Int
	var m_type = "ennemy"
	var m_dmg = 10
	var m_rotationSpeed = math.Pi / 360
	
	def init(g:Game):Unit =
	{
		g.m_grid.nextPivotPoint(-1) match
		{
			case (_,None) =>
				println("Impossible")
			case (a,Some(p)) => 
				m_pos = p
				m_target = p
				m_currPivP = a
		}
	} 
	
	def resetStats():Unit =
	{
		m_speed = m_baseSpeed
	}

	def customEffect(g:Game):Unit = {}

	def update(g:Game):Unit =
	{
		m_rotation += m_rotationSpeed
		customEffect(g)
		if ((m_target-m_pos).length < m_speed/2.0)
		{
			g.m_grid.nextPivotPoint(m_currPivP) match
			{
				case (_,None) =>
					g.m_player.m_hp -= m_lifeCost
					m_hp = 0
				case (a,Some(p)) =>
					m_target = p
					m_currPivP = a
			}
		}
		else
		{
			var dir = (m_target - m_pos)
			if (m_speed >= dir.length)
			{
				m_pos = m_pos + dir
			}
			else
			{
				m_pos = m_pos + dir*(m_speed/dir.length)
			}
		}
		resetStats()
	}
}

class YSquare extends Ennemy
{
	var m_sprite = ImageIO.read(getClass().getResource("YellowSquare.png"))
	var m_maxHp = 20
	var m_hp = 20
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(35,35)
	var m_radius = 30
	var m_target = new Vect(200,200)
	var m_speed = 2.0
	var m_baseSpeed = 2.0
	var m_currPivP = 0
	var m_rotation = 0
	var m_expReward=10
	var m_goldReward=10
	var m_lifeCost=1

}

class RTriangle extends Ennemy
{
	var m_sprite = ImageIO.read(getClass().getResource("RedTriangle.png"))
	var m_maxHp = 10
	var m_hp = 10
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(37,45)
	var m_radius = 25
	var m_target = new Vect(200,200)
	var m_speed = 3.0
	var m_baseSpeed = 2.0
	var m_currPivP = 0
	var m_rotation = 0
	var m_expReward=25
	var m_goldReward=25
	var m_lifeCost=1

}

class BPentagon extends Ennemy
{
	var m_sprite = ImageIO.read(getClass().getResource("BluePentagon.png"))
	var m_maxHp = 150
	var m_hp = 150
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(55,55)
	var m_radius = 55
	var m_target = new Vect(200,200)
	var m_speed = 1.5
	var m_baseSpeed = 1.5
	var m_currPivP = 0
	var m_rotation = 0
	var m_expReward = 100
	var m_goldReward = 100
	var m_lifeCost = 5

	var m_buffRange = 200
	var m_buffSpeedValue = 2
	
	override def customEffect(g:Game)
	{
		for (k <- g.getInRange(this,m_buffRange))	
		{
			k.m_speed += m_buffSpeedValue
		}
	}

	override def customAnimation(g:Graphics2D):Unit =
	{
		g.setColor(Color.blue)
		g.drawOval(
			m_pos.x - m_buffRange,
			m_pos.y - m_buffRange,
			2*m_buffRange,
			2*m_buffRange
		)
	}
}

class GPentagon extends Ennemy
{
	var m_sprite = ImageIO.read(getClass().getResource("GreenPentagon.png"))
	var m_maxHp = 150
	var m_hp = 150
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(55,55)
	var m_radius = 55
	var m_target = new Vect(200,200)
	var m_speed = 1.5
	var m_baseSpeed = 1.5
	var m_currPivP = 0
	var m_rotation = 0
	var m_expReward = 100
	var m_goldReward = 100
	var m_lifeCost = 5

	var m_buffRange = 200
	var m_buffRegenValue = 2
	var m_buffCd = 10
	var m_buffTick = 0
	
	override def customEffect(g:Game)
	{
		if (m_buffTick == 0)
		{
			for (k <- g.getInRange(this,m_buffRange))	
			{
				k.m_hp = math.min(k.m_maxHp,k.m_hp + m_buffRegenValue)
			}
		}
		m_buffTick += 1
		m_buffTick %= m_buffCd
	}

	override def customAnimation(g:Graphics2D):Unit =
	{
		g.setColor(Color.green)
		g.drawOval(
			m_pos.x - m_buffRange,
			m_pos.y - m_buffRange,
			2*m_buffRange,
			2*m_buffRange
		)
	}
}

class APentagon extends Ennemy
{
	var m_sprite = ImageIO.read(getClass().getResource("AlphaPentagon.png"))
	var m_maxHp = 3000
	var m_hp = 3000
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(115,115)
	var m_radius = 115
	var m_target = new Vect(200,200)
	var m_speed = 1.5
	var m_baseSpeed = 1.5
	var m_currPivP = 0
	var m_rotation = 0
	var m_expReward = 3000
	var m_goldReward = 3000
	var m_lifeCost = 100
}
