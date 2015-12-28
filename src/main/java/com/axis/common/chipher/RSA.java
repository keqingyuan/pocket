package com.axis.common.chipher;
/**
 * RSA加密计算
 * @author gwg
 *
 */

import java.math.BigInteger; 
import java.security.KeyFactory;  
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.PrivateKey;  
import java.security.PublicKey;  
import java.security.Signature;
  
import java.security.interfaces.RSAPrivateKey;  
import java.security.interfaces.RSAPublicKey;  
 
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

  
import java.util.HashMap;  
import java.util.Map;  
  
import javax.crypto.Cipher;  

//import com.ft.ic.keyManage.javaCard.ISD;



public class RSA {
	  public static final String KEY_ALGORITHM = "RSA";  
	  public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";  
	  
	  private static final String PUBLIC_KEY = "RSAPublicKey";  
	  private static final String PRIVATE_KEY = "RSAPrivateKey"; 

	  
	  
	  /** ****************************************************************************
	     * 用私钥对信息生成数字签名 
	     *  
	     * @param data 
	     *            加密数据 
	     * @param modulus 
	     * 			     私钥的N
	     * @param privateExponet 
	     * 			      私钥的D 
	     *  
	     * @return 
	     * @throws Exception 
	     ******************************************************************************/  
	    public static byte[] sign(byte[] data, String modulus,String privateExponet) throws Exception {  	       	      

            //获得私钥
	        PrivateKey privateKey = RSA.getPrivateKey(modulus, privateExponet); 
	  
	        // 用私钥对信息生成数字签名  
	        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
	        signature.initSign(privateKey);  
	        signature.update(data); 
	        byte[] sign =signature.sign();
	        return sign;  
	    }  
	    
	 
	    /***************************************************************************** 
	     * 解密<br> 
	     * 用私钥解密 
	     *  
	     * @param data 
	     *      				加密数据
	     * @param modulus 
	     * 						私钥的N
	     * @param privateExponet 
	     * 						私钥的D 
	     * @return 
	     * @throws Exception 
	     ********************************************************************************/  
	    public static byte[] decryptByPrivateKey(byte[] data, String modulus,String privateExponet)  
	            throws Exception {  
	
		        PrivateKey privateKey = RSA.getPrivateKey(modulus, privateExponet);  
		        
		          //加解密类  	         
		        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
		         
		         //加密  
		         
		        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
		    
		        byte[] deBytes = cipher.doFinal(data); 
		  
		        return deBytes;  
	    }  
	  
	    /************************************************************************************ 
	     * 解密<br> 
	     * 用公钥解密 
	     * @param data
	     * 						加密数据
	     * @param modulus 
	     * 						公钥的N
	     * @param publicExponet 
	     * 						公钥的E 
	     * @return 
	     * @throws Exception 
	     ***********************************************************************************/  
	    public static byte[] decryptByPublicKey(byte[] data, String modulus,String publicExponet)  
	            throws Exception {  

	            PublicKey publicKey = RSA.getPublicKey(modulus, publicExponet);  
		        
		        //加解密类  	         
		        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
		         
		        //加密  		         
		        cipher.init(Cipher.DECRYPT_MODE, publicKey);  
		    
		        byte[] deBytes = cipher.doFinal(data); 
		  
		        return deBytes;  
	    }  
	  
	    /** *******************************************************************************
	     * 加密<br> 
	     * 用公钥加密 
	     *  
	     * @param data				
	     * 						明文加密数据
	     * @param modulus 
	     * 						公钥的N
	     * @param publicExponet 
	     * 						公钥的E 
	     * @return 
	     * @throws Exception 
	     *************************************************************************************/  
	    public static byte[] encryptByPublicKey(byte[] data, String modulus,String publicExponet)  
	            throws Exception {  

	            PublicKey publicKey = RSA.getPublicKey(modulus, publicExponet);  
		        
		        //加解密类  	         
		        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
		         
		        //加密  		         
		        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
		    
		        byte[] enBytes = cipher.doFinal(data); 
		  
		        return enBytes;    
	    }  
	  
