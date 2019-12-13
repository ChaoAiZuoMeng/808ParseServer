package com.chaokong.tool;

import java.io.UnsupportedEncodingException;

public class Transfer {

	private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * 编码为utf-8
	 * 获取指定长的的字符串对应的字节码，如果长度不够，末位自动补0
	 *
	 * @param
	 * @return
	 */
	public static byte[] getBytes(String s, int length) {
		int fixLength = length - s.getBytes().length;
		if (s.getBytes().length < length) {
			byte[] S_bytes = new byte[length];
			System.arraycopy(s.getBytes(), 0, S_bytes, 0, s.getBytes().length);
			for (int x = length - fixLength; x < length; x++) {
				S_bytes[x] = 0x00;
			}
			return S_bytes;
		}
		return s.getBytes();
	}


	public static String bytesToHex(byte[] bytes) {
		char[] buf = new char[bytes.length * 2];
		int index = 0;
		for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
			buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
			buf[index++] = HEX_CHAR[b & 0xf];
		}

		return new String(buf);
	}

	/**
	 * 字符串转换为16进制字符串
	 *
	 * @return
	 */
	public static String stringToHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	/**
	 * 字符串转换成十六进制字符串
	 *
	 * @param String str 待转换的ASCII字符串
	 * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
	 */
	public static String str2HexStr(String str,String charset) throws UnsupportedEncodingException {

		char[] chars = "0123456789abcdef".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes(charset);
		int bit;

		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString().trim();
	}
	/**
	 * 将16进制字符串转换为byte[]
	 *
	 * @param str
	 * @return
	 */
	public static byte[] hexStrToBytes(String str) {
		if (str == null || str.trim().equals("")) {
			return new byte[0];
		}

		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}
		return bytes;
	}

	public static String byteToStr(byte[] byteArray) {
		if (byteArray == null) {
			return null;
		}
		String str = new String(byteArray);
		return str;
	}


	/**
	 * 十六进制转换字符串
	 *
	 * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串
	 */
	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);

	}

}