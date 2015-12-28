package com.axis.common.chipher;
/**
 * SHA1安全哈希计算
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.axis.common.ftBaseFunc;

//import com.ft.ic.keyManage.javaCard.CapFile;



public class SHA1 {

	static ftBaseFunc ftPublic = new ftBaseFunc();
	
	  /**
	   * SHA1安全哈希计算
	   * @param inStr 所要加密的字符串数据(BCD数据)
	   * @return ACSII类型数据 长度20字节
	   */
	    public static String doSHA1(String inStr) {
	        MessageDigest md = null;
	        String outStr = null;
	        try {
	            md = MessageDigest.getInstance("SHA-1");           //选择SHA-1，也可以选择MD5
	            byte[] digest = md.digest(inStr.getBytes());       //返回的是byet[]，要转化为String存储比较方便
	            outStr = ftPublic.ftByteToString(digest);
	        }
	        catch (NoSuchAlgorithmException nsae) {
	            nsae.printStackTrace();
	        }
	        return outStr;
	    }
	    
	    /**
		   * SHA1安全哈希计算
		   * @param inByte 所要加密的字节数据(ACSII型数据)
		   * @return ACSII类型数据 长度20字节
		   */
		    public static String doSHA1(byte[] inByte) {
		        MessageDigest md = null;
		        String outStr = null;
		        try {
		            md = MessageDigest.getInstance("SHA-1");     //选择SHA-1，也可以选择MD5
		            byte[] digest = md.digest(inByte);           //返回的是byet[]，要转化为String存储比较方便
		            outStr = ftPublic.ftByteToString(digest);
		        }
		        catch (NoSuchAlgorithmException nsae) {
		            nsae.printStackTrace();
		        }
		        return outStr;
		    }
		    
		    /**
		     * 对cap文件做哈希计算
		     * @param path cap文件路径
		     * @return
		     */
		  public static String SHA1ForCap(String path)
		    {
		    	String result = new String();
//		    	CapFile cf = new CapFile();
		    	
//		    	String buf = cf.getLoadData(path);
//		    	buf = buf.substring(8);
//		    	byte[] capBuf = DoData.stringToByte(buf);
		    	//哈希计算  
//		   	     result = doSHA1(capBuf);			    	
		        
		        return result;
		    }

}
