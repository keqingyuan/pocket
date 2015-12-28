package com.axis.common.chipher;


import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.axis.common.Convert;
import com.axis.common.Stringutil;
import com.axis.common.Util;
import com.axis.common.exceptions.UtilException;

//import com.ft.ic.workSystem.excption.ErrMessage;

public class DES {
	
	private static final String Algorithm = "DES"; //定义 加密算法,可用 DES,DESede,Blowfish
	
	//keybyte为加密密钥，长度为8字节 
	//src为被加密的数据缓冲区（源） 
	/**
	 * DES加密
	 */
	private static byte[] encryptMode(byte[] keybyte, byte[] src) 
	{ 
		try { 
				//生成密钥 
				SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); 
				//加密 
				Cipher c1 = Cipher.getInstance(Algorithm); 
				c1.init(Cipher.ENCRYPT_MODE, deskey); 
				return c1.doFinal(src); 
		} 
		catch (java.security.NoSuchAlgorithmException e1)
		{ 
			e1.printStackTrace(); 
		} 
		catch (javax.crypto.NoSuchPaddingException e2) 
		{ 
			e2.printStackTrace(); 
		} 
		catch (java.lang.Exception e3)
		{ 
			e3.printStackTrace(); 
		} 
		return null; 
	} 

	//keybyte为加密密钥，长度为8字节 
	//src为加密后的缓冲区 
	/**
	 * DES解密
	 */
	private static byte[] decryptMode(byte[] keybyte, byte[] src) 
	{ 
		try { 
				//生成密钥 
				SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); 
				//解密 
				Cipher c1 = Cipher.getInstance(Algorithm); 
				c1.init(Cipher.DECRYPT_MODE, deskey); 
				return c1.doFinal(src); 
			} 
		catch (java.security.NoSuchAlgorithmException e1)
		{ 
			e1.printStackTrace(); 
		} 
		catch (javax.crypto.NoSuchPaddingException e2)
		{ 
			e2.printStackTrace(); 
		} 
		catch (java.lang.Exception e3) { 
			e3.printStackTrace(); 
		} 
		return null; 
	} 
	
	/**
	 * DES加密
	 * @param key 密钥  8字节
	 * @param data 需要加密的数据  8字节
	 * @return 加密后的数据
	 */
	@SuppressWarnings("restriction")
	public static byte[] encDES(String key,String data)
	{
		//添加新安全算法,如果用JCE就要把它添加进去 
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		byte[] szKey = Convert.toBytes(key);
		byte[] szData = Convert.toBytes(data);
		byte[] encoded = encryptMode(szKey, szData);
		int len = encoded.length-8;
		byte[] out = new byte[len];
		for(int i = 0;i<len;i++)
		{
			out[i]=encoded[i];
		}
		return out;		
	}

		
	/**
	 * DES解密
	 * @param key 密钥  8字节
	 * @param data 加密数据 8字节
	 * @return 解密后的数据
	 */
	@SuppressWarnings("restriction")
	public static byte[] decDES(String key,String data)
	{
		//添加新安全算法,如果用JCE就要把它添加进去 
		Security.addProvider(new com.sun.crypto.provider.SunJCE());	
		String buffer = "0808080808080808";
		byte[] szBuffer = Convert.toBytes(buffer);
		byte[] szKey = Convert.toBytes(key);
		szBuffer = encryptMode(szKey, szBuffer);
		buffer = Convert.toString(szBuffer);
		data = data+buffer;
		byte[] szData = Convert.toBytes(data);
		byte[] srcBytes = decryptMode(szKey, szData); 
		int len = srcBytes.length-8;
		byte[] out = new byte[len];
		for(int i = 0;i<len;i++)
		{
			out[i]=srcBytes[i];
		}
		return out;	
	}
	/**
	 * 3DES加密
	 * @param key密钥 16字节
	 * @param data加密数据 8字节的倍数
	 * @param isPadding true:数据后面补充8000...
	 * @return 加密后的数据
	 * @throws ErrMessage 
	 */
	public static String enc3DES(String key,String data, boolean isPadding)
	{
		if(isPadding)
			data = padding80(data);
		
		int keyLen = key.length();
		int dataLen = data.length();
		String key1 = new String();
		String key2 = new String();
		String out = new String();
		
		if(keyLen!=32)
		{
			throw new UtilException(Stringutil.format("keyLen of class enc3DES error: [{}]", keyLen));
		}
		if(dataLen%8!=0)
		{
			throw new UtilException(Stringutil.format("dataLen of class enc3DES error: [{}]", dataLen));
		}
		key1 = key.substring(0, 16);
		key2 = key.substring(16, 32);
		byte[]enc1 = encDES(key1,data);//key1对数据加密
		String buffer = Convert.toString(enc1);
		byte[]dec1 = decDES(key2,buffer);//用key2对上步中的输出数据进行解密
		String buffer2 = Convert.toString(dec1);
		byte[]enc2 = encDES(key1,buffer2);//key1对解密后的数据加密
		out = Convert.toString(enc2);
		out = out.toUpperCase();
		return out;
	}
	/**
	 * 3DES解密
	 * @param key 密钥 16字节
	 * @param data 加密数据 8字节的倍数
	 * @return 解密后的数据
	 */
	public static String dec3DES(String key,String data)
	{
		int keyLen = key.length();
		int dataLen = data.length();
		
		String key1 = new String();
		String key2 = new String();
		String out = new String();
		
		if(keyLen!=32)
		{
			return null;
		}
		if(dataLen%8!=0)
		{
			return null;
		}
		
		key1 = key.substring(0, 16);
		key2 = key.substring(16, 32);
		
		byte[]dec1 = decDES(key1,data);//用key1对加密数据解密
		String buffer = Convert.toString(dec1);
		
		byte[]enc1 = encDES(key2,buffer);//用key2对上面输出的解密数据加密
		String buffer2 = Convert.toString(enc1);
		
		byte[]dec2 = decDES(key1,buffer2);//用key1对加密数据解密
		out = Convert.toString(dec2);
		
		out = out.toUpperCase();
		
		return out;
	}
	/**
	 * 分散算法
	 * @param key 16个字节
	 * @param data 8个字节
	 * @return
	 * @throws ErrMessage 
	 */
	public static String diversify(String key,String data)
	{
		int keyLen = key.length();
		int dataLen = data.length();
		String buffer = new String();
		if(keyLen!=32)
		{
			throw new UtilException(Stringutil.format("Length of keyLen [{}] is not 32", keyLen));
		}
		if(dataLen!=16)
		{
			throw new UtilException(Stringutil.format("Length of dataLen [{}] is not 16", dataLen));
		}
		byte[] buf = Convert.toBytes(data);
		for(int i=0;i<8;i++)
		{
			byte temp = buf[i];
			buf[i]=(byte) (~temp);
		}
		buffer = Convert.toString(buf);		
		buffer = data+buffer;
		String out = enc3DES(key,buffer,false);	
		return out;
	}
	/**
	 * 3des_encode_cbc加密
	 * @param iv 初始化向量
	 * @param key 密钥数据
	 * @param data 加密数据
	 * @return 加密获得数据
	 * @throws ErrMessage 
	 */
	public static String Encode_3DES_CBC(String iv,String key,String data) throws Exception
	{
		String buffer1 = new String();
		String xor = new String();
		String buffer3DES = new String();
		String _3DES = new String();
		int len ;
		
		//判断iv,key,data数据长度的正确性
		len = data.length();
		if(iv.length() != 16||key.length() != 32||len%16 != 0||len == 0)
		{
			return null;
		}		
		
		//拆分成N组（N*16）
		//第一组
		buffer1 = data.substring(0, 16);
		//buffer1与初始化向量做异或运算
		xor = Util.calXOR(iv, buffer1, 8);
		//对异或结果做3DES加密
		buffer3DES = DES.enc3DES(key, xor, false);
		_3DES = buffer3DES;
		//第二组开始循环
		for(int i =1; i<len/16;i++)
		{
			String buffer = new String();
			//第i组数据
			buffer = data.substring(16*i, 16+16*i);
			//做异或运算
			xor = Util.calXOR(buffer3DES, buffer, 8);
			//3DES加密
			buffer3DES = DES.enc3DES(key, xor, false);
			_3DES = _3DES+buffer3DES;	
		}
					
		return _3DES;
		
	}
	/**
	 * 字符串后面补充80...数据
	 * @param str 需要补充80...的数据
	 * @return 补充好80...后的数据
	 */
	public static String padding80(String str)
	{
		//计算字符串长度
		int len = str.length()/2;
		String buf = "8000000000000000";
		//计算需要补充的位数
		int l = 8-len%8;
		//补充80...数据
		str+=buf.substring(0, l*2);
	
		return str;
	}
	
}
