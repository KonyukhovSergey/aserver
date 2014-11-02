package ru.serjik.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class file
{
	private final static Charset utf8 = Charset.forName("UTF-8");

	public final static String load(Path path)
	{
		try
		{
			return new String(Files.readAllBytes(path), utf8);
		}
		catch (IOException e)
		{
			// e.printStackTrace();
			return "";
		}
	}

	public final static void save(Path path, String data)
	{
		try
		{
			Files.write(path, data.getBytes(utf8));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
