import javax.imageio.ImageIO
import scala.math

trait Ennemy extends Entity
{
	var m_reward:Int
	var m_speed:Double
	var m_target:Vect
	var m_currPivP:Int
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
	var m_sprite = ImageIO.read(getClass().getResource("ysquare.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(25,25)
	var m_radius = 25
	var m_target = new Vect(200,200)
	var m_reward = 1
	var m_speed = 2.0
	var m_currPivP = 0
	var m_rotation = 0

}

class RTriangle extends Ennemy
{
	var m_sprite = ImageIO.read(getClass().getResource("rtriangle.png"))
	var m_maxHp = 50
	var m_hp = 50
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(35,35)
	var m_radius = 15
	var m_target = new Vect(200,200)
	var m_reward = 1
	var m_speed = 4.0
	var m_currPivP = 0
	var m_rotation = 0

}
