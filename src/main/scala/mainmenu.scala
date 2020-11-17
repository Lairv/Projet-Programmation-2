import swing._
import swing.event._


object MainMenu
{
	val startButton = new Button 
	{
		preferredSize = new Dimension(200, 100)
		text = "Start"
	}

	val selectedMap = new TextField
	{
		var baseText = "Map sélectionnée : "
		text = baseText + "Carte 1"
		editable = false
	}
	val map1Button = new Button 
	{
		preferredSize = new Dimension(200, 100)
		text = "Carte 1"
	}
	val map2Button = new Button 
	{
		preferredSize = new Dimension(200, 100)
		text = "Carte 2"
	}
	val mapPanel = new BorderPanel
	{
		layout(selectedMap) = BorderPanel.Position.North
		layout(map1Button) = BorderPanel.Position.West
		layout(map2Button) = BorderPanel.Position.East
	}


	val selectedWave = new TextField
	{
		var baseText = "Waves sélectionnée : "
		text = baseText + "Facile"
		editable = false
	}
	val wave1Button = new Button 
	{
		preferredSize = new Dimension(200, 100)
		text = "Facile"
	}
	val wave2Button = new Button 
	{
		preferredSize = new Dimension(200, 100)
		text = "Difficile"
	}
	val wavePanel = new BorderPanel
	{
		layout(selectedWave) = BorderPanel.Position.North
		layout(wave1Button) = BorderPanel.Position.West
		layout(wave2Button) = BorderPanel.Position.East
	}

	val panel = new BorderPanel
	{
		layout(startButton) = BorderPanel.Position.North
		layout(mapPanel) = BorderPanel.Position.Center
		layout(wavePanel) = BorderPanel.Position.South
	}
}
