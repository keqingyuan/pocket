package com.axis.common.pki;

import java.security.cert.CertificateEncodingException;

import com.axis.common.ftBaseFunc;

import junit.framework.TestCase;

public class X509Test extends TestCase {

	
	private X509Helper cers = new X509Helper("certs//ICCBD CA.cer");
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetVersion() {
		try {
			System.out.println("* "+ new ftBaseFunc().ftBytesToHexString(cers.getX509Certificate().getTBSCertificate()));
		} catch (CertificateEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Version:"+cers.getVersion());
	}

	public void testGetSerialNumber() {
		System.out.println("SerialNumber:"+cers.getSerialNumber());
	}

	public void testGetSubjectDN() {
		System.out.println("SubjectDN:"+cers.getSubjectDN());
	}

	public void testGetIssuerDN() {
		System.out.println("IssuerDN:"+cers.getIssuerDN());
	}

	public void testGetNotBefore() {
		System.out.println("Data From:"+cers.getNotBefore());
	}

	public void testGetNotAfter() {
		System.out.println("Data To:"+cers.getNotAfter());
	}

	public void testGetSinAlgName() {
		System.out.println("SignAlg:"+cers.getSinAlgName());
	}

	public void testGetSignature() {
		System.out.println("Signature:"+cers.getSignature());
	}
	public void testGetPublicKey() {
		System.out.println("public key:"+cers.getPublicKey());
	}
}
