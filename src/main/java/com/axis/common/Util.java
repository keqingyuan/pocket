package com.axis.common;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.axis.common.exceptions.UtilException;

public class Util {

	private Util() {

	}

	public static byte[] toBytes(int a) {
		return new byte[] { (byte) (0x000000ff & (a >>> 24)), (byte) (0x000000ff & (a >>> 16)),
				(byte) (0x000000ff & (a >>> 8)), (byte) (0x000000ff & (a)) };
	}

	public static byte[] toBytes(short a) {
		return new byte[] { (byte) (0x000000ff & (a >>> 8)), (byte) (0x000000ff & (a)) };
	}

	/**
	 * 十六进制字符串转换成字节数组，两个字符为一个字节
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] toBytes(String hexStr) {
		int i = hexStr.length();
		int k = 0;
		if (hexStr == null || i == 0 || i % 2 != 0) {
			return null;
		}
		byte[] result = new byte[i / 2];
		for (k = 0; k < i / 2; k++) {
			String buffer = hexStr.substring(2 * k, 2 * k + 2);
			int temp = (int) Long.parseLong(buffer, 16);
			result[k] = (byte) temp;
		}
		return result;
	}

	public static byte[] toBytes2(String hexStr) {
		if (hexStr == null || hexStr.equals("")) {
			return null;
		}
		hexStr = hexStr.toUpperCase();
		int length = hexStr.length() / 2;
		byte[] d = new byte[length];
		char[] hexChars = hexStr.toCharArray();
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) ((byte) HEX_ATOM.indexOf(hexChars[pos]) << 4 | (byte) HEX_ATOM.indexOf(hexChars[pos + 1]));
		}
		return d;
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
	 * 
	 * @param d
	 *            字节数组
	 * @param s
	 *            需要转换字节的起始位置
	 * @param n
	 *            数组的长度
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
	 *            要进行异或计算的数据长度(字符串长度的一半)
	 * @return 异或计算返回值
	 * @throws ErrMessage
	 */
	public static String calXOR(String Param1, String Param2, int len) {
		byte[] p1, p2;
		byte[] buffer = new byte[len];
		p1 = Convert.toBytes(Param1);
		p2 = Convert.toBytes(Param2);
		if (len <= 0) {
			throw new UtilException(Stringutil.format("Length of class ftCalXOR error: [{}]", len));
		}

		String str = new String();
		for (int i = 0; i < len; i++) {
			buffer[i] = (byte) (p1[i] ^ p2[i]);

		}
		str = str + Convert.toString(buffer);
		return str;
	}

	/**
	 * 异或计算函数
	 *
	 * @param p1
	 *            要进行异或计算的第一个参数
	 * @param p2
	 *            要进行异或计算的第二个参数
	 * @param len
	 *            要进行异或计算的数据长度(字符串长度的一半)
	 * @return 异或计算返回值
	 * @throws ErrMessage
	 */
	public static byte[] calXOR(byte[] p1, byte[] p2, int len) {
		if (p1 != null && p2 != null && len > 0) {
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++) {
				buffer[i] = (byte) (p1[i] ^ p2[i]);
			}
			return buffer;
		}
		return null;
	}

	public static byte[] getRandom(int len) {
		// 定义一个空的字符串
		String result = "";
		// 进行len次循环
		for (int i = 0; i < len; i++) {
			// 生成一个32--127的int型整数
			int intVal = (int) (Math.random() * 96 + 32);
			// 将int 强制转换为char后连接到result后面
			result = result + (char) intVal;
		}
		return result.getBytes();
	}

	/**
	 * @param format
	 *            0:yyyyMMdd 1:yyyy-MM-dd 2:yyyy/MM/dd 3:yyMMdd
	 * @return 系统日期
	 * @函数名称：getSysDate
	 * @函数功能：获取当前系统日期
	 */
	public static byte[] getSysDate(int format) {
		String time = null;
		Date date = new Date();
		SimpleDateFormat dateFormat = null;
		switch (format) {
		case 0:
			dateFormat = new SimpleDateFormat("yyyyMMdd");
			break;
		case 1:
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			break;
		case 2:
			dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			break;
		case 3:
			dateFormat = new SimpleDateFormat("yyMMdd");
			break;
		default:
			dateFormat = new SimpleDateFormat("yyyyMMdd");
			break;
		}
		time = dateFormat.format(date);
		return toBytes(time);
	}

	/**
	 * @param format
	 *            0: HHmmss 1: HH:mm:ss 2: HHmmssSSS 3: HH:mm:ss:SSS
	 * @return
	 * @函数名称 getSysTime
	 * @说明 获取当前系统时间
	 */
	public static byte[] getSysTime(int format) {
		String time = null;
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = null;
		switch (format) {
		case 0:
			dateFormat = new SimpleDateFormat("HHmmss");
			break;
		case 1:
			dateFormat = new SimpleDateFormat("HH:mm:ss");
			break;
		case 2:
			dateFormat = new SimpleDateFormat("HHmmssSSS");
			break;
		default:
			dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
			break;
		}
		time = dateFormat.format(date);
		return toBytes(time);
	}

	public static int memcmp(byte[] buffer1, byte[] buffer2, int length) {
		return memcmp(buffer1, 0, length, buffer2, 0, length);
	}

	public static int memcmp(byte[] buffer1, int offset1, int length1, byte[] buffer2, int offset2, int length2) {

		if (buffer1 == buffer2 && offset1 == offset2 && length1 == length2)
			return 0;

		int end1 = offset1 + length1;
		int end2 = offset2 + length2;

		for (int i = offset1, j = offset2; i < end1 && j < end2; i++, j++) {
			int a = (buffer1[i] & 0xff);
			int b = (buffer2[j] & 0xff);
			if (a != b)
				return a - b;
		}
		return length1 - length2;
	}

	public static byte[] padding(byte[] msg, int msgLen, byte[] padding) {
		int l = 8 - (msgLen % 8);
		byte[] result = Arrays.copyOf(msg, msg.length + l);
		System.arraycopy(padding, 0, result, msg.length, l);
		return result;
	}

	private final static char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };
	public static final String HEX_ATOM = "0123456789ABCDEF";
	// padding 0x80 0x00 0x00 0x00
	public static final byte[] PADDING_80 = { (byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	// padding 0x00 0x00 0x00 0x00
	public static final byte[] PADDING_00 = { (byte) 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

}
