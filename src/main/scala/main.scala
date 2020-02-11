import swing._
import swing.event._

object MyApp extends SimpleSwingApplication {
	def top = new MainFrame {

		title = "Diep.io"
		contents = (new Game).newGame
		
	}
}
