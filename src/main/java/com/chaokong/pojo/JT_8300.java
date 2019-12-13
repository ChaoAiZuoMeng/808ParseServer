package com.chaokong.pojo;

import com.chaokong.tool.Transfer;
import com.chaokong.util.Kafka;
import com.chaokong.util.PropertiesUtil;
import lombok.Data;

import java.io.UnsupportedEncodingException;

/**
 * 文本信息下发
 * 起始字节	 字段	     数据类型	     描述及要求
 * 0	     标志	     BYTE			 文本信息标志位含义见表27
 * 1	     文本信息	 STRING	 		 最长为102字节，经GBK编码
 *
 * @author huakaiMay
 */

@Data
public class JT_8300 implements MessageBody {
	private final static String TOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.cgi_issue");
	/**
	 * 标志
	 */
	private byte indicate;

	/**
	 * 文本信息
	 */
	private String text;


	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(String.format("标志：%1$s,文本：%2$s", getIndicate(), getText()));
		return sBuilder.toString();
	}

	@Override
	public void assembly(String id) throws UnsupportedEncodingException {
		// 以gbk编码，每个中文占两个字节
		byte[] messageBody = new byte[getText().length() * 2 + 1];
		String hex = assembly(messageBody, id);
		Kafka.producerSendMessage(hex, TOPIC);
	}

	private String assembly(byte[] messageBody, String id) throws UnsupportedEncodingException {
		// 标识位
		messageBody[0] = getIndicate();
		try {
			byte[] texts = getText().getBytes("gbk");
			for (int i = 0; i < texts.length; i++) {
				// 文本信息
				messageBody[i + 1] = texts[i];
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		String idHex = Transfer.str2HexStr(id, "gbk");
//		System.out.println(idHex);
		String msgHex = Transfer.bytesToHex(messageBody);
//		System.out.println("hex: " + msgHex);

		String response = idHex + msgHex;
		return response;
	}
}
