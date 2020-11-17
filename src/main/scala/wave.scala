import javax.swing
import javax.swing.Timer
import java.awt.event.{ActionListener,ActionEvent}
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.math.{abs, max}

class Wave(t : Array[(String,Int,Int,Int)], g : Game)
{
	// t contient des éléments de la forme (type d'ennemi, nombre, début d'apparition, fin d'apparition)
	var m_contents = t
	var m_game = g
	
	def sendWave() : Unit =
	{
		var maxTime = 0
		for (i <- m_contents)
		{
			maxTime = max(maxTime, i._4)

			var timer = new Timer(i._3, new ActionListener() {
			override def actionPerformed(e : ActionEvent) {
				
				m_game.addEnnemy(i._1,1)
			}
			});
			timer.setInitialDelay(i._3)
			timer.setDelay(((i._4-i._3)/i._2).toInt)

			var endTimer = new Timer(i._4, new ActionListener() {
			override def actionPerformed(e : ActionEvent) {
				timer.stop();
			}
			});
			endTimer.setRepeats(false);
			timer.start();
			endTimer.start();
		}
		var endWaveTimer = new Timer(maxTime, new ActionListener() {
		override def actionPerformed(e : ActionEvent) {
			g.m_testIfEndWave = true
		}
		});
		endWaveTimer.setRepeats(false);
		endWaveTimer.start();
	}
}


object ReadWaves
{
	def readWaves(f : String, g : Game) : ArrayBuffer[Wave] =
	{
		var waves = ArrayBuffer[Wave]()
		var lines = Source.fromFile("src/main/resources/"+f+".txt").getLines.toArray
		var linesRead = 0
		while (linesRead < lines.length)
		{
			var currentWave = new Array[(String,Int,Int,Int)](0)
			while (lines(linesRead) != "///")
			{
				var line = lines(linesRead).split(" ")
				currentWave :+= (line(0),line(1).toInt,line(2).toInt,line(3).toInt)
				linesRead += 1		
			}
			linesRead += 1
			waves += new Wave(currentWave, g)
		}
		
		return waves
	}
}
