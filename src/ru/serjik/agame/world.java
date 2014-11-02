package ru.serjik.agame;

import java.util.ArrayList;
import java.util.List;

public class World
{
	public static final World instance = new World();

	private List<GameObject> objects = new ArrayList<GameObject>();
	private List<Player> players = new ArrayList<Player>();

	public void broadcast(String message)
	{
		for (Player player : players)
		{
			player.send(message);
		}
	}

	public void add(GameObject object)
	{
		objects.add(object);

		for (Player player : players)
		{
			player.addToListOfVisibility(object);
		}

		if (object instanceof Player)
		{
			players.add((Player) object);
		}
	}

	public void remove(GameObject object)
	{
		objects.remove(object);

		if (object instanceof Player)
		{
			players.remove(object);
		}

		for (Player player : players)
		{
			player.removeFromListOfVisibility(object);
		}
	}

	public int playesCount()
	{
		return players.size();
	}

	public int updateVisibleObjects(Player player)
	{
		int index = -1;
		for (GameObject object : objects)
		{
			if (object != player)
			{
				int addedIndex = player.addToListOfVisibility(object);
				
				if (index < 0 && addedIndex >= 0)
				{
					index = addedIndex;
				}
			}
		}
		return index;
	}

}
