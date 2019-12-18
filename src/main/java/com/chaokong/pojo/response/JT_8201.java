package com.chaokong.pojo.response;

import com.chaokong.pojo.MessageBody;
import lombok.Data;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.UnsupportedEncodingException;

/**
 * 位置信息查询,位置信息查询消息体为空
 *
 * @author huakaiMay
 */
@Data
public class JT_8201 implements MessageBody {


	@Override
	public void assembly(String id, KafkaProducer producer) throws UnsupportedEncodingException {
		
	}
}