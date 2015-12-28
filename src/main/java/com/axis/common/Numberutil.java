package com.axis.common;

public class Numberutil {

	public static int Hex2Dec(String HexStr)
	{
		String s = "0123456789abcdef";
		HexStr = HexStr.toLowerCase();
		int len = HexStr.length();

		int Dec = 0;
		for(int i=0;i<len;i++)
		{
			int buf = s.indexOf(HexStr.charAt(i));
			Dec = (int) (Dec+buf*Math.pow(16,(len-i-1)));
		}
		return Dec;
	}
}
