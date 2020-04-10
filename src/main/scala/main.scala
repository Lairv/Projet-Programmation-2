import swing._
import swing.event._


object MyApp extends SimpleSwingApplication {
	def top = new MainFrame {

		title = "Diep.td"
		contents = MainMenu.panel
		var map = "map1"
		
		listenTo(MainMenu.startButton)
		listenTo(MainMenu.map1Button)
		listenTo(MainMenu.map2Button)
		reactions +=
		{
			case ButtonClicked(source) if (source == MainMenu.startButton) =>
				contents = (new Game(map)).newGame
			case ButtonClicked(source) if (source == MainMenu.map1Button) =>
				map = "map1"
			case ButtonClicked(source) if (source == MainMenu.map2Button) =>
				map = "map2"
			case _ => {}
		}
		
		
	}
}
