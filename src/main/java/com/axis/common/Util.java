package com.axis.common;

import com.axis.common.exceptions.UtilException;

public class Util {
	private final static char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	private Util() {
	}

	public static byte[] toBytes(int a) {
		return new byte[] { (byte) (0x000000ff & (a >>> 24)), (byte) (0x000000ff & (a >>> 16)),
				(byte) (0x000000ff & (a >>> 8)), (byte) (0x000000ff & (a)) };
	}
	/**
	 * 字符串转换成字节数组，两个字符为一个字节
	 * @param str
	 * @return
	 */
	public static byte[] toBytes(String str){
		int i = str.length();
		int k = 0;
		if(str == null||i==0||i%2 != 0)
		{
			return null;
		}
		byte[] result = new byte[i/2];
		for( k=0;k<i/2;k++)
		{
			String buffer = str.substring(2*k, 2*k+2);
			int temp = (int) Long.parseLong(buffer, 16);
			result[k] = (byte)temp;			
		}			
		return result;
	}

	public static boolean testBit(byte data, int bit) {
		final byte mask = (byte) ((1 << bit) & 0x000000FF);

		return (data & mask) == mask;
	}

	public static int toInt(byte[] b, int s, int n) {
		int ret = 0;

		final int e = s + n;
		for (int i = s; i < e; ++i) {
			ret <<= 8;
			ret |= b[i] & 0xFF;
		}
		return ret;
	}

	public static int toIntR(byte[] b, int s, int n) {
		int ret = 0;

		for (int i = s; (i >= 0 && n > 0); --i, --n) {
			ret <<= 8;
			ret |= b[i] & 0xFF;
		}
		return ret;
	}

	public static int toInt(byte... b) {
		int ret = 0;
		for (final byte a : b) {
			ret <<= 8;
			ret |= a & 0xFF;
		}
		return ret;
	}

	public static int toIntR(byte... b) {
		return toIntR(b, b.length - 1, b.length);
	}

	public static String toHexString(byte... d) {
		return (d == null || d.length == 0) ? "" : toHexString(d, 0, d.length);
	}

	public static String toHexString(byte[] d, int s, int n) {
		final char[] ret = new char[n * 2];
		final int e = s + n;

		int x = 0;
		for (int i = s; i < e; ++i) {
			final byte v = d[i];
			ret[x++] = HEX[0x0F & (v >> 4)];
			ret[x++] = HEX[0x0F & v];
		}
		return new String(ret);
	}

	/**
	 * byte数组转换成十六进制字符串
	 * @param d 字节数组
	 * @param s 需要转换字节的起始位置
	 * @param n 数组的长度
	 * @return
	 */
	public static String toHexStringR(byte[] d, int s, int n) {
		final char[] ret = new char[n * 2];

		int x = 0;
		for (int i = s + n - 1; i >= s; --i) {
			final byte v = d[i];
			ret[x++] = HEX[0x0F & (v >> 4)];
			ret[x++] = HEX[0x0F & v];
		}
		return new String(ret);
	}

	public static String ensureString(String str) {
		return str == null ? "" : str;
	}

	public static String toStringR(int n) {
		final StringBuilder ret = new StringBuilder(16).append('0');

		long N = 0xFFFFFFFFL & n;
		while (N != 0) {
			ret.append((int) (N % 100));
			N /= 100;
		}

		return ret.toString();
	}

	public static int parseInt(String txt, int radix, int def) {
		int ret;
		try {
			ret = Integer.valueOf(txt, radix);
		} catch (Exception e) {
			ret = def;
		}

		return ret;
	}

	public static int BCDtoInt(byte[] b, int s, int n) {
		int ret = 0;

		final int e = s + n;
		for (int i = s; i < e; ++i) {
			int h = (b[i] >> 4) & 0x0F;
			int l = b[i] & 0x0F;

			if (h > 9 || l > 9)
				return -1;

			ret = ret * 100 + h * 10 + l;
		}

		return ret;
	}

	public static int BCDtoInt(byte... b) {
		return BCDtoInt(b, 0, b.length);
	}

	/**
	 * 异或计算函数
	 * 
	 * @param Param1
	 *            要进行异或计算的第一个参数
	 * @param Param2
	 *            要进行异或计算的第二个参数
	 * @param len
	 *            要进行异或计算的数据长度
	 * @return 异或计算返回值
	 */
	public static byte[] calXOR(byte[] Param1, byte[] Param2, int len) {
		if (len <= 0) {
			return null;
		}
		byte[] buffer = new byte[len];

		for (int i = 0; i < len; i++) {
			buffer[i] = (byte) (Param1[i] ^ Param2[i]);
		}
		return buffer;

	}
	/**
	 * 异或计算函数
	 * @param Param1 要进行异或计算的第一个参数
	 * @param Param2 要进行异或计算的第二个参数
	 * @param len   要进行异或计算的数据长度(字符串长度的一半)
	 * @return      异或计算返回值
	 * @throws ErrMessage 
	 */
	public static String calXOR(String Param1,String Param2,int len) 
	{
		byte[] p1 ,p2;
		byte[] buffer = new byte[len];
		p1 = Convert.toBytes(Param1);
		p2 = Convert.toBytes(Param2);
		if(len <= 0 )
		{
			throw new UtilException(Stringutil.format("Length of class ftCalXOR error: [{}]", len));
		}

		String str = new String();
		for(int i=0;i<len;i++)
		{
			buffer[i] = (byte) (p1[i]^p2[i]);
			
		}
		str = str+Convert.toString(buffer);
		return str;		
	}
	
}
