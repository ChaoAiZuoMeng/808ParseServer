package com.chaokong.pojo.response;


import com.chaokong.pojo.MessageBody;
import lombok.Data;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.UnsupportedEncodingException;

/**
 * 平台通用应答
 *
 * @author huakaiMay
 */
@Data
public class JT_8001 implements MessageBody {
	private short responseMessageSerialNo;
	private short responseMessageId;
	private byte responseResult;



	public String toString() {
		return "应答结果:" + this.responseResult;
	}


	@Override
	public void assembly(String id, KafkaProducer producer) throws UnsupportedEncodingException {
		
	}
}


