import swing._
import swing.event._
import java.awt.{Graphics2D,Color}
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer

class Game extends Reactor
{
	val m_grid = new Grid(20,10,50)
	var m_entityList = ArrayBuffer[Entity]()
	var m_deadEntities = ArrayBuffer[Int]()
	var m_g = this
	
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
					for (i <- m_deadEntities)
					{
						m_entityList.remove(i)
					}
					// On change les entitées a afficher
					m_grid.setEntities(m_entityList)
					// On affiche tout 
					m_grid.repaint
					m_grid.revalidate
				}
				Thread.sleep(50)
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
	
	def newGame : BorderPanel =
	{	
		// Panneau des boutons
		val textOutput = new TextField
		{
			text = "Welcome !"
			editable = false
		}
		val playButton = new Button 
		{
			preferredSize = new Dimension(200,50)
			text = "Add ennemy"
		}
		val addTankButton = new Button
		{
			preferredSize = new Dimension(200,50)
			text = "Add tank"
		}
		val addTweenButton = new Button
		{
			preferredSize = new Dimension(200,50)
			text = "Add double tank"
		}
    	val buttonPanel = new GridPanel(5,1)
    	{
    		contents += playButton
    		contents += addTankButton
    		contents += addTweenButton
    	}
    	listenTo(playButton)
    	listenTo(addTankButton)
    	listenTo(addTweenButton)
    	
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
			case ButtonClicked(source) if (source == playButton) => 
				var i = new YSquare
				i.init(this)
				m_entityList += i
				
			case ButtonClicked(source) if (source == addTankButton) =>
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
						}
						else
						{
							textOutput.text = "Y'a déjà une tourelle fdp"
						}
				}		
			
			case ButtonClicked(source) if (source == addTweenButton) =>
				m_grid.m_selected match
				{
					case None =>
						textOutput.text = "Selectionne une case fdp"
					case Some(p) =>
						if (m_grid.isAvailable(p))
						{
							var i = new Tween(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
							i.init(this)
							m_entityList += i
						}
						else
						{
							textOutput.text = "Y'a déjà une tourelle fdp"
						}
				}		
				
			case _ => {}
		}
		
		// Démarrage de la boucle de jeu dans un thread
    	new Thread(Loop).start
    	
		return panel
	}
	
}
