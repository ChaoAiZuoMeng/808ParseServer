package com.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.chaokong.pojo.trace.Trace;
import com.chaokong.tool.Transfer;
import com.chaokong.util.KafkaUtil;
import com.chaokong.util.YunCar;
import com.chaokong.util.YunCar.Car;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;

public class KTest {

	private static Logger logger = Logger.getLogger("testLog");
	private static Logger error = Logger.getLogger(KTest.class);

	public static void main(String[] args) {
		String bb = "00124810084000000000000c0303021dc48506fec50f00e202e7012e191225104848140400000004170200010104000b3317030202e52504000000002a020000300113310112710402120b07e93602791897900100006c750000313800006811000037aa000058db0000109a000069cb0000177000006697000003b6000057d7000002eeea040200c000ef0400008400";
		byte[] bytes = Transfer.hexStrToBytes(bb);
		KafkaUtil kafkaUtil = new KafkaUtil();
		KafkaProducer producer = kafkaUtil.getProducer(StringSerializer.class.getName());
		KafkaProducer producer1 = kafkaUtil.getProducer(ByteArraySerializer.class.getName());
		byte[] aa = {1};
		int i = 0;
		while (i < 10) {
			i++;
//			kafkaUtil.producerSend(producer1, bytes, "msg0200");
			kafkaUtil.producerSend(producer1, aa, "msg0200");
//			kafkaUtil.producerSend(producer, "{\n" +
//					"    \"id\":8300,\n" +
//					"    \"simNo\":\"1000019\",\n" +
//					"    \"indicate\":\"12\",\n" +
//					"    \"text\":\"hello\"\n" +
//					"}", "jsonMsg");
		}


	}

	// 从 gateway 获得 protobuf格式 数据 
	@Test
	public void test() throws InvalidProtocolBufferException {

		KafkaUtil kafkaUtil = new KafkaUtil();
		KafkaConsumer consumer = kafkaUtil.getConsumer("sk11", ByteArrayDeserializer.class.getName(), "gateway");
		while (true) {

			ConsumerRecords<String, byte[]> records = kafkaUtil.getRecords(consumer);
			for (ConsumerRecord<String, byte[]> record : records) {
				byte[] message = record.value();
				YunCar.Car car = YunCar.Car.parseFrom(message);
				String obdid1 = car.getDetails().getObdId();
				String uptime1 = car.getDetails().getUptime();
				if(obdid1.equals("017395501499") && uptime1.startsWith("2020-03-17")) {
					logger.info(car);
				}
			}
			
		}
	}
	
	// 从 jsongateway 获得 json格式 数据
	@Test
	public void testJson() throws InvalidProtocolBufferException {

		KafkaUtil kafkaUtil = new KafkaUtil();
		KafkaConsumer consumer = kafkaUtil.getConsumer("sk8", StringDeserializer.class.getName(), "jsongateway");
		while (true) {

			ConsumerRecords<String, String> records = kafkaUtil.getRecords(consumer);
			for (ConsumerRecord<String, String> record : records) {
				String message = record.value();
				Gson gson = new Gson();
				Trace trace = gson.fromJson(message, Trace.class);
				String obdid1 = trace.getMacID();
				String uptime1 = trace.getUptime();
				if(obdid1.equals("017395501499") && uptime1.startsWith("2020-03-17")){
					logger.info(message);
				}
			}
			
		}
	}

	@Test
	public void test1() {
		error.info("123");
		            error.error("123");
	}

}
