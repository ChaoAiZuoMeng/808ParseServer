package com.chaokong.tool;

public class Tools {

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * 把长度<4的字节数组转化为int
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static int bytesToInteger(byte[] value, int length) {
		int i = 0;
		if (1 == length) {
			i = value[0] & 0xFF;
		} else if (2 == length) {
			int temp0 = value[0] & 0xFF;
			int temp1 = value[1] & 0xFF;
			i = (temp0 << 8) + temp1;
		}
		return i;
	}

	public static String bytes2hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		String tmp = null;
		for (byte b : bytes) {

			tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() == 1) {
				tmp = "0" + tmp;
			}
			sb.append(tmp);
		}
		return sb.toString();
	}

	/**
	 * 把长度为5的字节数组转化为long
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static long bytesToLong(byte[] value) {
		long i = 0;
		if (5 == value.length) {
			long temp0 = value[0] & 0xFF;
			long temp1 = value[1] & 0xFF;
			long temp2 = value[2] & 0xFF;
			long temp3 = value[3] & 0xFF;
			long temp4 = value[4] & 0xFF;
			i = ((long) temp0 << 32) + (temp1 << 24) + (temp2 << 16) + (temp3 << 8) + temp4;
		} else {

		}
		return i;
	}

	public static byte[] HexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;

		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);

			b[i] = (byte) (parse(c0) << 4 | parse(c1));
		}
		return b;
	}
	
	private static int parse(char c) {
	     if (c >= 'a') {
	       return c - 97 + 10 & 0xF;
	     }

	     if (c >= 'A') {
	       return c - 65 + 10 & 0xF;
	     }

	     return c - 48 & 0xF;
	   }
	
	
	
	
	
	
	
	
	

	public static void main(String[] args) {
//		byte[] bb = hexStringToByteArray("3636363838");
//		long aa = bytesToLong(bb, 5);
//		
//		String sss = PropertiesUtil.getValueByKey("manufacturerId.properties", "cheHuLu");
//		if(aa == Long.valueOf(sss))
//		{
//			System.out.println("yeah");
//		}
//		System.out.println(aa);

	}
}
