trait Entity
{
	var m_sprite:java.awt.image.BufferedImage
	var m_maxHp:Int
	var m_hp:Int
	var m_pos:Vect // Position de l'angle en haut à gauche de l'image
	var m_offset:Vect // Position du centre de gravité de l'entité
	
	def update(g:Game):Unit
	def init(g:Game):Unit
	
	def isDead():Boolean =
	{
		return (m_hp <= 0)
	}
}
