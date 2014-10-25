package ru.serjik.agame;

import java.nio.file.Paths;

import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.MessageListener;

public class Player implements MessageListener
{
	public String name;
	public ClientData client;

	private String dataPath;

	public Player(String name, ClientData client)
	{
		this.name = name;
		this.dataPath = Paths.get(Server.pathData, "users", name).toString();
		client.tag = this;
		this.client = client;
	}

	@Override
	public void onMessage(ClientData client, String message)
	{

	}
}
