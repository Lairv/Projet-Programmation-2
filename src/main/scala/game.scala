import swing._
import swing.event._
import java.awt.{Graphics2D,Color}
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer

class Game(map:String) extends Reactor
{
	val m_grid = new Grid(map,50,this)
	var m_entityList = ArrayBuffer[Entity]()
	var m_deadEntities = ArrayBuffer[Int]()
	var m_g = this
	var m_player = new Player
	
	var m_waves = Array(
					new Wave(Array(
								("ysquare",3,0),
								("ysquare",3,2000),
								("ysquare",3,4000)
								),this
							),
					new Wave(Array(
								("ysquare",5,0),
								("ysquare",5,5000),
								("ysquare",5,10000),
								("ysquare",5,12000),
								("ysquare",5,14000),
								("rtriangle",1,18000)
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
					new Wave(Array(
								("bpentagon",2,0),
								("ysquare",10,100),
								("gpentagon",2,5000),
								("ysquare",10,5000)
								),this
							),
					new Wave(Array(
								("ysquare",5,0),
								("ysquare",5,2000),
								("rtriangle",3,3000),
								("ysquare",5,4000),
								("rtriangle",3,5000),
								("ysquare",5,6000),
								("rtriangle",3,7000),
								("bpentagon",1,8000),
								("ysquare",5,10000),
								("bpentagon",3,12000),
								("ysquare",10,14000)
								),this
							),
					new Wave(Array(
								("bpentagon",5,0),
								("gpentagon",10,0),
								("ysquare",20,100),
								("rtriangle",20,200),
								("ysquare",20,500),
								("rtriangle",20,600),
								),this
							),
					new Wave(Array(
								("apentagon",1,0),
								("bpentagon",3,5000),
								("bpentagon",5,10000),
								),this
							),
					new Wave(Array(
								("apentagon",5,0),
								("apentagon",10,5000),
								("apentagon",50,10000),
								("apentagon",200,15000),
								),this
							)
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
						if (m_entityList(i).isDead || m_grid.isOutOfBound(m_entityList(i)))
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
	
	def addTurret(turretType : String, p : Vect) : Unit =
	{
		if (turretType == "smasher")
		{
			var i = new Smasher(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			m_entityList += i
			m_grid.putTurret(p)
		}
		if (turretType == "tank")
		{
			var i = new Tank(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			i.init(this)
			m_entityList += i
			m_grid.putTurret(p)
		}
		if (turretType == "twin")
		{
			var i = new Twin(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			i.init(this)
			m_entityList += i
			m_grid.putTurret(p)
		}
		if (turretType == "triplet")
		{
			var i = new Triplet(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			i.init(this)
			m_entityList += i
			m_grid.putTurret(p)
		}
		if (turretType == "tripleshot")
		{
			var i = new Tripleshot(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			i.init(this)
			m_entityList += i
			m_grid.putTurret(p)
		}
		if (turretType == "stalker")
		{
			var i = new Stalker(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			i.init(this)
			m_entityList += i
			m_grid.putTurret(p)
		}
		if (turretType == "spreadshot")
		{
			var i = new Spreadshot(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			i.init(this)
			m_entityList += i
			m_grid.putTurret(p)
		}
		if (turretType == "sniper")
		{
			var i = new Sniper(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			i.init(this)
			m_entityList += i
			m_grid.putTurret(p)
		}
		if (turretType == "quadtank")
		{
			var i = new Quadtank(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			i.init(this)
			m_entityList += i
			m_grid.putTurret(p)
		}
		if (turretType == "pentashot")
		{
			var i = new Pentashot(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			i.init(this)
			m_entityList += i
			m_grid.putTurret(p)
		}
		if (turretType == "octotank")
		{
			var i = new Octotank(p*m_grid.m_cellSize + new Vect(m_grid.m_cellSize/2,m_grid.m_cellSize/2))
			i.init(this)
			m_entityList += i
			m_grid.putTurret(p)
		}
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
		if (ennemyType == "bpentagon")
		{
			for (k <- 1 to number)
			{
				var i = new BPentagon
				i.init(this)
				addEntity(i)
			}
		}
		if (ennemyType == "gpentagon")
		{
			for (k <- 1 to number)
			{
				var i = new GPentagon
				i.init(this)
				addEntity(i)
			}
		}
		if (ennemyType == "apentagon")
		{
			for (k <- 1 to number)
			{
				var i = new APentagon
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
				if (m_grid.getPosInGrid(e.m_pos) == p && e.m_type == "turret")
				{
					return Some (e.asInstanceOf[Turret])
				}
			}
		}
		return None
	}

	def collide(e1 : Entity, e2 : Entity) : Boolean =
	{
		var inter = e1.m_pos-e2.m_pos
		return (inter.length <= (e1.m_radius + e2.m_radius))
	}

	def getCollisions(e:Entity, entityType:String) : ArrayBuffer[Entity] =
	{
		var collideList = ArrayBuffer[Entity]()
		for (i <- 0 to m_entityList.length-1)
		{
			if (collide(e,m_entityList(i)) && (m_entityList(i).m_type == entityType))
			{
				 collideList += m_entityList(i)
			}
		}
		return collideList
	}
	
	def getInRange(e:Entity,range:Int) : ArrayBuffer[Entity] =
	{
		var inRangeList = ArrayBuffer[Entity]()
		for (i <- 0 to m_entityList.length-1)
		{
			if (((e.m_pos-m_entityList(i).m_pos).length <= range) && (m_entityList(i).m_type == "ennemy"))
			{
				 inRangeList += m_entityList(i)
			}
		}
		return inRangeList
	}

	// À n'appeler que lorsqu'on sait qu'une tourelle est sélectionnée
	def getSelectedTurret() : Turret =
	{
		m_grid.m_selected match
		{
			case None => new Tank(new Vect(0,0)) // n'arrive jamais
			case Some(p) =>
				getTurretInPos(p) match
				{
					case None => new Tank(new Vect(0,0)) // n'arrive jamais
					case Some(t) => return t
				}
		}
	}
	
	// À n'appeler que lorsqu'on sait qu'une case est sélectionnée
	def getSelectedPos() : Vect =
	{
		m_grid.m_selected match
		{
			case None => new Vect(0,0)
			case Some(p) => p
		}
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
	val expBar = new NewProgressBar(0,100,"EXP")
	val bulletSpeedBar = new NewProgressBar(0,100,"Bullet Speed")
	val bulletPenBar = new NewProgressBar(0,100,"Bullet Penetration")
	val bulletDmgBar = new NewProgressBar(0,100,"Bullet Damage")
	val reloadBar = new NewProgressBar(10,40,"Reload Speed")
	
	// Boutons correspondants
	val bulletSpeedButton = new StatsButton(10,10,"speed")
	val bulletPenButton = new StatsButton(10,10,"pen")
	val bulletDmgButton = new StatsButton(10,5,"dmg")
	val reloadButton = new StatsButton(10,5,"reload")

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
	val evolution1Button = new EvolutionButton("???",100,100)
	val evolution2Button = new EvolutionButton("???",100,100)
	val evolution3Button = new EvolutionButton("???",100,100)
	val evolution4Button = new EvolutionButton("???",100,100)

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
	
	listenTo(bulletSpeedButton)
	listenTo(bulletPenButton)
	listenTo(bulletDmgButton)
	listenTo(reloadButton)
	listenTo(evolution1Button)
	listenTo(evolution2Button)
	listenTo(evolution3Button)
	listenTo(evolution4Button)
	listenTo(sellButton)
	
	reactions +=
	{
		case ButtonClicked(source) if (source == bulletSpeedButton) => (source).asInstanceOf[StatsButton].buy(this)
		case ButtonClicked(source) if (source == bulletPenButton) => (source).asInstanceOf[StatsButton].buy(this)
		case ButtonClicked(source) if (source == bulletDmgButton) => (source).asInstanceOf[StatsButton].buy(this)
		case ButtonClicked(source) if (source == reloadButton) => (source).asInstanceOf[StatsButton].buy(this)
		case ButtonClicked(source) if (source == evolution1Button) => (source).asInstanceOf[EvolutionButton].buy(this)
		case ButtonClicked(source) if (source == evolution2Button) => (source).asInstanceOf[EvolutionButton].buy(this)
		case ButtonClicked(source) if (source == evolution3Button) => (source).asInstanceOf[EvolutionButton].buy(this)
		case ButtonClicked(source) if (source == evolution4Button) => (source).asInstanceOf[EvolutionButton].buy(this)
		case ButtonClicked(source) if (source == sellButton) =>
			getSelectedTurret.m_hp = 0
			m_grid.removeTurret(getSelectedPos)
			m_player.m_gold += 50
		case _ => {}
	}
	
	// Panneau général qui est modifié quand on sélectionne une tourelle
	val modifiablePanel = new GridPanel(1,1)
	{
		preferredSize = new Dimension(300,200)
		contents += nonSelectedPanel
	}
	
	// Fonctions pour actualiser et modifier les panels
	def updateSelectedPanel(t : Turret) : Unit =
	{
		expBar.value = t.m_exp
		bulletSpeedBar.value = t.m_bulletSpeed
		bulletPenBar.value = t.m_bulletPenetration
		bulletDmgBar.value = t.m_bulletDamage
		reloadBar.value = reloadBar.max - t.m_reload + reloadBar.min
		
		// Mise à jour des boutons
		if (t.m_bulletSpeed + bulletSpeedButton.m_amount <= bulletSpeedBar.max && m_player.m_gold >= bulletSpeedButton.m_price)
		{
			bulletSpeedButton.enabled = true
		}
		else
		{
			bulletSpeedButton.enabled = false
		}
		if (t.m_bulletPenetration + bulletPenButton.m_amount <= bulletPenBar.max && m_player.m_gold >= bulletPenButton.m_price)
		{
			bulletPenButton.enabled = true
		}
		else
		{
			bulletPenButton.enabled = false
		}
		if (t.m_bulletDamage + bulletDmgButton.m_amount <= bulletDmgBar.max && m_player.m_gold >= bulletDmgButton.m_price)
		{
			bulletDmgButton.enabled = true
		}
		else
		{
			bulletDmgButton.enabled = false
		}
		if (t.m_reload - reloadButton.m_amount >= reloadBar.min && m_player.m_gold >= reloadButton.m_price)
		{
			reloadButton.enabled = true
		}
		else
		{
			reloadButton.enabled = false
		}
		
		// Mise à jour des boutons d'evolution
		if (t.m_exp >= evolution1Button.m_exp && m_player.m_gold >= evolution1Button.m_price)
		{
			evolution1Button.enabled = true
		}
		else
		{
			evolution1Button.enabled = false
		}
		if (t.m_exp >= evolution2Button.m_exp && m_player.m_gold >= evolution2Button.m_price)
		{
			evolution2Button.enabled = true
		}
		else
		{
			evolution2Button.enabled = false
		}
		if (t.m_exp >= evolution3Button.m_exp && m_player.m_gold >= evolution3Button.m_price)
		{
			evolution3Button.enabled = true
		}
		else
		{
			evolution3Button.enabled = false
		}
		if (t.m_exp >= evolution4Button.m_exp && m_player.m_gold >= evolution4Button.m_price)
		{
			evolution4Button.enabled = true
		}
		else
		{
			evolution4Button.enabled = false
		}

		
		Evolution.evolutions(t.m_upgrade) match
		{
			case None =>
				evolution1Button.disable
				evolution2Button.disable
				evolution3Button.disable
				evolution4Button.disable
			case Some(e) =>
				evolution1Button.enable(e._1._1,e._1._2)
				evolution2Button.enable(e._2._1,e._2._2)
				evolution3Button.enable(e._3._1,e._3._2)
				evolution4Button.enable(e._4._1,e._4._2)
		}
	}
	
	def updateModifiablePanel() : Unit =
	{
		m_grid.m_selected match
		{
			case None =>
				modifiablePanel.contents -= selectedPanel
				modifiablePanel.contents += nonSelectedPanel
			case Some(p) =>
				getTurretInPos(p) match
				{
					case None =>
						modifiablePanel.contents -= selectedPanel
						modifiablePanel.contents += nonSelectedPanel
					case Some(t) =>
						updateSelectedPanel(t)
						modifiablePanel.contents -= nonSelectedPanel
						modifiablePanel.contents += selectedPanel
				}
		}
	}
	
	object PanelLoop extends Runnable
	{
		def run
		{
			while (true)
			{
				this.synchronized
				{
					updateModifiablePanel()
					modifiablePanel.repaint
					modifiablePanel.revalidate
				}
				Thread.sleep(100)
			}
		}
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
			if (m_waveCounter == m_waves.length)
			{
				textOutput.text = "C'est terminé"
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
					textOutput.text = "Selectionne une case"
				case Some(p) =>
					if (m_player.m_gold < 50)
					{
						textOutput.text = "Tu n'as pas assez d'argent"
					}
					else if (m_grid.isAvailable(p))
					{
						m_player.m_gold -= 50
						if (m_grid.isTurretGround(p))
						{
							addTurret("tank",p)
						}
						else if (m_grid.isEnnemyGround(p))
						{
							addTurret("smasher",p)
						}
					}
					else
					{
						textOutput.text = "Ne mets pas de tourelle ici"
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
		}

		// Démarrage de la boucle de jeu dans un thread
    		new Thread(Loop).start
		// Actualisation des panels dans un thread
    		new Thread(PanelLoop).start
    	
		return panel
	}
	
}
