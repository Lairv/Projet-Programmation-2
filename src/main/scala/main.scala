import swing._
import swing.event._


object MyApp extends SimpleSwingApplication {
	def top = new MainFrame {

		title = "Diep.td"
		contents = MainMenu.panel
		
		listenTo(MainMenu.startButton)
		reactions +=
		{
			case ButtonClicked(source) if (source == MainMenu.startButton) =>
				contents = (new Game).newGame
			case _ => {}
		}
		
		
	}
}
