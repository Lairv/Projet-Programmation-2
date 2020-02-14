import swing._
import swing.event._

object MyApp extends SimpleSwingApplication {
	def top = new MainFrame {

		title = "Diep TD"
		contents = (new Game).newGame
		
	}
}
