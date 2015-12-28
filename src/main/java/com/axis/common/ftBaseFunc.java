package com.axis.common;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.axis.common.exceptions.UtilException;

public class ftBaseFunc {

	public final int FTOK = 0;
	public final int EASY_TYPE = 1;    //简单类型
	public final int STRUCTURE_TYPE=0; //结构类型
	
	public int ftHexToDec(String HexStr)
	{
		String s = "0123456789abcdef";
		HexStr = HexStr.toLowerCase();
		int len = HexStr.length();
//		if(len%2!=0)
//		{
//			return -1;
//		}
		int Dec = 0;
		for(int i=0;i<len;i++)
		{
			int buf = s.indexOf(HexStr.charAt(i));
			Dec = (int) (Dec+buf*Math.pow(16,(len-i-1)));
		}
		return Dec;
	}
	/**
	 * @函数名称：十六进制字符串转换成长整形
	 * @说明：20ef -> 8431
	 * @param HexStr
	 * @return long
	 */
	public long ftHexToLong(String HexStr) {

		long lVal = 0;
		int nLen;
		
		nLen = HexStr.length();
		if(nLen >8 ){
			return -1;
		}
		String validate = "(?i)[0-9a-f]+";  
        if (!HexStr.matches(validate))
        	return -2;
        lVal = Long.parseLong(HexStr, 16);
		return lVal;
	}
	/******************************************************************************************
	 *******	函数名称：ftLongToHex	   -序号：1.4-									*******
	 *******	函数功能：十进制金额值转换成16进制金额字符串							*******
	 *******----------------------------------------------------------------------------*******
	 *******	函数参数：																*******
	 *******			long Val：要转换的十进制金额值									*******
	 *******			char *hexstr:(输出)转换生成的16进制金额值字符串					*******
	 *******			int Flag：金额值输出类型标志；缺省值为1：BCD码；0：16进制字符串	*******
	 *******----------------------------------------------------------------------------*******
	 *******	返 回 值：成功：失败返回：-1；成功返回：生成的16进制金额值字符串长度	*******
	 ******************************************************************************************/
	public int ftLongToHex(long Val, StringBuffer dest, int flag){
		int nLen;
		long MaxVal = 2147483647;
		long aHexVal[] = new long[10];
		long lVal;
		long lInVal = 0;
		char[] szHexStr = new char[11];
		char[] szAscStr = new char[11];
		
		if(Val > MaxVal || Val < 0)
			return -1;
		int i = 0;
		lInVal = Val;
		while(true){
			lVal = lInVal % 16;
			aHexVal[i] = lVal;
			i++;
			lInVal = lInVal / 16;
			if(lInVal == 0)
				break;
		}
		//ASCII码数据长度
		nLen = i;
		for (i = 0; i < nLen; i++) {
			char ch;
			if(aHexVal[i]>=0 && aHexVal[i]<=9){
				ch = (char) (aHexVal[i] + 48);
			}
			else{
				ch = (char) (((int)aHexVal[i]-10) + 'A');
			}
			szAscStr[i] = ch;
		}
		for(i=0; i<nLen; i++){
			szHexStr[nLen-1-i] = szAscStr[i];
		}
		i = nLen % 2;
		if(i > 0){
			szAscStr[0] = '0';
			for(int j =0; j<nLen; j++){
				szAscStr[j+1] = szHexStr[j];
			}
			nLen += 1;
		}else
			szAscStr = szHexStr;
		
		//清空HexStr
		dest.setLength(0);
		
		//flag = 0,返回的金额为十六进制的ASCII码字符串金额
		if(flag == 0){
			dest.append(String.valueOf(szAscStr).trim());
			return dest.length();
		}
		//falg =1,返回的金额为BCD码的金额
		nLen = nLen /2;
		StringBuffer out = new StringBuffer();
		ftAtoh(szAscStr.toString(), out, nLen);
		dest.append(out);
		return 0;
	}
	public int ftAtoh(String source, StringBuffer dest, int bcdlen) {
		char hi,lo;
		int i,n;
		int nLen;
		char ascstr[] = source.toCharArray();
		
		// 获取ASCII长度
		nLen = source.length();
		// 检查要转换的ASCII字符串是否合法，合法的ASCII字符为：‘0’~‘9’、	'A'~'F'、	'a'~'f'
		for (i = 0; i < nLen; i++) {
			// 检查传入的字符是否在合法的BCD码字符范围内
			if( !( (ascstr[i] >='0' && ascstr[i] <='9') || (ascstr[i] >='A'  && ascstr[i] <='F')  || (ascstr[i] >='a' && ascstr[i] <='f') ))
			{
				return -1;
			}
		}
		// 检查要转换的ASCII字符串长度和要生成的BCD码长度是否合法
		if( nLen < bcdlen*2)
		{
			return -2;
		}
		char bcdstr[] = new char[nLen/2];
		for (i=n=0;n<nLen/2;) {
			hi = Character.toUpperCase(ascstr[i++]);
			lo = Character.toUpperCase(ascstr[i++]);
			char ch = (char) ((((hi>='A')?(hi-'A'+10):(hi-'0'))<<4) | ((lo>='A')?(lo-'A'+10):(lo-'0')));
			bcdstr[n++]= ch;
		}
		//清空dest
		dest.setLength(0);
		dest.append(bcdstr);
		return 0;
	}
	public int ftBinToHex(String source, StringBuffer dest) {
		int sourceLen = source.length();
		int index = sourceLen/4;
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < index; i++) {
			int nReturn = 0;
			//获取第一个4位的段存入临时变量temp中
			String temp = source.substring(i*4, (i+1)*4);
			int Len = temp.length();
			for (int j = 0; j < Len; j++) {
				if(temp.toCharArray()[j] == '1'){
					int nVal = (int) Math.pow((double)2,(int)Len-1-j);
					if (nVal >0){
						nReturn += nVal;
					}
				}
			}
			if(nReturn <9 )
			{
				out.append(nReturn);
			}
			else{
				switch (nReturn) {
				case 10:
					out.append("A");
					break;
				case 11:
					out.append("B");
					break;
				case 12:
					out.append("C");
					break;
				case 13:
					out.append("D");
					break;
				case 14:
					out.append("E");
					break;
				case 15:
					out.append("F");
					break;

				default:
					break;
				}
			}
		}
		//清空dest
		dest.setLength(0);
		dest.append(out);
		return 0;
	}
	/******************************************************************************************
	 *******	函数名称：ftDataToBitStringNoASCII	   -序号：1.11.1-								*******
	 *******	函数功能：将BCD码数据转换成BIT字符串1字节转换成8个0、1的字符函数		            *******
	 *******----------------------------------------------------------------------------*******
	 *******	函数参数：char *OutBuf：转换生成的BIT字符数组							        *******
	 *******            char *InData：要进行转换的数据指针							        *******
	 *******			int   Len:数据长度                                               *******
	 *******            例如：01—>0000 0001                                              *******
	 *******----------------------------------------------------------------------------*******
	 *******	返 回 值：成功返回：0；失败返回：-1		          						    *******
	 ******************************************************************************************/
	public int ftDataToBitStringNoASCII(String source,
			StringBuilder dest) {
		if(source.length()<0)
			return -1;
		String temp = null;
		//清空dest
		dest.setLength(0);
		for(int i=0; i< source.length(); i++){
			char ch = source.toCharArray()[i];
			StringBuffer out = new StringBuffer();
			ftCharToBitString(ch, out);
			temp = out.substring(out.length()-4, out.length());
			dest.append(temp);
		}
		return 0;
	}
	public int ftCharToBitString(char source, StringBuffer dest){
		StringBuffer szBitString = new StringBuffer("00000000");
		int nVal;
		for (int i = 0; i < 8; i++) {
			nVal = (int) Math.pow((float)2, (int)7-i);
			int flag = Character.getNumericValue(source) & nVal;
//			System.out.print(flag);
			if(flag != 0)
				szBitString.setCharAt(i, '1');
		}
		//清空dest
		dest.setLength(0);
		dest.append(szBitString);
		return 0;
	}
	/**
	 * @函数名称：ftGetSysTime
	 * @函数功能：获取当前系统日期
	 * @param format 0:yyyyMMdd
	 * 				 1:yyyy-MM-dd
	 * 				 2:yyyy/MM/dd
	 * @return 系统日期
	 */
	public String ftGetSysDate(int format)
	{
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
		default:
			dateFormat = new SimpleDateFormat("yyyyMMdd");
			break;
		}
		time = dateFormat.format(date);
		return time;
	}
	/**
	 * @函数名称 ftGetSysTime
	 * @说明 获取当前系统时间
	 * @param format 0: HHmmss
	 *               1: HH:mm:ss
	 *               2: HHmmssSSS
	 *               3: HH:mm:ss:SSS
	 * @return
	 */
	public String ftGetSysTime(int format){
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
		return time;
	}
	public byte[] ftHexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (ftCharToByte(hexChars[pos]) << 4 | ftCharToByte(hexChars[pos + 1]));
		}
		return d;
	}
	/**
	 * 
	 * @param c
	 * @return
	 */
	public byte ftCharToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	//FIXME:分割
	/**
	 * 函数名称：ftDecToHex(int input)
	 *    功能：将十进制数据转换成两字节的十六进制
	 * 参数注释：input 为需要转换的十进制数据
	 * 
	 */
	public String ftDecToHex(int input)
	{
		
		int i;
		String str=Integer.toHexString(input);
		i=str.length();
		if(i%2!=0)
		{
			str="0"+str;
		}else if(i==0)
		{
			return null;
		}
		return str;
		
	}
	/**
	 * 函数名称：byteToString(byte[] buffer, int l, int i)
	 * 功能：          将数组中指定位置间的元素由byte型转换为String(ACSII)
	 * 参数注释：@buffer 内容数组
	 * 			@l   数组内的起始位置下标
	 * 			@i   连续元素的个数
	 *     
	 * 返回值：    @str 转换后的String型数据
	 * 
	 */
	public String ftByteToString(byte[] buffer, int l, int i) 
	{
		String str = new String();
		if(i==0&&buffer==null)
		{
			return null;
		}
		for(int k=0;k<i;k++)
		{
			int temp=buffer[l+k];
			if(temp<0)
			{
				temp=buffer[l+k]&0xff; 
			}
			
			String buf=Integer.toHexString(temp);
			
			if(buf.length()==1)
			{
				buf="0"+buf;
			}
			str+=buf;		
		}
		
		return str;
	}
	/**
	 * 函数名称：byteToString(byte[] buffer) 
	 * 功能：          将数组中指定位置间的元素由byte型转换为String(ACSII)
	 * 参数注释：@buffer 内容数组
	 *     
	 * 返回值：    @str 转换后的String型数据
	 * 
	 */
	public String ftByteToString(byte[] buffer) 
	{
		String str = new String();
		if(buffer==null)
		{
			return null;
		}
		int i = buffer.length;
		for(int k=0;k<i;k++)
		{
			int temp=buffer[k];
			if(temp<0)
			{
				temp=buffer[k]&0xff; 
			}
			
			String buf=Integer.toHexString(temp);
			
			if(buf.length()==1)
			{
				buf="0"+buf;
			}
			str+=buf;		
		}
		
		return str;
	}
	/**
	 * ASCII字符串数据转化为byte型
	 * @param str
	 * @return
	 */
	public byte[] ftStringToByte(String str)
	{
		int i = str.length();
		int k = 0;
		if(str == null||i==0||i%2 != 0)
		{
//			throw new ErrMessage("The String of class stringToByte error!");
		}
		byte[] result = new byte[i/2];
		for( k=0;k<i/2;k++)
		{
			String buffer = str.substring(2*k, 2*k+2);
			int temp = ftHexToDec(buffer);
			result[k] = (byte)temp;			
		}			
		return result;
	}
	/**
	 * ASCII转化为BCD
	 * @param ASCIIs（GBK数据）
	 * @return
	 */
	public String ftASCIIToBCD(byte[] ASCIIs) {  
        String result = null;
		try {
			result = new String(ASCIIs,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;  
    }
	/**
	 * ASCII转化为BCD(应用于具有汉字数据的转换)
	 * @param ASCIIs（GBK数据）
	 * @return
	 */
	public String ftASCIIToBCDForGBK(String ASCIIs) {
		byte[] bufByte = ftStringToByte(ASCIIs);
		 String result = null;
			try {
				result = new String(bufByte,"GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return result;  
		
	}
	/**
	 * 
	 * @param ASCII
	 * @return
	 */
	public char ftASCIIToChar(int ASCII) {  
		 if(ASCII==0)
			 return '0';
		 else
	        return (char) ASCII;
	}
	/**
	 * BCD字符串转换为ASCII码   
	 * @param s
	 * @return
	 */
	public String ftBCDToASCII(String s) {// 字符串转换为ASCII码   
        if (s == null || "".equals(s)) {  
        	throw new UtilException(Stringutil.format("The String [{}] is null!", s));
        }
  
        char[] chars = s.toCharArray();  
        int[] asciiArray = new int[chars.length];  
        String result = new String();
  
        for (int i = 0; i < chars.length; i++) {  
            asciiArray[i] = ftCharToASCII(chars[i]); 
            result = result + ftDecToHex(asciiArray[i]);
        }  
        return result;  
    }
	/**
	 * BCD字符串转换为ASCII码   (主要处理汉字转化问题)
	 * @param s 
	 * @return
	 */
	public int[] ftBCD2ASCII(String s) {// 字符串转换为ASCII码   
        if (s == null || "".equals(s)) {  
            return null;  
        }  
  
        char[] chars = s.toCharArray();  
        int[] asciiArray = new int[chars.length];  
  
        for (int i = 0; i < chars.length; i++) {  
            asciiArray[i] = ftCharToASCII(chars[i]);  
        }  
        return asciiArray;  
    }
	/**
	 * 
	 * @param s
	 * @return
	 */
	public byte[] ftBCD2ASCIIForByte(String s) {// 字符串转换为ASCII码   
        if (s == null || "".equals(s)) {  
            return null;  
        }  
  
        char[] chars = s.toCharArray();  
        byte[] asciiArray = new byte[chars.length];  
  
        for (int i = 0; i < chars.length; i++) {  
            asciiArray[i] = ftCharToASCIIForByte(chars[i]);  
        }  
        return asciiArray;  
    }
	/**
	 * 
	 * @param c
	 * @return
	 */
	public int ftCharToASCII(char c) {  
        return (int) c;  
    }
	/**
	 * 
	 * @param c
	 * @return
	 */
	public byte ftCharToASCIIForByte(char c) {  
        return (byte) c;  
    }
	/**
	 * 异或计算函数
	 * @param Param1 要进行异或计算的第一个参数
	 * @param Param2 要进行异或计算的第二个参数
	 * @param len   要进行异或计算的数据长度
	 * @return      异或计算返回值
	 */
	public byte[] ftCalXOR(byte[]Param1,byte[]Param2,int len)
	{		
		if(len <= 0 )
		{
//			throw new ErrMessage("len of class ftCalXOR error!");
		}
		byte[] buffer = new byte[len];
		
		for(int i=0;i<len;i++)
		{
			buffer[i] = (byte) (Param1[i]^Param2[i]);
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
	public String ftCalXOR(String Param1,String Param2,int len) 
	{
		byte[] p1 ,p2;
		byte[] buffer = new byte[len];
		p1 = ftStringToByte(Param1);
		p2 = ftStringToByte(Param2);
		if(len <= 0 )
		{
			throw new UtilException(Stringutil.format("Length of class ftCalXOR error: [{}]", len));
		}

		String str = new String();
		for(int i=0;i<len;i++)
		{
			buffer[i] = (byte) (p1[i]^p2[i]);
			
		}
		str = str+ftByteToString(buffer);
		return str;		
	}
	/**
	 * 字符串后面补充80...数据
	 * @param str 需要补充80...的数据
	 * @return 补充好80...后的数据
	 */
	public String ftFixed80(String str)
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
	/**
	 * 字节数组转换成16进制的字符串，带空格
	 * @param bytes
	 * @return
	 */
	public String ftBytesToHexStringWithSpaces(byte[] bytes)
	{
		return ftBytesToHexStringWithSpaces(bytes, bytes.length);
	}
	/**
	 * @函数名称 ftByteToHexStr
	 * @说明 字节变成有十六进制字符串
	 * @param src (输入)字节数组
	 * @param len (输入)字节数组的长度
	 * @return
	 */
	private String ftBytesToHexStringWithSpaces(byte[] src, int len) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		int n = len;
		if (len > src.length)
			n = src.length;

		for (int i = 0; i < n; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv + " ");
		}
		return stringBuilder.toString();
	}
	/**
	 * @函数名称 ftByteToHexStrNoSpace
	 * @说明 字节变成有十六进制无空格字符串
	 * @param src (输入)字节数组
	 * @param len (输入)字节数组的长度
	 * @return
	 */
	private String ftBytesToHexStrNoSpace(byte[] src, int len) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		int n = len;
		if (len > src.length)
			n = src.length;

		for (int i = 0; i < n; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	/**
	 * 字节数组转换成16进制字符串
	 * @param bytes
	 * @return
	 */
	public String ftBytesToHexString(byte[] bytes)
	{
		return ftBytesToHexStrNoSpace(bytes, bytes.length);
	}
	/** 产生一个随机的字符串*/
	public String generateRandomCharacter(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(62);
			buf.append(str.charAt(num));
		}
		
		return buf.toString();
	}
	public String generateRandomNumeric(int length)
	{
		long seed = new Date().getTime();
		Random random = new Random(seed);
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < length; i++) {
			buf.append(random.nextInt(10));
		}
		return buf.toString();
	}
	/**
	 * 清除填充数据
	 * @param str 具有填充数据的数据
	 * @param flag填充数据（1位）
	 * @return
	 */
	public String clearFlag(String str,String flag)
	{
		if(flag.length()!=1)
		{
			return null;
		}
		int len = str.length();
		while(str.substring(len-1, len).equals(flag))
		{
			len= len-1;
		}
		str = str.substring(0, len);
		return str;
		
	}
	/**
	 * 对数据补位
	 * @param str 需要补充的数据
	 * @param flag 补充标志
	 * @param len  长度
	 * @return
	 */
	public String addFlag(String str,String flag,int len){
		if(str.length()>len)
		{
			str = str.substring(0, len);
		}
		while(str.length()<len)
		{
			str = str+flag;
		}
		
		return str;
	}
	/**
	 * 根据TLV数据获取目标tag值
	 * @param resp 返回的TLV数据
	 * @param tag 用"_"分割各层tag数据，查找对应tag时注意层次 例如：6F_A5_BF0C_61_4F
	 * @return
	 */
	public String getTLV(String resp,String tag)
	{
		String result = new String();
		int number = 0;
		//将字符创全部大写处理
		resp = resp.toUpperCase();
		tag = tag.toUpperCase();
		//拆分各层tag值
		String[] _tag = tag.split("_");
		
		number = _tag.length; //tag层次数量
		
		//循环tag层找出目标Value
		for(int i=0;i<number;i++)
		{
			//在字符串内找出tag位置（index+1）
			int index = resp.indexOf(_tag[i]);
			//获取length数据
			int tag_len = _tag[i].length();
			index = index+tag_len;
			String length = resp.substring(index,index+2);
			index = index+2;
			//对L大于等于128的数据做判断处理,一般就用到两个字节表示长度
			if(length.equals("81"))
			{
				length = resp.substring(index,index+2);
				index = index+2;
			}
			else if(length.equals("82"))
			{
				length = resp.substring(index,index+4);
				index = index+4;
			}
			//将十六进制长度数据转化为十进制长度数据
			int val_len = ftHexToDec(length)*2;
			resp = resp.substring(index, index+val_len);
			
		}
		result = resp;
		
		return result;
	}
	public int findTag(String buffer,List<String> full_tag ){
		 //String full_tag = new String();
	        int result;
	        String structure_type = "2367ABEF";
	        
	       //将字符创全部大写处理
	        buffer = buffer.toUpperCase();
	        
	        String tag_first = buffer.substring(0, 1);
	        String tag_sec=buffer.substring(1, 2);
	        //判断是否有后续tag字节
	        //第一字节不能被2整除，第二字节为F，则符合TLV规定的“11111”实际标签值表示在后续字节中
	        if(tag_sec.equals("F")&&ftHexToDec(tag_first)%2==1){
	        	full_tag.add( buffer.substring(0, 4));	        
	        }
	        else{
	        	full_tag.add( buffer.substring(0, 2));
	        }
	        //判断tag是简单类型还是结构类型
	        int buf = structure_type.indexOf(tag_first);
	        if(buf>=0){
	        	result =  EASY_TYPE;
	        }
	        else{
	        	result =  STRUCTURE_TYPE;
	        }
			return result;
	}
	/**
	 * @函数名称 getTagLen
	 * @param tag (输入)
	 * @return tag的长度 1:两个字符，2:四个字符
	 */
	public int getTagLen(String tag) {
		String buf1 = null;
		String buf2 = null;
		int buf;
		buf1 = tag.substring(0, 1);
		buf2 = tag.substring(1, 2);
		buf = (int) ftHexToLong(buf1);
		if(buf%2!=0 && buf2.equals("F"))
			return 2;
		else
			return 1;
	}
	/**
	 * 根据tag获取tlv里对应的值和tag，最多截取15个
	 * @param tlv 输入tlv字符串
	 * @param tagstr 输入tag字符串(逗号分割)
	 * @param tag 输出tag tag是tagstr的子集
	 * @param value 输出tag对应的值
	 * @return
	 */
	public int ftTLVGetStrVal(String tlv, String tagstr, StringBuilder tag, StringBuilder value)
	{
		String szTags[] = new String[16];
		String szBuf = null;
		String szTag = null;
		String szTagBuf = null;
		String szLen1 = null;
		StringBuffer szVal = new StringBuffer(256);
		int nLayer = 0;
		int nPos;
		int nLen = tagstr.length();
		int i = 0;
		int nTagLen;
		int nValLen;
		boolean bFlag = false;
		while(true){
			nPos = tagstr.indexOf(",",i);
			if(nPos == -1){
				if(nLen > i)
				{
					szTags[nLayer] = tagstr.substring(i, nLen);
					nLayer++;
				}
				break;
			}else{
				szTags[nLayer] = tagstr.substring(i, nPos);
				i = nPos+1;
				nLayer++;
			}
			//最多处理15个Tag
			if(nLayer >= 15)
				break;
		}
		if(nLayer <= 0)
			return -1;
		//将TLV数据保存
		szBuf = tlv;
		for (i = 0; i < nLayer; i++) {
			nPos = 0;
			bFlag = true;
			//设置要获取数据的tag
			szTag = szTags[i];
			while(bFlag)
			{
				if(szBuf.isEmpty())
					break;
				szTagBuf = szBuf.substring(nPos, nPos+2);
				nTagLen = getTagLen(szTagBuf);
				if(nTagLen == 2)
				{
					szTagBuf = szBuf.substring(nPos, nPos+4);
					nPos += 4;
				}else
					nPos += 2;
				if(szTagBuf.equals(szTag))
				{
					// 拼接tag
					if(tag.length() == 0)
						tag.append(szTagBuf);
					else
						tag.append(",").append(szTagBuf);
					
					//取TAG数据长度高子节
					szLen1 = szBuf.substring(nPos, nPos+2);
					if(szLen1.equals("81")){
						nPos += 2;
						szLen1 = szBuf.substring(nPos, nPos+2);
					}
					//值长度
					nValLen = (int) (ftHexToLong(szLen1))*2;
					//值开始位置
					nPos += 2;
					//截取值数据
					if(szVal.length() == 0)
						szVal.append(szBuf.substring(nPos, nPos+nValLen));
					else
						szVal.append(",").append(szBuf.substring(nPos, nPos+nValLen));
					
					//把值保存到szBuf中用于下次查询
					szBuf = szBuf.substring(nPos+nValLen);
					bFlag = false;
				}
				else{
					//取TAG数据长度高子节
					szLen1 = szBuf.substring(nPos, nPos+2);
					if(szLen1.equals("81")){
						nPos += 2;
						szLen1 = szBuf.substring(nPos, nPos+2);
					}
					//值长度
					nValLen = (int) (ftHexToLong(szLen1))*2;
					//值开始位置
					nPos += 2;
					if(nValLen != szBuf.substring(nPos).length()){
						//截取值数据
						nPos += nValLen;
					}

					
					//tag寻找完成
					if(nPos > szBuf.length()){
						bFlag = false;
					}
				}
			}
		}
		value.setLength(0);
		value.append(szVal);
		return FTOK;
	}
	/**
	 * @函数名称 ftTLVGetStrVal
	 * @说明 根据tag获取tlv里对应的值，最多截取15个
	 * @param tlv (输入)tlv字符串
	 * @param tag (输入)tag标签(逗号分割)
	 * @param dest (输出)对应的Tag值
	 * @return 0 成功
	 */
	public int ftTLVGetStrVal(String tlv, String tag, StringBuilder dest)
	{
		String szTags[] = new String[16];
		String szBuf = null;
		String szTag = null;
		String szTagBuf = null;
		String szLen1 = null;
		StringBuffer szVal = new StringBuffer(256);
		int nLayer = 0;
		int nPos;
		int nLen = tag.length();
		int i = 0;
		int nTagLen;
		int nValLen;
		boolean bFlag = false;
		while(true){
			nPos = tag.indexOf(",",i);
			if(nPos == -1){
				if(nLen > i)
				{
					szTags[nLayer] = tag.substring(i, nLen);
					nLayer++;
				}
				break;
			}else{
				szTags[nLayer] = tag.substring(i, nPos);
				i = nPos+1;
				nLayer++;
			}
			//最多处理15个Tag
			if(nLayer >= 15)
				break;
		}
		if(nLayer <= 0)
			return -1;
		//将TLV数据保存
		szBuf = tlv;
		for (i = 0; i < nLayer; i++) {
			nPos = 0;
			bFlag = true;
			//设置要获取数据的tag
			szTag = szTags[i];
			while(bFlag)
			{
				szTagBuf = szBuf.substring(nPos, nPos+2);
				nTagLen = getTagLen(szTagBuf);
				if(nTagLen == 2)
				{
					szTagBuf = szBuf.substring(nPos, nPos+4);
					nPos += 4;
				}else
					nPos += 2;
				if(szTagBuf.equals(szTag))
				{
					//取TAG数据长度高子节
					szLen1 = szBuf.substring(nPos, nPos+2);
					if(szLen1.equals("81")){
						nPos += 2;
						szLen1 = szBuf.substring(nPos, nPos+2);
					}
					//值长度
					nValLen = (int) (ftHexToLong(szLen1))*2;
					//值开始位置
					nPos += 2;
					//截取值数据
					if(szVal.length() == 0)
						szVal.append(szBuf.substring(nPos, nPos+nValLen));
					else
						szVal.append(",").append(szBuf.substring(nPos, nPos+nValLen));
					
					//把值保存到szBuf中用于下次查询
					szBuf = szBuf.substring(nPos+nValLen);
					bFlag = false;
				}
				else{
					//取TAG数据长度高子节
					szLen1 = szBuf.substring(nPos, nPos+2);
					if(szLen1.equals("81")){
						nPos += 2;
						szLen1 = szBuf.substring(nPos, nPos+2);
					}
					//值长度
					nValLen = (int) (ftHexToLong(szLen1))*2;
					//值开始位置
					nPos += 2;
					if(nValLen != szBuf.substring(nPos).length()){
						//截取值数据
						nPos += nValLen;
					}

					
					//tag寻找完成
					if(nPos > szBuf.length()){
						bFlag = false;
					}
				}
			}
		}
		dest.setLength(0);
		dest.append(szVal);
		return FTOK;
	}
	int MAXTAGNUM = 40;
	/**
	 * 获取tag标签和值长度，最多分40个
	 * @param tagData
	 * @param tag
	 * @param vlen
	 * @return
	 */
	public int getTagAndValueLen(String tagData, StringBuilder[] tag, StringBuilder[] vlen) {
		String[] valueLenList = new String[MAXTAGNUM];
		String[] szTagList = new String[MAXTAGNUM];
		String szBuf = null;
		
		int tagNum = 0;
		int tagLen;
		int len = tagData.length();
		int i=0;
		while(i<len){
			tagLen = 0;
			szBuf = tagData.substring(i, i+2);
			tagLen = getTagLen(szBuf.toString());
			if(tagLen == 1){
				szTagList[tagNum] = szBuf;
				i += 2;
			}
			else if(tagLen == 2){
				szTagList[tagNum] = tagData.substring(i, i+4);
				i += 4;
			}
			else
				return 0;
			valueLenList[tagNum] = tagData.substring(i, i+2);
			i += 2;
			tagNum++;
		}
		for(int j = 0; j< tagNum; j++){
			tag[j] = new StringBuilder(szTagList[j]);
			vlen[j] = new StringBuilder(valueLenList[j]);
		}
		return tagNum;
	}
}
