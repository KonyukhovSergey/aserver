package ru.serjik.agame;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.ConsoleLineReader;
import ru.serjik.nionet.NioNetServer;
import ru.serjik.nionet.NioNetServerListener;
import ru.serjik.utils.file;
import ru.serjik.utils.json;

public class Server implements NioNetServerListener
{
	public static String pathData;
	private NioNetServer server;
	private ServerConfig config;
	private static int lastId = 0;

	public Server(String pathData) throws IOException
	{
		Server.pathData = pathData;
		config = json.parse(file.load(configPath()), ServerConfig.class);
		server = new NioNetServer(config.port, this);
	}

	public boolean tick()
	{
		server.tick();

		String cmd = ConsoleLineReader.read(System.in);

		if (cmd.equals("stop"))
		{
			return false;
		}

		return true;
	}

	public static void main(String[] args)
	{
		System.out.println("agrh!!!");

		try
		{
			Server server = new Server(args.length > 0 ? args[0] : "c:\\progs\\data\\");

			while (server.tick())
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("good bye...");
	}

	@Override
	public void onAccept(ClientData client)
	{
		System.out.println("accepted: " + client.toString());
		new Account(client);
	}

	@Override
	public void onMessage(ClientData client, String message)
	{
		// System.out.println(client.toString() + ": " + message);
		client.tag.onMessage(client, message);
	}

	@Override
	public void onDisconnect(ClientData client)
	{
		System.out.println("disconnect: " + client.toString());

		if (client.tag instanceof Player)
		{
			Player player = (Player) client.tag;
			player.save();
			World.instance.remove(player);
			World.instance.broadcast("system: " + player.name + " has disconnected");
		}
	}

	public static int getNextId()
	{
		return lastId++;
	}

	private Path configPath()
	{
		return Paths.get(pathData, "config.json");
	}
}
