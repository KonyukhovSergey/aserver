package ru.serjik.utils;

public class HexUtils
{
	public static final float DELTA_WIDTH = (float) (Math.sqrt(3) / 2);
	private static final float DELTA_HEIHT = 0.75f;

	private static final byte[] direction = { -1, 5, 0, 4, -1, 1, 3, 2, -1 };

	public static final byte[] dq = { 1, 1, 0, -1, -1, 0 };
	public static final byte[] dr = { -1, 0, 1, 1, 0, -1 };
	public static final float[] angle = { 120, 180, 240, 300, 0, 60 };

	public static final int distance(int q1, int r1, int q2, int r2)
	{
		return (Math.abs(q1 - q2) + Math.abs(r1 - r2) + Math.abs(q1 + r1 - q2 - r2)) / 2;
	}

	public static final int direction(int dq, int dr)
	{
		return direction[dq + 1 + (dr + 1) * 3];
	}

	public static final float x(int q, int r)
	{
		return q * DELTA_WIDTH + r * DELTA_WIDTH * 0.5f;
	}

	public static final float y(int r)
	{
		return r * DELTA_HEIHT;
	}
}
