/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 处理加密的工具，包括RSA，DES，MD5
 * 
 * @author BruceSu
 * 
 */
public class EncryptUtil {

	/**
	 * 得到公钥
	 * 
	 * @param key
	 *            密钥字符串（hex字符串）
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes = hexStringToBytes(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 得到私钥
	 * 
	 * @param key
	 *            密钥字符串（hex字符串）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes = hexStringToBytes(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * 得到密钥字符串（hex字符串）
	 * 
	 * @return
	 */
	public static String getKeyString(Key key) throws Exception {
		byte[] b = key.getEncoded();
		return bytesToHexString(b);
	}

	/**
	 * 将byte[]转成hex字符串
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToBytes(String s) {
		byte[] keyBytes;
		keyBytes = new byte[s.length() / 2];
		for (int i = 0; i < keyBytes.length; i++) {
			keyBytes[i] = (byte) Integer.parseInt(
					s.substring(i * 2, i * 2 + 2), 16);
		}
		return keyBytes;
	}

	/**
	 * 将hex字符串转成byte[]
	 * 
	 * @param b
	 * @return
	 */
	public static String bytesToHexString(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				sb.append(0);
			}
			sb.append(hv);
		}
		return sb.toString();

	}

	/**
	 * RSA加密
	 * 
	 * @param deBytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptRSA(byte[] deBytes, String rsaPublicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(rsaPublicKey));
		byte[] enBytes = cipher.doFinal(deBytes);
		return enBytes;
	}

	/**
	 * RSA解密
	 * 
	 * @param enBytes
	 * @param rsaPublicKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptRSA(byte[] enBytes, String rsaPublicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, getPublicKey(rsaPublicKey));
		byte[] deBytes = cipher.doFinal(enBytes);
		return deBytes;
	}

	/**
	 * DES加密
	 * 
	 * @param deBytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptDES(byte[] deBytes, SecretKey key)
			throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] enBytes = cipher.doFinal(deBytes);
		return enBytes;
	}

	/**
	 * DES解密
	 * 
	 * @param enBytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptDES(byte[] enBytes, SecretKey key)
			throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] deBytes = cipher.doFinal(enBytes);
		return deBytes;
	}

	/**
	 * 获取单个文件的MD5值！
	 * 
	 * @param file
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getMD5(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest digest = null;
		digest = MessageDigest.getInstance("MD5");
		digest.update(data);
		// BigInteger bigInt = new BigInteger(1, digest.digest());
		// System.out.println(bigInt.toString(16));
		return digest.digest();
	}

	/**
	 * 获得des随机密钥
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static SecretKey getDesKeyBytes() throws NoSuchAlgorithmException {
		SecureRandom sr = new SecureRandom();
		KeyGenerator kg = KeyGenerator.getInstance("DES");
		kg.init(sr);
		SecretKey key = kg.generateKey();
		return key;
		// return key.getEncoded();
	}

	/**
	 * 根据数据获得des密钥
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static SecretKey getDesKey(byte[] keyBytes)
			throws NoSuchAlgorithmException {
		SecureRandom sr = new SecureRandom(keyBytes);
		KeyGenerator kg = KeyGenerator.getInstance("DES");
		kg.init(sr);
		SecretKey key = kg.generateKey();
		return key;
	}

	public static byte[] rsaSignet(byte[] data, String rsaPublicKey)
			throws Exception {
		// 用私钥对信息生成数字签名
		java.security.Signature signet = java.security.Signature
				.getInstance("MD5withRSA");
		signet.initVerify(getPublicKey(rsaPublicKey));
		String privateKeyString = "";// getKeyString(privateKey);
		PrivateKey privateKey = getPrivateKey(privateKeyString);
		signet.initSign(privateKey);
		signet.update(data);
		byte[] signed = signet.sign(); // 对信息的数字签名
		return signed;
	}

	public static void main(String[] args) throws Exception {
		// {"version":"2.1.6","channel":"10000","mac":"none","xmdmm":"xsanguo_not_supported","xmamg":"9C-C9-76-D9-5D-16-2D-54-5C-2E-55-3E-E2-39-E0-8C"}
		// e19e937fe3f4aa1bd0ea7f39194b103f
		// e19e937fe3f4aa1bd0ea7f39194b103f
		String testStr = "cj111115100001bc2541135f5c96efd5341907ab51f3d";
		byte[] md5 = getMD5(testStr.getBytes());
		System.out.println("MD5: " + bytesToHexString(md5));
		// byte[] enData = encryptRSA(md5);
		// System.out.println("RSA加密: " + bytesToHexString(enData));
		// System.out.println("RSA签名" + bytesToHexString(rsaSignet(md5)));
		// 没有对应的私钥，无法运行
		// System.out.println("RSA解密: " + bytesToHexString(decryptRSA(enData)));

		byte[] desKey = getDesKeyBytes().getEncoded();
		System.out.println("DES密钥: " + bytesToHexString(desKey));
		byte[] enData2 = encryptDES("des测试".getBytes(), getDesKey(desKey));
		System.out.println("DES加密: " + bytesToHexString(enData2));
		System.out.println("DES解密: "
				+ new String(decryptDES(enData2, getDesKey(desKey))));

		// KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		// 密钥位数
		// keyPairGen.initialize(1024);
		// 密钥对
		// KeyPair keyPair = keyPairGen.generateKeyPair();

		// 公钥
		// PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// PublicKey publicKey = new RSAPublicKeyImpl(rsa_public_bytes);
		// 私钥
		// PrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		String publicKeyString = "";// getKeyString(publicKey);
		// System.out.println("公钥:\n" + publicKeyString);

		String privateKeyString = "";// getKeyString(privateKey);
		// System.out.println("私钥:\n" + privateKeyString);

		// 通过密钥字符串得到密钥
		PublicKey publicKey = getPublicKey(publicKeyString);
		PrivateKey privateKey = getPrivateKey(privateKeyString);

		// 加解密类
		Cipher cipher = Cipher.getInstance("RSA");// Cipher.getInstance("RSA/ECB/PKCS1Padding");

		// 明文
		byte[] plainText = "测试字符串明文".getBytes();

		// 公钥加密
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 私钥加密
		// cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[] enBytes = cipher.doFinal(plainText);
		// System.out.println("加密得到字符串为：" + bytesToHexString(enBytes));

		// 通过密钥字符串得到密钥
		publicKey = getPublicKey(publicKeyString);
		privateKey = getPrivateKey(privateKeyString);

		// 私钥解密
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 公钥解密
		// cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] deBytes = cipher.doFinal(enBytes);

		publicKeyString = getKeyString(publicKey);
		// System.out.println("公钥:\n" + publicKeyString);

		privateKeyString = getKeyString(privateKey);
		// System.out.println("私钥:\n" + privateKeyString);

		String s = new String(deBytes);
		// System.out.println("解密字符串为：" + s);

	}
}
