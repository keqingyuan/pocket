package com.axis.common.pki;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import com.axis.common.ftPublicFunc;

import junit.framework.TestCase;

public class CertificateHelperTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testEncryptByPrivateKey() {
		String kp = "keystore//eload.server.p12";
		String pwd = "eload.server";
		String alias;
		try {
			alias = CertificateHelper.getAlias(kp,pwd);
			System.out.println(alias);
			byte[] b = "111111111111111111111111111111111111111111111111".getBytes();
			String data = new ftPublicFunc().ftBytesToHexString(b);
			System.out.println(data);
			//b = CertificateHelper.encryptByPublicKey(b, "certs//eload.server.cer");
			b = CertificateHelper.encryptByPrivateKey(b, kp , pwd, alias, pwd);
			data = new ftPublicFunc().ftBytesToHexString(b);
			System.out.println(data);
			String cp = "certs//eload.server.cer";
			b = CertificateHelper.decryptByPublicKey(b, cp);
			data = new ftPublicFunc().ftBytesToHexString(b);
			System.out.println(data);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void testDecryptByPrivateKey() {
//		fail("Not yet implemented");
//	}

//	public void testEncryptByPublicKey() {
//		fail("Not yet implemented");
//	}
//
//	public void testDecryptByPublicKey() {
//		fail("Not yet implemented");
//	}
//
//	public void testVerifyCertificateString() {
//		fail("Not yet implemented");
//	}
//
//	public void testVerifyCertificateDateString() {
//		fail("Not yet implemented");
//	}
//
//	public void testSign() {
//		fail("Not yet implemented");
//	}
//
	public void testVerify() {
		try {
			System.out.println("CA 公钥验证服务器证书");
			String caPath = "certs//ICCBD CA.cer";
			String serverPath = "certs//eload.server.cer";
			X509Helper xh = new X509Helper(serverPath);
			X509Certificate servercert = xh.getX509Certificate();
			byte sign[] = servercert.getSignature();
			byte data[] = servercert.getTBSCertificate();
			System.out.println("CA: "+new ftPublicFunc().ftBytesToHexString(new X509Helper("certs//qingyuan.cer").getX509Certificate().getEncoded()));
			System.out.println("server certs: "+new ftPublicFunc().ftBytesToHexString(servercert.getTBSCertificate()));
			System.out.println("sign: "+ new ftPublicFunc().ftBytesToHexString(sign));
			System.out.println("server PK: "+ new ftPublicFunc().ftBytesToHexString(servercert.getPublicKey().getEncoded()));
			
			if(CertificateHelper.verify(data, sign, caPath))
				System.out.println("success");
			else
				System.out.println("fail");
				
		} catch (CertificateEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

//	public void testVerifyCertificateDateStringStringString() {
//		fail("Not yet implemented");
//	}
//
//	public void testVerifyCertificateStringStringString() {
//		fail("Not yet implemented");
//	}

}
