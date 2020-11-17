import swing._
import swing.event._


object MyApp extends SimpleSwingApplication {
	def top = new MainFrame {
		title = "Diep.td"
		contents = MainMenu.panel
		var map = "map1"
		var wave = "waves1"
		
		listenTo(MainMenu.startButton)
		listenTo(MainMenu.map1Button)
		listenTo(MainMenu.map2Button)
		listenTo(MainMenu.wave1Button)
		listenTo(MainMenu.wave2Button)
		reactions +=
		{
			case ButtonClicked(source) if (source == MainMenu.startButton) =>
				contents = (new Game(map,wave)).newGame
			case ButtonClicked(source) if (source == MainMenu.map1Button) =>
				map = "map1"
				MainMenu.selectedMap.text = MainMenu.selectedMap.baseText + MainMenu.map1Button.text
			case ButtonClicked(source) if (source == MainMenu.map2Button) =>
				map = "map2"
				MainMenu.selectedMap.text = MainMenu.selectedMap.baseText + MainMenu.map2Button.text
			case ButtonClicked(source) if (source == MainMenu.wave1Button) =>
				wave = "waves1"
				MainMenu.selectedWave.text = MainMenu.selectedWave.baseText + MainMenu.wave1Button.text
			case ButtonClicked(source) if (source == MainMenu.wave2Button) =>
				wave = "waves2"
				MainMenu.selectedWave.text = MainMenu.selectedWave.baseText + MainMenu.wave2Button.text
			case _ => {}
		}
	}
}
