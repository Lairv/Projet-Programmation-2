import swing._
import swing.event._
import java.awt.{Graphics2D,Color}
import java.awt.geom.AffineTransform
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer

class booleanMatrix(cols:Int, rows:Int)
{
	val m_cols = cols
	val m_rows = rows
	val m_arr = new Array[Boolean](m_cols*m_rows)
	
	def at(r:Int, c:Int):Boolean =
	{
		return m_arr(m_cols * r + c)
	}
	
	def ch(r:Int, c:Int, v:Boolean):Unit =
	{
		m_arr(m_cols*r+c) = v	
	}
}

class Grid(cols:Int, rows:Int, cellSize:Int)extends Component
{
	val m_cols = cols
	val m_rows = rows
	val m_cellSize = cellSize
	var m_map = new booleanMatrix(m_cols,m_rows)
	var m_entityMap = new booleanMatrix(m_cols,m_rows)
	var m_entities = ArrayBuffer[Entity]()
	var m_selected : Option[Vect] = None
	
	var m_random = scala.util.Random
	
	// A modifier en fonction de la carte 
	var m_pivotPoints = Array(Array(new Vect(-1,3),new Vect(-1,5)),
							  Array(new Vect(15,3),new Vect(13,5)),
							  Array(new Vect(13,m_rows),new Vect(15,m_rows)))
	
	preferredSize = new Dimension(m_cols * cellSize, m_rows * cellSize)
	
	def nextPivotPoint (currPivP:Int) =
	{
		if (currPivP == m_pivotPoints.length - 1)
		{
			None
		}
		else
		{
			var t = m_random.nextFloat.toDouble
			Some ((m_pivotPoints(currPivP+1)(0)*m_cellSize)*t+(m_pivotPoints(currPivP+1)(1)*m_cellSize)*(1-t) + (new Vect(m_cellSize/2,m_cellSize/2)))
		}
	}
	
	def getPosInGrid (p:Vect) : Vect =
	{
		return new Vect(p.x/m_cellSize,p.y/m_cellSize)
	}
	
	def isAvailable(p : Vect) : Boolean =
	{
		!(m_map.at(p.y,p.x)) && !(m_entityMap.at(p.y,p.x))
	}
	
	def putTurret(p : Vect) : Unit =
	{
		m_entityMap.ch(p.y,p.x,true)
	}
	
	def initGrid () =
	{
		for (i <- 0 to m_rows-1)
		{
			for (j <- 0 to m_cols-1)
			{
				m_map.ch(i,j,false)
				m_entityMap.ch(i,j,false)
			}
		}
		
		// A changer en fonction de la map qu'on veut faire
		for (i <- 3 to 5)
		{
			for (j <- 0 to 15)
			{
				m_map.ch(i,j,true)
			}
		}
		for (i <- 6 to 9)
		{
			for (j <- 13 to 15)
			{
				m_map.ch(i,j,true)
			}
		}
	}
	
	def drawGrid(g : Graphics2D) =
	{
		// Dessine les cases
		g.setColor(Color.gray)
		for (i <- 0 to m_rows-1)
		{
			for (j <- 0 to m_cols-1)
			{
				if (!m_map.at(i,j))
				{
					g.fillRect(j*m_cellSize, i*m_cellSize, m_cellSize, m_cellSize)
				}
			}
		}
	
		// Affiche la case sélectionnée
		m_selected match
		{
			case None => {}
			case Some(p) => 
				g.setColor(Color.green)
				g.fillRect(p.x*m_cellSize, p.y*m_cellSize, m_cellSize, m_cellSize)
		}
		
		// Dessine les lignes
		g.setColor(Color.black)
		for (i <- 1 to m_cols)
		{
			g.drawLine(i*m_cellSize, 0, i*m_cellSize, rows * cellSize)
		}
		for (i <- 1 to m_rows)
		{
			g.drawLine(0, i*m_cellSize, cols * m_cellSize, i*m_cellSize)
		}
		
	}
	
	def setEntities(entityList : ArrayBuffer[Entity]) : Unit =
	{
		m_entities = entityList
	}
	
	def drawTurretCannons(g : Graphics2D, t : Turret) : Unit =
	{
		g.setColor(Color.red)
		for (i <- t.m_cannonList)
		{
			g.drawOval(t.m_pos.x + i.m_currentOffset.x -5,
					   t.m_pos.y + i.m_currentOffset.y -5,
					   10 , 10)
			g.drawLine(t.m_pos.x + i.m_currentOffset.x,
					   t.m_pos.y + i.m_currentOffset.y,
					   t.m_pos.x + i.m_currentOffset.x + i.m_currentDirection.x,
					   t.m_pos.y + i.m_currentOffset.y + i.m_currentDirection.y)
		}
	}
	
	def drawLifeBar(g : Graphics2D, e : Entity) : Unit =
	{
		g.setColor(Color.black)
		g.fillRect(e.m_pos.x - e.m_maxHp/2,
				   e.m_pos.y - e.m_offset.y - 10,
				   e.m_maxHp, 10)
		g.setColor(Color.red)
		g.fillRect(e.m_pos.x-e.m_maxHp/2 + 2,
				   e.m_pos.y - e.m_offset.y + 2 - 10,
				   math.min(math.max(e.m_hp,0),e.m_maxHp - 4),
				   6)
	}
	
	def drawTurretRange(g : Graphics2D, t : Turret) : Unit =
	{
		m_selected match
		{
			case None =>
				{}
			case Some(p) =>
				if (getPosInGrid(t.m_pos) == p)
				{
					g.setColor(Color.green)
					g.drawOval(t.m_pos.x - t.m_range,
							 t.m_pos.y - t.m_range,
							 2*t.m_range,
							 2*t.m_range)
				}
		}
	}
	
	def drawEntities(g : Graphics2D) : Unit =
	{
		for (i <- m_entities)
		{
			if (i.m_rotation == 0) // Si l'entité n'est pas tournée on ne calcule pas de rotation, petit gain de performances
			{
				g.drawImage(i.m_sprite,i.m_pos.x - i.m_offset.x,i.m_pos.y - i.m_offset.y, null)
			}
			else
			{
				var at : AffineTransform = new AffineTransform()
				
				at.translate(i.m_pos.x,i.m_pos.y)
				at.rotate(i.m_rotation)
				at.translate(-i.m_offset.x, -i.m_offset.y)
				
				g.drawImage(i.m_sprite,at,null)
			}
			
			// Partie pour dessiner les barres de vie
			if (i.m_type == "ennemy")
			{
				drawLifeBar(g,i)
			}
			
			// Partie pour dessiner les cannons + le cercle de portée
			if (i.m_type == "turret")
			{
				drawTurretRange(g,i.asInstanceOf[Turret])
				drawTurretCannons(g,i.asInstanceOf[Turret])
			}
		}
	}
	
	override def paint(g : Graphics2D)
	{
		// On dessine la grille
		drawGrid(g)
		// On dessine les entitées
		drawEntities(g)
	}
	
}
