object Evolution
{
	var evolutions = Map(
				"tank" -> Some((("twin",100),("sniper",100),("",100),("",100))),
				"twin" -> Some((("tripleshot",100),("quadtank",100),("",100),("",100))),
				"tripleshot" -> Some((("triplet",100),("pentashot",100),("spreadshot",100),("",100))),
				"quadtank" -> Some((("octotank",100),("",100),("",100),("",100))),
				"sniper" -> Some((("stalker",50),("",100),("",100),("",100))),
				"pentashot" -> None,
				"octotank" -> None,
				"spreadshot" -> None,
				"stalker" -> None,
				"triplet" -> None,
			)
}
