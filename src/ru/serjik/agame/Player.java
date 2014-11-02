package ru.serjik.agame;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.MessageListener;
import ru.serjik.utils.file;
import ru.serjik.utils.json;

public class Player extends GameObject implements MessageListener
{
	private static final String CONTROL = "/ctl;";

	private transient static final int VISIBLE_RANGE = 9;

	private transient List<GameObject> objects = new ArrayList<GameObject>();

	public transient String name;
	private transient ClientData client;

	public static Player instatiate(String name, ClientData client)
	{
		Player player = json.parse(file.load(dataPath(name)), Player.class);
		if (player == null)
		{
			player = new Player();
		}

		player.name = name;
		player.client = client;
		client.tag = player;

		player.send(player.addCommand());
		World.instance.updateVisibleObjects(player);

		return player;
	}

	@Override
	public void onMessage(ClientData client, String message)
	{
		System.out.println(name + ": " + message);

		if (message.startsWith(CONTROL))
		{
			control(message.substring(CONTROL.length()));
		}
		else if (message.equals("info"))
		{
			send("players count = " + World.instance.playesCount());
			return;
		}
		else
		{
			World.instance.broadcast(name + ": " + message);
		}
	}

	private void control(String command)
	{
		if (command.equals("mf"))
		{
			move(1);
		}
		else if (command.equals("mb"))
		{
			move(-1);
		}
		else if (command.equals("rl"))
		{
			rotate(-1);
		}
		else if (command.equals("rr"))
		{
			rotate(1);
		}

		updateForRemoveFromVisibilityList();

		String controlCommand = "/ctl;" + id() + ";" + command;

		send(controlCommand);

		for (GameObject object : objects)
		{
			if (object instanceof Player)
			{
				((Player) object).send(controlCommand);
			}
		}

		int addIndex = World.instance.updateVisibleObjects(this);

		if (addIndex >= 0)
		{
			for (int i = addIndex; i < objects.size(); i++)
			{
				GameObject object = objects.get(i);
				if (object instanceof Player)
				{
					((Player) object).addToListOfVisibility(this);
				}
			}
		}
	}

	public int addToListOfVisibility(GameObject object)
	{
		if (object != this)
		{
			if (distance(object) <= VISIBLE_RANGE)
			{
				if (false == objects.contains(object))
				{
					send(object.addCommand());
					objects.add(object);
					return objects.size() - 1;
				}
			}
		}

		return -1;
	}

	public void removeFromListOfVisibility(GameObject object)
	{
		if (objects.remove(object))
		{
			send("/del;" + object.id());
		}
	}

	private void updateForRemoveFromVisibilityList()
	{
		for (int i = 0; i < objects.size();)
		{
			if (distance(objects.get(i)) > VISIBLE_RANGE)
			{
				GameObject object = objects.get(i);
				removeFromListOfVisibility(object);
				if (object instanceof Player)
				{
					((Player) object).removeFromListOfVisibility(this);
				}
			}
			else
			{
				i++;
			}
		}
	}

	public void save()
	{
		file.save(dataPath(name), json.from(this));
	}

	public void send(String message)
	{
		System.out.println("-> " + name + ": " + message);
		client.send(message);
	}

	private static Path dataPath(String name)
	{
		return Paths.get(Server.pathData, "users", name, "data.json");
	}

	@Override
	public String addCommand()
	{
		return "/add;player;" + id() + ";" + name + ";" + q + ";" + r + ";" + direction;
	}

}
