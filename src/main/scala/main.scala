import swing._
import swing.event._
import java.awt
import java.awt.event
import javax.swing
import java.awt.CardLayout


object MyApp extends SimpleSwingApplication {
	def top = new MainFrame {

		title = "Diep.td"
		
		/*
		var cl = new CardLayout(800,600)
		this.setLayout(cl)
		
		val button1 = new Button 
		{
			text = "b1"
		}
		val button2 = new Button 
		{
			text = "b2"
		}
		val panel1 = new BorderPanel {
			layout(button1) = BorderPanel.Position.Center
		}
		val panel2 = new BorderPanel {
			layout(button2) = BorderPanel.Position.Center
		}
		this.add(panel1)
		this.add(panel2)
		*/
		contents = (new Game).newGame
		
	}
}
