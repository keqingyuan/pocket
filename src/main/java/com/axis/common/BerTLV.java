package com.axis.common;

import java.util.HashMap;

public class BerTLV {

	public static HashMap<String, String> split(String tlv){
		if(tlv.isEmpty())
			return null;
		HashMap<String, String> map = new HashMap<String, String>();
		//将字符创全部大写处理
		tlv = tlv.toUpperCase();
		int size = tlv.length();
		int pos = 0;
		while(pos < size){
			String tag = tlv.substring(pos, pos+2);
			int tlb = TagLen(tag);
			if(tlb == 2)
			{
				tag = tlv.substring(pos, pos+4);
				pos += 4;
			}else
				pos += 2;
			
			String lb = tlv.substring(pos, pos+2);
			if(lb.equals("81")){
				lb = tlv.substring(pos, pos+4);
				pos += 4;
			}else
				pos += 2;
			int len = (int) Long.parseLong(lb, 16)*2;
			
			String v = tlv.substring(pos, pos+len);
			pos += len; 
			map.put(tag, v);
		}
		return map;
		
	}
	/**
	 * 根据TLV数据获取目标tag值
	 * @param tlv 返回的TLV数据
	 * @param tag 用"_"分割各层tag数据，查找对应tag时注意层次和大写<br/> 例如：6F_A5_BF0C_61_4F
	 * @return
	 */
	public static String getV(String tlv, final String tag){
		String result = new String();
		int number = 0;
		//将字符创全部大写处理
		tlv = tlv.toUpperCase();
		//拆分各层tag值
		String[] _tag = tag.split("_");
		
		number = _tag.length; //tag层次数量
		
		//循环tag层找出目标Value
		for(int i=0;i<number;i++)
		{
			//在字符串内找出tag位置（index+1）
			int index = tlv.indexOf(_tag[i]);
			//获取length数据
			int tag_len = _tag[i].length();
			index = index+tag_len;
			String length = tlv.substring(index,index+2);
			index = index+2;
			//对L大于等于128的数据做判断处理,一般就用到两个字节表示长度
			if(length.equals("81"))
			{
				length = tlv.substring(index,index+2);
				index = index+2;
			}
			else if(length.equals("82"))
			{
				length = tlv.substring(index,index+4);
				index = index+4;
			}
			//将十六进制长度数据转化为十进制长度数据
			int val_len = Numberutil.Hex2Dec(length)*2;
			tlv = tlv.substring(index, index+val_len);
			
		}
		result = tlv;
		
		return result;
	}
	public static String getT(String tlv){
		return null;
	}
	public static String getL(String tlv){
		return null;
	}
	/**
	 * @函数名称 getTagLen
	 * @param tag (输入)
	 * @return tag的长度 1:两个字符，2:四个字符
	 */
	public static int TagLen(String tag) {
		String buf1 = null;
		String buf2 = null;
		int buf;
		buf1 = tag.substring(0, 1);
		buf2 = tag.substring(1, 2);
		buf = (int) Long.parseLong(buf1, 16);
		if(buf%2!=0 && buf2.equals("F"))
			return 2;
		else
			return 1;
	}
}
