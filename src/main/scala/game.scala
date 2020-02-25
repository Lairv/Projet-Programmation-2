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
	
	def getTurretInPos(p : Vect) : Option[Turret] =
	{
		if (m_grid.isTurret(p))
		{
			for (e <- m_entityList)
			{
				if (m_grid.getPosInGrid(e.m_pos) == p)
				{
					return Some (e.asInstanceOf[Turret])
				}
			}
		}
		return None
	}
		
	// DEFINITION DE TOUS LES ELEMENTS DE L'INTERFACE
	
	// Panneau des boutons
	val textOutput = new TextField
	{
		text = "Welcome !"
		editable = false
	}
	val sendWaveButton = new Button 
	{
		preferredSize = new Dimension(250, 40)
		text = "New wave"
	}
	
	// Affiché quand on ne sélectionne pas de tourelles
	
	val addTurretButton = new Button
	{
		text = "Add turret 50g"
	}
	
	val nonSelectedPanel = new BorderPanel
	{
		layout(addTurretButton) = BorderPanel.Position.Center
	}
	
	// Affiché quand on sélectionne une tourelle
	
	// Barres de progression
	val expBar = new ProgressBar
	{
		min = 0
		max = 100
		label = "EXP"
		labelPainted = true
	}
	val bulletSpeedBar = new ProgressBar
	{
		min = 0
		max = 100
		label = "Bullet Speed"
		labelPainted = true
	}
	val bulletPenBar = new ProgressBar
	{
		min = 0
		max = 100
		label = "Bullet Penetration"
		labelPainted = true
	}
	val bulletDmgBar = new ProgressBar
	{
		min = 0
		max = 100
		label = "Bullet Damage"
		labelPainted = true
	}
	val reloadBar = new ProgressBar
	{
		min = 0
		max = 100
		label = "Reload Speed"
		labelPainted = true
	}
	
	// Boutons correspondants
	val bulletSpeedButton = new Button
	{
		text = "+"
	}
	val bulletPenButton = new Button
	{
		text = "+"
	}
	val bulletDmgButton = new Button
	{
		text = "+"
	}
	val reloadButton = new Button
	{
		text = "+"
	}
	
	val progressionBarPanel = new GridPanel(9,1)
	{
		contents += expBar
		contents += bulletSpeedBar
		contents += bulletSpeedButton
		contents += bulletPenBar
		contents += bulletPenButton
		contents += bulletDmgBar
		contents += bulletDmgButton
		contents += reloadBar
		contents += reloadButton
	}
	
	// Panel avec les évolutions
	val evolution1Button = new Button
	{
		text = "Evolution 1"
	}
	val evolution2Button = new Button
	{
		text = "Evolution 2"
	}
	val evolution3Button = new Button
	{
		text = "Evolution 3"
	}
	val evolution4Button = new Button
	{
		text = "Evolution 4"
	}
	
	val evolutionPanel = new GridPanel(2,2)
	{
		contents += evolution1Button
		contents += evolution2Button
		contents += evolution3Button
		contents += evolution4Button
	}
	
	// Bouton pour vendre
	val sellButton = new Button
	{
		text = "$$$"
	}
	
	// Panel quand une tourelle est sélectionnée
	val selectedPanel = new BorderPanel
	{
		layout(progressionBarPanel) = BorderPanel.Position.North
		layout(evolutionPanel) = BorderPanel.Position.Center
		layout(sellButton) = BorderPanel.Position.South
	}
	
	// Panneau général qui est modifié quand on sélectionne une tourelle
	val modifiablePanel = new GridPanel(1,1)
	{
		var m_state = false // Permet de savoir dans quel état est ce panel
		contents += nonSelectedPanel
	}
	
	// Fonctions pour actualiser et modifier les panels
	
	object PanelLoop extends Runnable
	{
		def run
		{
			while (true)
			{
				this.synchronized
				{
					modifiablePanel.repaint
					modifiablePanel.revalidate
				}
				Thread.sleep(100)
			}
		}
	}
	
	def swapModifiablePanel() : Unit =
	{
		if (!modifiablePanel.m_state)
		{
			modifiablePanel.contents -= nonSelectedPanel
			modifiablePanel.contents += selectedPanel
			modifiablePanel.m_state = true
		}
		else
		{
			modifiablePanel.contents -= selectedPanel
			modifiablePanel.contents += nonSelectedPanel
			modifiablePanel.m_state = false
		}
	}
	
	def updateSelectedPanel(t : Turret) : Unit =
	{
		expBar.value = t.m_exp
		bulletSpeedBar.value = t.m_bulletSpeed
		bulletPenBar.value = t.m_bulletPenetration
		bulletDmgBar.value = t.m_bulletDamage
		reloadBar.value = t.m_reload
	}
	
	// Panel avec les boutons
	val buttonPanel = new BorderPanel
	{	
		layout(sendWaveButton) = BorderPanel.Position.North
		layout(modifiablePanel) = BorderPanel.Position.Center
	}
	listenTo(sendWaveButton)
	listenTo(addTurretButton)
	
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
			swapModifiablePanel()
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
		
	
	def newGame : BorderPanel =
	{	
		// Création de la grille de jeu
    	m_grid.initGrid()
    	m_grid.listenTo(m_grid.mouse.clicks)
		m_grid.reactions +=
		{
				case MouseClicked(_,p,_,_,_) =>
					m_grid.m_selected = Some (m_grid.getPosInGrid(new Vect(p.x,p.y)))
					getTurretInPos(m_grid.getPosInGrid(new Vect(p.x,p.y))) match
					{
						case None =>
							if (modifiablePanel.m_state)
							{
								swapModifiablePanel()
							}
						case Some(t) =>
							if (!modifiablePanel.m_state)
							{
								updateSelectedPanel(t)
								swapModifiablePanel()
							}
					}
					
		}
    	
		// Démarrage de la boucle de jeu dans un thread
    	new Thread(Loop).start
		// Actualisation des Panels dans un thread
    	new Thread(PanelLoop).start
    	
		return panel
	}
	
}
