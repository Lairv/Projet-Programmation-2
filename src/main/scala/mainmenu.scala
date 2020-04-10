import swing._
import swing.event._


object MainMenu
{

	val startButton = new Button 
	{
		preferredSize = new Dimension(200, 100)
		text = "Start"
	}

	val map1Button = new Button 
	{
		preferredSize = new Dimension(200, 200)
		text = "Carte facile"
	}
	val map2Button = new Button 
	{
		preferredSize = new Dimension(200, 200)
		text = "Carte difficile"
	}
	val mapPanel = new BorderPanel
	{
		layout(map1Button) = BorderPanel.Position.West
		layout(map2Button) = BorderPanel.Position.Center
	}

	val panel = new BorderPanel
	{
		preferredSize = new Dimension(400,400)
		layout(startButton) = BorderPanel.Position.Center
		layout(mapPanel) = BorderPanel.Position.South
	}
}
