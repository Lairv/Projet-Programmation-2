import swing._
import swing.event._
import java.awt.{Graphics2D,Color}
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer

class Game extends Reactor
{
	val m_grid = new Grid(20,10,50,this)
	var m_entityList = ArrayBuffer[Entity]()
	var m_deadEntities = ArrayBuffer[Int]()
	var m_g = this
	var m_player = new Player
	
	var m_waves = Array(
					new Wave(Array(
								("ysquare",2,0),
								("ysquare",2,2000),
								("ysquare",2,4000),
								("rtriangle",2,4000),
								),this
							),
					new Wave(Array(
								("ysquare",5,0),
								("rtriangle",3,2500),
								("ysquare",5,5000),
								("rtriangle",3,7500),
								("ysquare",5,10000),
								("rtriangle",3,11000),
								("ysquare",5,12000),
								("rtriangle",3,11000),
								("ysquare",5,14000)
								),this
							),
					new Wave(Array(
								("ysquare",5,0),
								("ysquare",5,2000),
								("ysquare",5,4000),
								("ysquare",5,6000),
								("ysquare",5,8000),
								("ysquare",5,10000),
								("ysquare",10,12000),
								("ysquare",10,14000)
								),this
							),
				)
	var m_waveCounter = 0			
	
	
	object Loop extends Runnable
	{
		def run
		{
			while (true)
			{
				this.synchronized
				{
					// On update toutes les entitées
					m_deadEntities = ArrayBuffer[Int]()
					for (i <- 0 to m_entityList.length-1)
					{
						m_entityList(i).update(m_g)
						if (m_entityList(i).isDead)
						{
							m_deadEntities += i
						}
					}
					// On enlève les entitées mortes
					for (i <- m_deadEntities.length-1 to 0 by -1)
					{
						m_entityList.remove(m_deadEntities(i))
					}
					
					// On affiche tout 
					m_grid.repaint
					m_grid.revalidate
				}
				Thread.sleep(17)
			}
		}
	}
	
	def giveTargetToTurret(t : Turret) : Option[Entity]=
	{
		for (i <- m_entityList)
		{
			if (i.m_type == "ennemy" && (i.m_pos-t.m_pos).length <= t.m_range)
			{
				return Some (i)
			}
		}
		return None
	}
	
	def addEntity(e : Entity) : Unit =
	{
		m_entityList += e
	}
	
	def addEnnemy(ennemyType : String, number : Int) : Unit =
	{
		if (ennemyType == "ysquare")
		{
			for (k <- 1 to number)
			{
				var i = new YSquare
				i.init(this)
				addEntity(i)
			}
		}
		if (ennemyType == "rtriangle")
		{
			for (k <- 1 to number)
			{
				var i = new RTriangle
				i.init(this)
				addEntity(i)
			}
		}
	}
	
	def newGame : BorderPanel =
	{	
		// Panneau des boutons
		val textOutput = new TextField
		{
			text = "Welcome !"
			editable = false
		}
		val sendWaveButton = new Button 
		{
			text = "New wave"
		}
		val addTurretButton = new Button
		{
			text = "Add turret 50g"
		}
		
		
    	val buttonPanel = new GridPanel(10,1)
    	{
    		preferredSize = new Dimension(200, 150)
    		contents += sendWaveButton
    		contents += addTurretButton
    	}
    	listenTo(sendWaveButton)
    	listenTo(addTurretButton)
    	
		// Création de la grille de jeu
    	m_grid.initGrid()
    	m_grid.listenTo(m_grid.mouse.clicks)
		m_grid.reactions +=
		{
				case MouseClicked(_,p,_,_,_) =>
					m_grid.m_selected = Some (m_grid.getPosInGrid(new Vect(p.x,p.y)))
		}
    	
    	// Création de l'interface
    	val panel = new BorderPanel
    	{
			layout(buttonPanel) = BorderPanel.Position.West
    		layout(m_grid) = BorderPanel.Position.East
    		layout(textOutput) = BorderPanel.Position.South
		}
		
		// Ajout des évènements
		reactions +=
		{
			case ButtonClicked(source) if (source == sendWaveButton) => 
				if (m_waveCounter == m_waves.length)
				{
					textOutput.text = "C'est terminé fdp"
				}
				else
				{
					m_waves(m_waveCounter).sendWave
					m_waveCounter += 1
				}
				
			case ButtonClicked(source) if (source == addTurretButton) =>
				m_grid.m_selected match
				{
					case None =>
						textOutput.text = "Selectionne une case fdp"
					case Some(p) =>
						if (m_grid.isAvailable(p))
						{
							var i = new Tank(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
							i.init(this)
							m_entityList += i
							m_grid.putTurret(p)
						}
						else
						{
							textOutput.text = "Mets pas de tourelle ici fdp"
						}
				}		
			
			case _ => {}
		}
		
		// Démarrage de la boucle de jeu dans un thread
    	new Thread(Loop).start
    	
		return panel
	}
	
}
