package ru.serjik.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class json
{
	private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public final static String from(Object object)
	{
		return gson.toJson(object);
	}
	
	public final static <T> T parse(String json, Class<T> classOfT)
	{
		return gson.fromJson(json, classOfT);		
	}
}
