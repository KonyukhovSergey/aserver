package ru.serjik.agame;

public class GameItem extends GameObject
{
	public String type;
	public int count;

	public GameItem(String[] values)
	{
		type = values[1];
		count = Integer.parseInt(values[2]);
		q = Integer.parseInt(values[3]);
		r = Integer.parseInt(values[4]);
		direction = Integer.parseInt(values[5]);
	}

	@Override
	public String addCommand()
	{
		return "/add;item;" + id() + ";" + type + ";" + count + location();
	}

}
