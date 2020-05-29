class Player
{
	var m_hp = 100
	var m_gold = 100

	def isDead() : Boolean =
	{
		return (m_hp <= 0)
	}
}
