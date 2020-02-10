import swing._
import java.awt.{Graphics2D,Color}
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
	var m_entities = ArrayBuffer[Entity]()
	
	var m_random = scala.util.Random
	
	// A modifier en fonction de la carte 
	var m_pivotPoints = Array(Array(new Vect(-1,3),new Vect(-1,4),new Vect(-1,5)),
							  Array(new Vect(15,3),new Vect(14,4),new Vect(13,5)),
							  Array(new Vect(13,m_rows+1),new Vect(14,m_rows+1),new Vect(15,m_rows+1)))
	
	preferredSize = new Dimension(m_cols * cellSize, m_rows * cellSize)
	
	def nextPivotPoint (currPivP:Int) =
	{
		if (currPivP == m_pivotPoints.length - 1)
		{
			None
		}
		else
		{
			Some (m_pivotPoints(currPivP+1)(m_random.nextInt(m_pivotPoints(currPivP+1).length))*m_cellSize)
		}
	}
	
	def getPosInGrid (p:Vect) : Vect =
	{
		return new Vect((p.x/m_cellSize)*m_cellSize, (p.y/m_cellSize)*m_cellSize)
	}
	
	def initGrid () =
	{
		for (i <- 0 to m_rows-1)
		{
			for (j <- 0 to m_cols-1)
			{
				m_map.ch(i,j,false)
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
					g.fillRect(j*cellSize, i*cellSize, cellSize, cellSize)
				}
			}
		}
	
		// Dessine les lignes
		g.setColor(Color.black)
		for (i <- 1 to m_cols)
		{
			g.drawLine(i*m_cellSize, 0, i*m_cellSize, rows * cellSize)
		}
		for (i <- 1 to m_rows)
		{
			g.drawLine(0, i*m_cellSize, cols * cellSize, i*m_cellSize)
		}
	}
	
	def setEntities(entityList : ArrayBuffer[Entity]) : Unit =
	{
		m_entities = entityList
	}
	
	def drawEntities(g : Graphics2D) : Unit =
	{
		for (i <- m_entities)
		{
			g.drawImage(i.m_sprite,i.m_pos.x,i.m_pos.y, null)
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
