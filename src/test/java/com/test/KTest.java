package com.test;

import com.chaokong.tool.Transfer;
import com.chaokong.util.KafkaUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KTest {


	public static void main(String[] args) {
		String bb = "00124810084000000000000c0303021dc48506fec50f00e202e7012e191225104848140400000004170200010104000b3317030202e52504000000002a020000300113310112710402120b07e93602791897900100006c750000313800006811000037aa000058db0000109a000069cb0000177000006697000003b6000057d7000002eeea040200c000ef0400008400";
		byte[] bytes = Transfer.hexStrToBytes(bb);
		KafkaUtil kafkaUtil = new KafkaUtil();
		KafkaProducer producer = kafkaUtil.getProducer(StringSerializer.class.getName());
		KafkaProducer producer1 = kafkaUtil.getProducer(ByteArraySerializer.class.getName());
		byte[] aa = {1};
		for (int i = 0; i < 10; i++) {
//			kafkaUtil.producerSend(producer1,bytes, "msg0200");
//			kafkaUtil.producerSend(producer1, aa, "msg0200");
			kafkaUtil.producerSend(producer,"{\n" +
					"    \"id\":8300,\n" +
					"    \"simNo\":\"1000019\",\n" +
					"    \"indicate\":\"12\",\n" +
					"    \"text\":\"hello\"\n" +
					"}", "jsonMsg");
		}
	}
}
