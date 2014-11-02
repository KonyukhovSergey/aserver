package ru.serjik.agame;

import ru.serjik.utils.HexUtils;

public class HexLocation
{
	public int q, r;
	public int direction;

	public void rotate(int hexAngle)
	{
		direction = (direction + hexAngle + 6) % 6;
	}

	public void move(int deltaForward)
	{
		q += HexUtils.dq[direction] * deltaForward;
		r += HexUtils.dr[direction] * deltaForward;
	}

	public String location()
	{
		return ";" + q + ";" + r + ";" + direction;
	}
}
