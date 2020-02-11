trait Entity
{
	var m_sprite:java.awt.image.BufferedImage
	var m_maxHp:Int
	var m_hp:Int
	var m_pos:Vect // Position du centre de gravité de l'entité
	var m_offset:Vect // Décalage du haut à gauche de l'image de l'entité par rapport au centre
	var m_type:String
	var m_rotation:Double
	
	def update(g:Game):Unit
	def init(g:Game):Unit
	
	def isDead():Boolean =
	{
		return (m_hp <= 0)
	}
}