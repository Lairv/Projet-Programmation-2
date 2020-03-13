import javax.imageio.ImageIO
import scala.math

trait Ennemy extends Entity
{
	var m_speed:Double
	var m_target:Vect
	var m_currPivP:Int
	var m_expReward:Int
	var m_goldReward:Int
	var m_lifeCost:Int
	var m_type = "ennemy"
	
	def init(g:Game):Unit =
	{
		g.m_grid.nextPivotPoint(-1) match
		{
			case None =>
				println("Impossible")
			case Some(p) => 
				m_pos = p
				m_target = p
		}
	} 
	
	def update(g:Game):Unit =
	{
		m_rotation += math.Pi / 360
		if ((m_target-m_pos).length < m_speed/2.0)
		{
			g.m_grid.nextPivotPoint(m_currPivP) match
			{
				case None =>
					g.m_player.m_hp -= m_lifeCost
					m_hp = 0
				case Some(p) =>
					m_target = p
					m_currPivP += 1
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
	}
}

class YSquare extends Ennemy
{
	var m_sprite = ImageIO.read(getClass().getResource("YellowSquare.png"))
	var m_maxHp = 10
	var m_hp = 10
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(25,25)
	var m_radius = 25
	var m_target = new Vect(200,200)
	var m_speed = 2.0
	var m_currPivP = 0
	var m_rotation = 0
	var m_expReward=10
	var m_goldReward=10
	var m_lifeCost=1

}

class RTriangle extends Ennemy
{
	var m_sprite = ImageIO.read(getClass().getResource("RedTriangle.png"))
	var m_maxHp = 5
	var m_hp = 5
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(35,35)
	var m_radius = 15
	var m_target = new Vect(200,200)
	var m_speed = 3.0
	var m_currPivP = 0
	var m_rotation = 0
	var m_expReward=25
	var m_goldReward=25
	var m_lifeCost=1

}

class BPentagon extends Ennemy
{
	var m_sprite = ImageIO.read(getClass().getResource("BluePentagon.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(60,60)
	var m_radius = 60
	var m_target = new Vect(200,200)
	var m_speed = 1.5
	var m_currPivP = 0
	var m_rotation = 0
	var m_expReward = 100
	var m_goldReward = 100
	var m_lifeCost = 5
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
	var m_currPivP = 0
	var m_rotation = 0
	var m_expReward = 3000
	var m_goldReward = 3000
	var m_lifeCost = 100
}
