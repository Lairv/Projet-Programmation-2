import swing._
import swing.event._
import java.awt.{Graphics2D,Color,Font}
import java.awt.geom.AffineTransform
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

class booleanMatrix(cols:Int, rows:Int)
{
	val m_cols = cols
	val m_rows = rows
	val m_arr = new Array[Boolean](m_cols*m_rows)
	
	def at(x:Int, y:Int):Boolean =
	{
		return m_arr(m_cols * y + x)
	}
	
	def ch(x:Int, y:Int, v:Boolean):Unit =
	{
		m_arr(m_cols*y+x) = v	
	}
}

class Grid(mapName:String, cellSize:Int, game:Game)extends Component
{
	var m_cols = 0
	var m_rows = 0
	val m_cellSize = cellSize
	val m_mapName = mapName
	var m_map = new booleanMatrix(0,0)
	var m_entityMap = new booleanMatrix(0,0)
	var m_pivotPointGraph = new PivotPointGraph(m_mapName)
	
	var m_game = game
	
	var m_selected : Option[Vect] = None
	
	var m_random = scala.util.Random
	
	def barycentre(p1:Vect, p2:Vect) : Vect =
	{
		var t = m_random.nextFloat.toDouble
		return (p1*m_cellSize)*t+(p2*m_cellSize)*(1-t) + (new Vect(m_cellSize/2,m_cellSize/2))
	}
	
	def nextPivotPoint (currPivP:Int) : (Int,Option[Vect]) =
	{
		if (currPivP == -1)
		{	
			var t = m_random.nextInt(m_pivotPointGraph.m_initials.length)
			var u = m_pivotPointGraph.m_graph(m_pivotPointGraph.m_initials(t))
			(m_pivotPointGraph.m_initials(t) ,Some (barycentre(u._1,u._2)))
		}
		else if (m_pivotPointGraph.isFinal(currPivP))
		{
			(0,None)
		}
		else
		{
			var t = m_random.nextInt(m_pivotPointGraph.m_graph(currPivP)._3.length)
			var u = m_pivotPointGraph.m_graph(m_pivotPointGraph.m_graph(currPivP)._3(t))
			(m_pivotPointGraph.m_graph(currPivP)._3(t),Some (barycentre(u._1,u._2)))
		}
	}
	
	def getPosInGrid (p:Vect) : Vect =
	{
		return new Vect(p.x/m_cellSize,p.y/m_cellSize)
	}
	
	def isAvailable(p : Vect) : Boolean =
	{
		!(m_map.at(p.x,p.y)) && !(m_entityMap.at(p.x,p.y))
	}
	
	def isTurret(p : Vect) : Boolean =
	{
		!(m_map.at(p.x,p.y)) && (m_entityMap.at(p.x,p.y))
	}
	
	def putTurret(p : Vect) : Unit =
	{
		m_entityMap.ch(p.x,p.y,true)
	}
	
	def removeTurret(p : Vect) : Unit =
	{
		m_entityMap.ch(p.x,p.y,false)
	}
	
	def isOutOfBound(e : Entity) : Boolean =
	{
		var height = m_rows * m_cellSize
		var width = m_cols * m_cellSize
		if ((e.m_pos.x - e.m_offset.x > width) || (e.m_pos.x + e.m_offset.x < 0) ||  (e.m_pos.y - e.m_offset.y > height) || (e.m_pos.y + e.m_offset.y < 0)) {return true}
		else {return false}
	}

	def readMap(mapName : String) : Unit =
	{
		
		
	}

	def initGrid () =
	{
		// Lecture des tailles de la grille
		var lines = Source.fromFile("src/main/resources/"+m_mapName+".txt").getLines.toArray
		m_cols = lines(0).toInt
		m_rows = lines(0).toInt
		preferredSize = new Dimension(m_cols * cellSize, m_rows * cellSize)

		// Création des tableau
		m_map = new booleanMatrix(m_cols,m_rows)
		m_entityMap = new booleanMatrix(m_cols,m_rows)

		// Initialisation grille
		for (x <- 0 to m_cols-1)
		{
			for (y <- 0 to m_rows-1)
			{
				m_map.ch(x,y,false)
				m_entityMap.ch(x,y,false)
			}
		}

		// Lecture Map
		for (line <- 2 to (lines.length - 1)) {
			for (i <- 0 to (lines(line).length - 1))
			{
				if ((lines(line)(i).toInt -'0') == 1) {m_map.ch(i,line-2,true)}
			}
		}
		
		m_pivotPointGraph.init
	}
	
	def drawGrid(g : Graphics2D) =
	{
		// Dessine les cases
		g.setColor(Color.gray)
		for (x <- 0 to m_cols-1)
		{
			for (y <- 0 to m_rows-1)
			{
				if (!m_map.at(x,y))
				{
					g.fillRect(x*m_cellSize, y*m_cellSize, m_cellSize, m_cellSize)
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
			g.drawLine(i*m_cellSize, 0, i*m_cellSize, m_rows * cellSize)
		}
		for (i <- 1 to m_rows)
		{
			g.drawLine(0, i*m_cellSize, m_cols * m_cellSize, i*m_cellSize)
		}
		
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
		g.fillRect(e.m_pos.x - 50,
				   e.m_pos.y - e.m_offset.y - 10,
				   100, 10)
		g.setColor(Color.red)
		g.fillRect(e.m_pos.x-48,
				   e.m_pos.y - e.m_offset.y + 2 - 10,
				   math.min(math.max(100*e.m_hp/e.m_maxHp,0),96),
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
		for (i <- m_game.m_entityList)
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
				// drawTurretCannons(g,i.asInstanceOf[Turret])
			}
		}
	}
	
	def drawPlayer(g : Graphics2D) : Unit =
	{
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20))
		g.setColor(Color.red)
		g.drawString("PV : " + m_game.m_player.m_hp, 5 , 25)
		g.setColor(Color.yellow)
		g.drawString("Golds : " + m_game.m_player.m_gold, 5 , 50)
	}
	
	override def paint(g : Graphics2D)
	{
		// On dessine la grille
		drawGrid(g)
		// On dessine les entitées
		drawEntities(g)
		// On affiche les pv + golds du joueur
		drawPlayer(g)
	}
	
}
