package com.axis.common.chipher;

import com.axis.common.Stringutil;
import com.axis.common.ftBaseFunc;
import com.axis.common.exceptions.UtilException;

public class MAC {
	static ftBaseFunc ftPublic = new ftBaseFunc();
	public static final char IS_SHORT = 1;
	public static final char IS_LONG = 0;
	/**
	 * MAC计算(des_des_mac)
	 * @param key        加密密钥 
	 * @param initData   计算MAC的初始化向量（8个字符的ASCII）
	 * @param data        需要加密的数据（任意长度ASCII）
	 * @return
	 * @throws ErrMessage 
	 */
	public static String macForDes_Des(String key,String initData,String data)
	{
		
		byte[] szTmp =null;
		String out = new String();
		String buffer = new String();
		String xorStr = new String();
		int dataLen = 0;
		
		if(key.length() < 16||initData.length() != 16)
		{
			throw new UtilException(Stringutil.format("key Length is [{}] or initData Length is [{}] of class macForDes_Des error", key.length(), initData.length()));
		}
		
		dataLen = data.length();
		
		if(dataLen%2 != 0)
		{
			throw new UtilException(Stringutil.format("dataLen is [{}] of class macForDes_Des error", dataLen));
		}
		
		/*dataLen = dataLen/2;
		
		//对数据进行补位，字节数不是8的倍数补“80 00 ・・・・”
		if(dataLen%8 != 0)
		{
			int addLen = 8-dataLen%8;
			data = data+"80";
			for(int i=1;i<addLen;i++ )
			{
				data+="00";
			}
		}*/
		//对数据进行补位，字节数不是8的倍数补“80 00 ・・・・”
		data = ftPublic.ftFixed80(data);
		
		//80补齐后数据长度（字符）
		int newLen = data.length();
		//每8字节分割一组（字符串长度为16个字符）
		int n = newLen/16;
		
		buffer = initData;
		
		key = key.substring(0, 16);//取密钥前8个字节用于DES加密
		//异或后DES加密
		for(int i=0;i<n;i++)
		{
			String D = data.substring(i*16, i*16+16);
			xorStr = ftPublic.ftCalXOR(buffer, D, 8);
			szTmp = DES.encDES(key, xorStr);
			buffer = ftPublic.ftByteToString(szTmp);
		}
		
		out = buffer.substring(0, 8);//取前四个字节数据
		out = out.toUpperCase();     //将数据中的字母都转化为大写字母
		
		return out;		
	 }
	
	/**
	 * MAC计算(des_3des_mac)
	 * @param key        加密密钥 长度为(16字节，32个字符)
	 * @param initData   计算MAC的初始化向量（8个字符的ASCII）
	 * @param data        需要加密的数据（任意长度ASCII）
	 * @return
	 * @throws ErrMessage 
	 */
	public static String macForDes_3Des(String key,String initData,String data, char isShort)
	{
		byte[] szTmp =null;
		String out = new String();
		String buffer = new String();
		String xorStr = new String();
		String lastD = new String();
		int dataLen = 0;
		
		if(key.length() < 32||initData.length() != 16)
		{
			throw new UtilException(Stringutil.format("key Length is [{}] or initData Length is [{}] of class macForDes_Des error", key.length(), initData.length()));
		}
		
		dataLen = data.length();
		
		if(dataLen%2 != 0)
		{
			throw new UtilException(Stringutil.format("dataLen is [{}] of class macForDes_Des error", dataLen));
		}
		
		
		//对数据进行补位，字节数不是8的倍数补“80 00 ・・・・”
		data = ftPublic.ftFixed80(data);
		//80补齐后数据长度（字符）
		int newLen = data.length();
		//每8字节分割一组（字符串长度为16个字符）
		int n = newLen/16;
		
		buffer = initData;
		
		//取密钥前8个字节，用于DES加密
		String newkey = key.substring(0, 16);
		//对非最后一个模块做异或后的DES加密
		for(int i=0;i<n-1;i++)
		{
			String D = data.substring(i*16, i*16+16);
			xorStr = ftPublic.ftCalXOR(buffer, D, 8);
			szTmp = DES.encDES(newkey, xorStr);
			buffer = ftPublic.ftByteToString(szTmp);
		}
		//最后一组数据
		lastD = data.substring(newLen-16,newLen);
		//对最后一组数据做异或
		xorStr = ftPublic.ftCalXOR(buffer, lastD, 8);
		//对最后一组数据做3DES加密
		buffer = DES.enc3DES(key, xorStr, false);
		if(isShort == IS_SHORT){
			//取前四个字节数据
			out = buffer.substring(0, 8);
			out = out.toUpperCase();     //将数据中的字母都转化为大写字母
		}else{
			out = buffer.toUpperCase();
		}
		
		return out;		
	 }
}
