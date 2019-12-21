package com.chaokong.pojo.response;

import com.chaokong.pojo.MessageBody;
import com.chaokong.tool.Transfer;
import com.chaokong.util.KafkaUtil;
import com.chaokong.util.PropertiesUtil;
import lombok.Data;
import org.apache.kafka.clients.producer.KafkaProducer;

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
	// 生成消息体  传到command topic
	private final static String TOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_command");
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
	public void assembly(String key, KafkaProducer producer) throws UnsupportedEncodingException {

		String indicateHex = Transfer.byteToHex(getIndicate());
		String textHex = Transfer.str2HexStr(getText(), "gbk");
		String  value = indicateHex + textHex;
		producerSend(key, value, producer); 
	}

	private void producerSend(String key, String message, KafkaProducer producer) {
		KafkaUtil kafka = new KafkaUtil();
		kafka.producerSend(producer, message, TOPIC, key);
	}
}
