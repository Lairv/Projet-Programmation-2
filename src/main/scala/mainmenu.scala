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
		text = "Map 1"
	}
	val map2Button = new Button 
	{
		preferredSize = new Dimension(200, 200)
		text = "Map 2"
	}
	val map3Button = new Button 
	{
		preferredSize = new Dimension(200, 200)
		text = "Map 3"
	}

	val mapPanel = new BorderPanel
	{
		layout(map1Button) = BorderPanel.Position.West
		layout(map2Button) = BorderPanel.Position.Center
		layout(map3Button) = BorderPanel.Position.East
	}

	val panel = new BorderPanel
	{
		layout(startButton) = BorderPanel.Position.North
		layout(mapPanel) = BorderPanel.Position.South
	}
}
