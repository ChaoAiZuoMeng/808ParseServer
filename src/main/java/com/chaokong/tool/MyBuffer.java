package com.chaokong.tool;

import org.apache.mina.core.buffer.IoBuffer;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

public class MyBuffer {

	public IoBuffer getBuff() {
		return buff;
	}

	IoBuffer buff;

	public MyBuffer() {
		buff = IoBuffer.allocate(1536);
		buff.mark();

	}

	public MyBuffer(int len) {
		buff = IoBuffer.allocate(len);
		buff.mark();
	}

	public MyBuffer(byte[] bytes) {
		if (bytes.length > 1024)
			buff = IoBuffer.allocate(bytes.length + 100);
		else
			buff = IoBuffer.allocate(1024);
		buff.mark();
		buff.put(bytes);
		buff.limit(bytes.length);
		buff.reset();
	}

	public void clear() {
		buff.clear();
		buff.mark();
	}

	public void put(byte a) {
		buff.put(a);
	}

	public void put(short a) {
		buff.putShort(a);
	}

	public void put(byte[] a) {
		buff.put(a);
	}

	public boolean hasRemain() {
		return buff.remaining() > 0;
	}

	public void put(int a) {
		buff.putInt(a);
	}

	public void putShort(int a) {
		buff.putShort((short) a);
	}

	public void put(String str) {
		// US-ASCII

		try {
			byte[] b = str.getBytes("gbk");
			buff.put(b);

		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void put(String str, int len) {
		byte[] result = new byte[len];
		try {
			byte[] b = str.getBytes("gbk");

			System.arraycopy(b, 0, result, 0, b.length);

			for (int m = b.length; m < len; m++) {
				result[m] = 0;
			}
			buff.put(result);

		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public byte get() {
		return buff.get();
	}

	public byte[] gets(int len) {
		byte[] data = new byte[len];
		buff.get(data);
		return data;
	}

	public int getInt() {
		return buff.getInt();
	}

	public short getShort() {
		return buff.getShort();
	}

	// 将data字节型数据转换为0~65535 (0xFFFF 即 WORD)。
	public int getUnsignedShort() {
		short t = buff.getShort();
		return t & 0xffff;
	}

	// 将data字节型数据转换为0~255 (0xFF 即BYTE)。
	public int getUnsignedByte() {
		return buff.get() & 0x0FF;
	}

	public long getUnsignedInt() {
		return buff.getInt() & 0x0FFFFFFFF;
	}

	public String getString() {
		try {
			String strTemp = buff
					.getString(Charset.forName("GBK").newDecoder());
			return strTemp;
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getString(int len) {
		try {
			String strTemp = buff.getString(len, Charset.forName("GBK")
					.newDecoder());
			return strTemp;
		} catch (CharacterCodingException e) {
			e.printStackTrace();
			gets(len);
		}
		return "";
	}

	public String getBcdString(int len) {
		byte[] bytes = this.gets(len);
		StringBuilder bcd = new StringBuilder();
		for (int m = 0; m < len; m++) {
			bcd.append(String.format("%02X", bytes[m]));
		}
		return bcd.toString();
	}

	public byte[] array() {
		int pos = buff.position();
		byte[] data = new byte[pos];
		buff.reset();
		buff.get(data);
		return data;
	}

	public static void main(String[] args) {

		IoBuffer ib = IoBuffer.allocate(1024);
		ib.mark();
		ib.put((byte) 128);
		ib.reset();
		// byte b = ib.get();
		// int x = b& 0xff;
		short x = ib.getUnsigned();

		short y = ib.getUnsigned(0);

//		System.out.println("" + x + "," + y);
	}

	public int getlength(){
		return buff.remaining();
	}
}
