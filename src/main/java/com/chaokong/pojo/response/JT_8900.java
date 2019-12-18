package com.chaokong.pojo.response;


import com.chaokong.pojo.MessageBody;
import lombok.Data;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.UnsupportedEncodingException;

/**
 * 数据下行透传
 *
 * @author huakaiMay
 */
@Data
public class JT_8900 implements MessageBody {
	private int count;
	private byte messageType;
	private byte[] messageContent;


	@Override
	public void assembly(String id, KafkaProducer producer) throws UnsupportedEncodingException {
		
	}
}


