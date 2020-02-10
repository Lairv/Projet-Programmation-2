import javax.imageio.ImageIO;

trait Ennemy extends Entity
{
	var m_reward:Int
	var m_speed:Int
	var m_target:Vect
	var m_currPivP:Int
}

class YSquare extends Ennemy
{
	var m_sprite = ImageIO.read(getClass().getResource("ysquare.png"))
	var m_maxHp = 100
	var m_hp = 100
	var m_pos = new Vect(0,0)
	var m_offset = new Vect(25,25)
	var m_target = new Vect(200,200)
	var m_reward = 1
	var m_speed = 5
	var m_currPivP = 0
	
	def init(g:Game):Unit =
	{
		g.m_grid.nextPivotPoint(-1) match
		{
			case None =>
				println("Impossible")
			case Some(p) => 
				m_pos = p 
		}
	} 
	
	def update(g:Game):Unit =
	{
		if (g.m_grid.getPosInGrid(m_pos+m_offset) == m_target)
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
			m_pos = m_pos + dir*(m_speed.toFloat/dir.length.toFloat)
		}
	}
}
