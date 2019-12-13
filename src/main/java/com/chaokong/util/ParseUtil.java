package com.chaokong.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ParseUtil
{
	/**
	 * int转换为字节数组byte[]
	 * 
	 * @param i
	 *            转换对象
	 * @param length
	 *            结果为length个字节
	 * @return
	 */
	public static byte[] parseInt2ByteArray(int i, int length)
	{
		byte[] temp = new byte[length];
		if (1 == length)
		{
			temp[0] = (byte) i;
		} else if (2 == length)
		{
			temp[0] = (byte) ((i >>> 8) & 0xff);
			temp[1] = (byte) (i & 0xff);
		}
		return temp;
	}

	/**
	 * 把长度为2的字节数组转化为int
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static int bytesToInteger(byte[] value, int length)
	{
		int i = 0;
		if (1 == length)
		{
			i = value[0];
		} else if (2 == length)
		{
			int temp0 = value[0] & 0xFF;
			int temp1 = value[1] & 0xFF;
			i = (temp0 << 8) + temp1;
		}
		return i;
	}

	/**
	 * 字符串转为二进制
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] stringToBinary(String str, int length) throws UnsupportedEncodingException
	{
		byte[] result = new byte[length];
		byte[] bs = str.getBytes("gbk");
		System.arraycopy(bs, 0, result, 0, bs.length);
		return result;
	}

	/**
	 * 二进制转为字符串
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String binaryToString(byte[] bs) throws UnsupportedEncodingException
	{
		String str = new String(bs, "gbk");
		return str;
	}

	/**
	 * 将String数组转换为byte数组(String-->int-->byte)
	 * 
	 * @param list
	 * @param length
	 * @return
	 */
	public static byte[] StringArr2ByteArr(List<String> list)
	{
		int destPos = 0;
		int length = list.size() * 2;
		byte[] resultByteArr = new byte[length];
		for (String valueStr : list)
		{
			int valueInt = Integer.valueOf(valueStr);
			byte[] valueHex = ParseUtil.parseInt2ByteArray(valueInt, 2);
			System.arraycopy(valueHex, 0, resultByteArr, destPos, 2);
			destPos += 2;
		}
		return resultByteArr;
	}

	/**
	 * byte[] --> int[] 将byte数组转为int数组(2个字节为1个数字)
	 * 
	 * @param bs
	 * @return
	 */
	public static int[] byteArrayToIntArray(byte[] bs)
	{
		int[] is = new int[bs.length / 2];
		for (int i = 0; i < bs.length - 1; i += 2)
		{
			byte[] temp = new byte[2];
			temp[0] = bs[i];
			temp[1] = bs[i + 1];
			int result = ParseUtil.bytesToInteger(temp, 2);
			is[i / 2] = result;
		}
		return is;
	}

	/**
	 * 将long转为byte数组[4]
	 * 
	 * @param l
	 * @return
	 */
	public static byte[] longToByteArray(long l)
	{
		byte[] temp = new byte[4];
		temp[0] = (byte) ((l & 0xFF000000) >>> 24);
		temp[1] = (byte) ((l & 0xFF0000) >>> 16);
		temp[2] = (byte) ((l & 0xFF00) >>> 8);
		temp[3] = (byte) ((l & 0xFF));
		return temp;
	}

	/**
	 * byte数组转为long(默认4个字节)
	 * 
	 * @param bs
	 * @return
	 */
	public static long byteArrayToLong(byte[] bs)
	{
		long long0 = bs[0] & 0xFF;
		long long1 = bs[1] & 0xFF;
		long long2 = bs[2] & 0xFF;
		long long3 = bs[3] & 0xFF;
		return (((long)long0 << 24) + (long1 << 16) + (long2 << 8) + long3);
	}
	
	
	public static void main(String[] args)
	{
//		long long1 = ((byte)-4 & 0xFF) << 24;	 
//		System.out.println(long1);				// -67108864	
//		long long2 = (long)((byte)-4 & 0xFF) << 24;
//		System.out.println(long2);				// 4227858432
//		
//		long i1 = (long)-4;
//		System.out.println(i1);
//		int i2 = 4;
//		System.out.println(i2);
		byte[] bsa = longToByteArray(4227858432l);
		for (byte b : bsa)
		{
			System.out.print(" " + b);
		}
		byte[] bsb = longToByteArray(-67108864l);
		for (byte b : bsb)
		{
			System.out.print(" " + b);
		}
		System.out.println();
//		
//		byte[] bs = {(byte)-4,(byte)-73,(byte)-65,(byte)38};
//		long a = byteArrayToLong(bs);
//		System.out.println(a);
	}

	/**
	 * 拼接byte数组
	 * 
	 * @param list
	 * @return
	 */
	public static byte[] concatByteArray(List<byte[]> list)
	{
		int totalLength = 0;
		for (byte[] bs : list)
		{
			totalLength += bs.length;
		}
		byte[] byteResult = new byte[totalLength];
		for (byte[] bs : list)
		{
			int destPos = 0;
			int length = 0;
			System.arraycopy(bs, 0, byteResult, destPos, length);
			destPos += length;
		}
		return byteResult;
	}
}
