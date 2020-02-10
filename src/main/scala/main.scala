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
	
	def newGame : BorderPanel =
	{	
		val playButton = new Button {
			preferredSize = new Dimension(100,50)
			text = "Play"
		}
    	
    	m_grid.initGrid()
    	new Thread(Loop).start
    	
    	listenTo(playButton)
    	
    	val buttonPanel = new GridPanel(5,1)
    	{
    		contents += playButton
    	}
    	
    	val panel = new BorderPanel
    	{
			layout(buttonPanel) = BorderPanel.Position.West
    		layout(m_grid) = BorderPanel.Position.East
		}
		return panel
	}
	
	reactions +=
	{
		case ButtonClicked(source) => 
			var i = new YSquare
			i.init(this)
			m_entityList += i
		case _ => {}
	}
}

object MyApp extends SimpleSwingApplication {
	
	def top = new MainFrame {

		title = "Diep.io"
		contents = (new Game).newGame
		
	}
}
