import javax.swing
import javax.swing.Timer
import java.awt.event.{ActionListener,ActionEvent}

class Wave(t : Array[(String,Int,Int)], g : Game)
{
	// t contient des éléments de la forme (type d'ennemi, nombre, délai d'apparition)
	var m_contents = t
	var m_game = g
	
	def sendWave() : Unit =
	{
		for (i <- m_contents)
		{
			var timer = new Timer(i._3, new ActionListener() {
		      override def actionPerformed(e : ActionEvent) {
		          m_game.addEnnemy(i._1,i._2)
		      }
			});
			timer.setRepeats(false);
			timer.start();
		}
	}
}
