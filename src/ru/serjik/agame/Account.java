package ru.serjik.agame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.MessageListener;
import ru.serjik.utils.file;
import ru.serjik.utils.json;
import ru.serjik.utils.md5;

public class Account implements MessageListener
{
	public String login;
	private String passh;

	public Account(ClientData client)
	{
		client.tag = this;
		salt = md5.get("time: " + System.currentTimeMillis());
		System.out.println("salt: " + salt);
		client.send(salt);
	}

	private transient states state = states.LOGIN_PASSH;

	private transient String captcha;
	private transient String salt;

	@Override
	public void onMessage(ClientData client, String message)
	{
		String[] values = message.split(";");

		if (values.length != 2)
		{
			client.close();
			return;
		}

		switch (state)
		{
		case LOGIN_PASSH:
			login = values[0];
			passh = values[1];

			if (Files.exists(profilePath()))
			{
				Account etalon = json.parse(file.load(profilePath()), Account.class);

				if (passh.equals(md5.get(etalon.passh + salt)))
				{
					welcome(client);
				}
				else
				{
					client.send("wrong pass");
					state = states.FINISH;
				}
			}
			else
			{
				captcha = "captcha";
				client.send("regme:" + captcha);
				state = states.PASSH_CAPTCHA;
			}
			break;

		case PASSH_CAPTCHA:
			passh = values[0];

			if (Files.exists(profilePath()))
			{
				client.send("login exists");
				state = states.FINISH;
			}
			else if (false == captcha.equals(values[1]))
			{
				client.send("wrong captcha");
				state = states.FINISH;
			}
			else
			{
				if (!ensureAccountFolder())
				{
					client.close();
					return;
				}
				file.save(profilePath(), json.from(this));
				welcome(client);
			}
			break;

		case FINISH:
			client.close();
			break;
		}
	}

	private void welcome(ClientData client)
	{
		client.send("welcome");
		World.instance.broadcast("system: " + login + " has joined");
		World.instance.add(Player.instatiate(login, client));
	}

	private boolean ensureAccountFolder()
	{
		Path folderPath = Paths.get(Server.pathData, "users", login);
		if (false == Files.isDirectory(folderPath))
		{
			try
			{
				Files.createDirectories(folderPath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private Path profilePath()
	{
		return Paths.get(Server.pathData, "users", login, "profile.json");
	}

	private enum states
	{
		LOGIN_PASSH, PASSH_CAPTCHA, FINISH
	}
}