	    /** ***************************************************************************************
	     * 加密<br> 
	     * 用私钥加密 
	     *  
	     * @param data 
	     * 						明文加密数据
	     * @param modulus 
	     * 						私钥的N
	     * @param privateExponet 
	     * 						私钥的D
	     * @return 
	     * @throws Exception 
	     *****************************************************************************************/  
	    public static byte[] encryptByPrivateKey(byte[] data, String modulus,String privateExponet)  
	            throws Exception {  
	    
	        PrivateKey privateKey = RSA.getPrivateKey(modulus, privateExponet);  
	        
	          //加解密类  	         
	         Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
	         
	         //加密  
	         
	        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
	    
	        byte[] enBytes = cipher.doFinal(data); 
	  
	        return enBytes;  
	    }  
  
	    /** ***********************************************************************************
	     * 初始化密钥 
	     *  
	     * @return 
	     * @throws Exception 
	     *****************************************************************************************/  
	    public static Map<String, Object> initKey() throws Exception {  
	        KeyPairGenerator keyPairGen = KeyPairGenerator  
	                .getInstance(KEY_ALGORITHM);  
	        keyPairGen.initialize(1024);  
	  
	        KeyPair keyPair = keyPairGen.generateKeyPair();  
	  
	        // 公钥  
	        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
	  
	        // 私钥  
	        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
	  
	        Map<String, Object> keyMap = new HashMap<String, Object>(2);  
	  
	        keyMap.put(PUBLIC_KEY, publicKey);  
	        keyMap.put(PRIVATE_KEY, privateKey);  
	        
	        return keyMap;  
	    }  
	    
	    
	    
	    /************************************************************************************************ 
	     * 取得公钥 
	     *  
	     * @param modulus 
	     * 						公钥N(16进制ASCII型数据串)
	     * @param publicExponent
	     * 						公钥E(16进制ASCII型数据串)
	     * @return 
	     * @throws Exception 
	     ****************************************************************************************************/ 
	    public static PublicKey getPublicKey(String modulus,String publicExponent) throws Exception {  
	    	  
            BigInteger n = new BigInteger(modulus,16);  
           
            BigInteger e = new BigInteger(publicExponent,16);  
  
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n,e);  
  
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
  
            PublicKey publicKey = keyFactory.generatePublic(keySpec);  
  
            return publicKey;  
  
      }  
   
	    /** ********************************************************************************
	     * 取得私钥 
	     *  
	     * @param modulus 
	     * 						私钥N(16进制ASCII型数据串)
	     * @param privateExponent
	     * 						私钥D(16进制ASCII型数据串) 
	     * @return 
	     * @throws Exception 
	     **************************************************************************************/  
      public static PrivateKey getPrivateKey(String modulus,String privateExponent) throws Exception {  
  
            BigInteger n = new BigInteger(modulus,16);  
  
            BigInteger d = new BigInteger(privateExponent,16);  
  
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(n,d);  
  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
  
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);  
  
            return privateKey;  
  
      }  
  
	    
	    public static void main(String args[]) throws Exception     
	    {   

//            byte[] data = DoData.stringToByte("b952d7e9136efd486b18934b19a13928977edd652bc9fe3ffcbeada2ef45e151e4db2ea0f2f6c18a8b3e6a50cc34c6aea3cffc09060a243fdd7d888b0ee6c29dd1438679ddb4b29285cff1f7226d59c4dfad295bec991a1608565556cd3dbd57a44d56536dc0f2d155a6b7432334cb88dc1ef51f7290bc4e26130b5b8dd5b803");
//            byte[] buf  = RSA.sign(data,ISD.MODULUS, ISD.PRIVATE_EXPONENT);
//            System.out.println(DoData.byteToString(buf)); 
////            String sha1 = SHA1.doSHA1(data);
////            System.out.println(sha1);
//            byte[] buf2 = RSA.encryptByPublicKey(buf, ISD.MODULUS, ISD.PUBLIC_EXPONENT);
//          System.out.println(DoData.byteToString(buf2));
//           // String modulus="37971092741399997485074643625255643030228196354320212968848278800567662814571677152937658874918653823588292636986486009058530272075019254932808755624344076521116444909333244014505894176681179961842015316253643010788960441145595390593481114049131399742240755124691700869131736481056463637238520553664521199521";
//            //BigInteger n = new BigInteger(modulus,10); 
//            //System.out.println(n.toString(16));
			
	    }   


}
