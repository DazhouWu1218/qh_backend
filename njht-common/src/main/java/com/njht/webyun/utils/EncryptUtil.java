/**
 * EncryptUtil.java
 * <p>(一句话描述功能):
 * <p>@author: guoy
 * <p>@date: 2019年4月4日
 */
package com.njht.webyun.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author guoy
 *         <p>
 *         描述： aes加密、解密工具
 *         <p>
 *         2019年4月4日
 *         <p>
 */
public class EncryptUtil {

	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

	public static String base64Encode(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	public static byte[] base64Decode(String base64Code) {
		return Base64.decodeBase64(base64Code);
	}

	public static byte[] aesEncrytToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

		return cipher.doFinal(content.getBytes("utf-8"));
	}

	public static String aesEncryptToString(String content, String encryptKey) throws Exception {
		return base64Encode(aesEncrytToBytes(content, encryptKey));
	}

	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);

		return new String(decryptBytes, "utf-8");
	}

	/**
	 * (描述):
	 * <p>
	 * (说明): 使用最多
	 * <p>
	 * @param encryptCode
	 * <p>
	 * @param decryptKey
	 * <p>
	 * @return
	 * <p>
	 * @throws Exception
	 * <p>
	 * date: 2019年4月4日
	 * <p>
	 * createdBy: guoy
	 */
	public static String aesDecryptByString(String encryptCode, String decryptKey) throws Exception {
		return aesDecryptByBytes(base64Decode(encryptCode), decryptKey);
	}

	/**
	 *    (描述):     
	 * <p>(说明):     十六进制转换成二进制                            
	 * <p>     	     @param hexStr
	 * <p>     	     @return
	 * <p>date:      2019年5月31日
	 * <p>updateBy: guoy
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		hexStr = hexStr.toUpperCase();
		int length = hexStr.length() / 2;
		char[] hexChar = hexStr.toCharArray();
		byte[] bs = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			bs[i] = (byte) (char2Byte(hexChar[pos]) << 4 | char2Byte(hexChar[pos + 1]));
		}
		return bs;
	}

	/**
	 *    (描述):     
	 * <p>(说明):     二进制转换成十六进制                            
	 * <p>     	     @param bytes
	 * <p>     	     @return
	 * <p>date:      2019年5月31日
	 * <p>updateBy: guoy
	 */
	public static String parseByte2HexStr(byte[] bytes) {
		if (bytes.length < 1) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			int v = bytes[i] & 0xff;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				sb.append("0");
			}
			sb.append(hv);
		}
		return sb.toString();
	}

	public static byte char2Byte(char ch) {
		return (byte) "0123456789ABCDEF".indexOf(ch);
	}

}
