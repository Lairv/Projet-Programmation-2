import swing._
import swing.event._
import java.awt.{Graphics2D,Color}
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer

class NewProgressBar(m_min:Int, m_max:Int, m_label:String) extends ProgressBar
{
	min = m_min
	max = m_max
	label = m_label
	labelPainted = true
}

class StatsButton(price:Int, amount:Int, buttonType:String) extends Button
{
	text = "+ (" + price.toString + "g)"
	var m_amount = amount
	var m_price = price
	var m_type = buttonType
	def buy(g : Game) : Unit =
	{
		var t = g.getSelectedTurret()
		if (m_type == "speed") {t.m_bulletSpeed += m_amount}
		else if (m_type == "pen") {t.m_bulletPenetration += m_amount}
		else if (m_type == "dmg") {t.m_bulletDamage += m_amount}
		else if (m_type == "reload") {t.m_reload -= m_amount}
		
 		g.m_player.m_gold -= m_price
		t.init(g)
	}
}

class EvolutionButton(evolution:String, price:Int, exp:Int) extends Button
{
	var m_evolution = evolution
	var m_price = price
	var m_exp = exp
	text = m_evolution + " (" + m_price.toString + "g)"

	def updateTxt() : Unit =
	{
		text = m_evolution + " (" + m_price.toString + "g)"
	}
	def buy(g:Game) : Unit =
	{
		g.getSelectedTurret.m_hp = 0
		g.addTurret(m_evolution,g.getSelectedPos)
		g.m_player.m_gold -= m_price
	}
	def disable() : Unit =
	{
		text = ""
		enabled = false
	}
	def enable(txt:String, exp:Int) : Unit =
	{
		if (txt == "")
		{
			disable		
		}
		else
		{
			m_evolution = txt
			m_exp = exp
			updateTxt()
		}
	}
}
