object Evolution
{
	var evolutions = Map(
				"tank" -> Some((("twin",100),("sniper",100),("tripleshot",100),("",100))),
				"twin" -> Some((("triplet",100),("quadtank",100),("",100),("",100))),
				"tripleshot" -> Some((("pentashot",100),("quadtank",100),("",100),("",100))),
				"quadtank" -> Some((("octotank",100),("",100),("",100),("",100))),
				"sniper" -> Some((("stalker",50),("",100),("",100),("",100))),
				"pentashot" -> Some((("spreadshot",50),("",100),("",100),("",100))),
				"octotank" -> None,
				"spreadshot" -> None,
				"stalker" -> None,
				"triplet" -> None,
			)
}
