package com.chaokong.pojo.response;

import com.chaokong.pojo.MessageBody;
import com.chaokong.tool.Transfer;
import com.chaokong.util.Kafka;
import com.chaokong.util.PropertiesUtil;
import lombok.Data;
import org.apache.kafka.common.serialization.StringSerializer;

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
		String response = id + ":" + getIndicate() + getText();
		String hex = Transfer.str2HexStr(response, "gbk");
		Kafka.producerSendMessage(hex, TOPIC, StringSerializer.class.getName(),null);
	}


}
