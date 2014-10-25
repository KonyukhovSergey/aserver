package ru.serjik.agame;

import java.util.ArrayList;
import java.util.List;

public class world
{
	public static List<Player> players = new ArrayList<Player>();
	
	public static void broadcast(String message)
	{
		for(Player player : players)
		{
			player.client.send(message);
		}
	}

}
