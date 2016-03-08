package com.axis.common.pki;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.binary.Base64;

import com.axis.common.ftPublicFunc;
import com.axis.common.exceptions.UtilException;

public class X509Helper {

	private X509Certificate x509;

	/**
	 * 
	 * @param certificatefFile
	 *            证书文件路径
	 */
	public X509Helper(String certificatefFile) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			FileInputStream in = new FileInputStream(certificatefFile);
			java.security.cert.Certificate cert = cf.generateCertificate(in);
			in.close();
			x509 = (X509Certificate) cert;
		} catch (CertificateException e) {
			throw new UtilException("Error occured when get X509 Instance.", e);
		} catch (FileNotFoundException e) {
			throw new UtilException("Certificate not found.", e);
		} catch (IOException e) {
			throw new UtilException("IOException.", e);
		}
	}
	
	public X509Helper(String certificate, boolean isBase64Format) {
		try {
			byte[] byteCert = null;
			if(isBase64Format)
			{
				byteCert = Base64.decodeBase64(certificate);
			}
			else
			{
				byteCert = new ftPublicFunc().ftHexStringToBytes(certificate);
			}
			ByteArrayInputStream certin = new ByteArrayInputStream(byteCert);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			java.security.cert.Certificate cert = cf.generateCertificate(certin);
			x509 = (X509Certificate) cert;
		} catch (CertificateException e) {
			throw new UtilException("Error occured when get X509 Instance.", e);
		}
	}

	/**
	 * 返回一个509证书
	 * 
	 * @return X509Certificate
	 */
	public X509Certificate getX509Certificate() {
		return x509;
	}

	/**
	 * 获取证书16进制字符串
	 */
	public String toString() {
		StringBuilder stringbuf = new StringBuilder();
		byte[] certb;
		try {
			certb = x509.getEncoded();
			for (byte b : certb) {
				stringbuf.append(String.format("%02x", b));
			}
		} catch (CertificateEncodingException e) {
			throw new UtilException(e);
		}
		return stringbuf.toString();
	}

	/**
	 * 
	 * @return 证书版本号
	 */
	public int getVersion() {
		return x509.getVersion();
	}

	/**
	 * 
	 * @return 证书序列号 16进制
	 */
	public String getSerialNumber() {
		return x509.getSerialNumber().toString(16);
	}

	/**
	 * 
	 * @return 返回SubjectDN
	 */
	public String getSubjectDN() {
		return x509.getSubjectDN().toString();
	}

	/**
	 * 
	 * @return 返回IssuerDN
	 */
	public String getIssuerDN() {
		return x509.getIssuerDN().toString();
	}

	/**
	 * 
	 * @return 返回有效日期起始日
	 */
	public Date getNotBefore() {
		return x509.getNotBefore();
	}

	/**
	 * 
	 * @return 返回有效日期结束日
	 */
	public Date getNotAfter() {
		return x509.getNotAfter();
	}

	/**
	 * 
	 * @return 证书签名算法
	 */
	public String getSinAlgName() {
		return x509.getSigAlgName();
	}

	/**
	 * 
	 * @return 证书签名
	 */
	public String getSignature() {
		byte[] b = x509.getSignature();
		return new ftPublicFunc().ftBytesToHexString(b);
	}

	/**
	 * 
	 * @return 证书的公钥
	 */
	public String getPublicKey() {
		StringBuilder stringbuild = new StringBuilder();
		byte[] pkb = x509.getPublicKey().getEncoded();
		for (byte b : pkb) {
			stringbuild.append(String.format("%02x", b));
		}
		return stringbuild.toString();
	}
}
