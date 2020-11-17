import java.awt.{Graphics2D,Color,Font}
import java.awt.geom.AffineTransform

trait Entity
{
	var m_sprite:java.awt.image.BufferedImage
	var m_maxHp:Int
	var m_hp:Int
	var m_pos:Vect // Position du centre de gravité de l'entité
	var m_offset:Vect // Décalage du haut à gauche de l'image de l'entité par rapport au centre
	var m_type:String
	var m_rotation:Double
	var m_rotationSpeed:Double
	var m_dmg:Int // Dégats des collisions
	var m_baseSpeed:Double // Vitesse initiale pour pouvoir la remettre à zero	
	var m_speed:Double

	// m_radius correspond à la hitbox de l'entité
	// Pour l'instant il n'y que des hitboxes circulaires, cela permet de facilement
	// tester les collisions avec un simple calcul de distances
	var m_radius:Int
	
	def update(g:Game):Unit
	def init(g:Game):Unit
	
	def rmvHp(x:Int):Unit =
	{
		m_hp -= x
	}
	
	def customAnimation(g:Graphics2D):Unit = {}

	def isDead():Boolean =
	{
		return (m_hp <= 0)
	}
}
