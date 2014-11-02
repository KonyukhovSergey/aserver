package ru.serjik.agame;

import ru.serjik.utils.HexUtils;

public abstract class GameObject extends HexLocation
{
	private transient int id = Server.getNextId();

	public int distance(GameObject object)
	{
		return HexUtils.distance(q, r, object.q, object.r);
	}

	public int id()
	{
		return id;
	}
	
	public abstract String addCommand();

}
