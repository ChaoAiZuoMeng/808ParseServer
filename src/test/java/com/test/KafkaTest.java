package com.test;


import com.chaokong.thread.ControllerConsumer;
import com.chaokong.util.KafkaUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

public class KafkaTest {

	private static String BOOTSTRAP = "10.211.55.3:9092";

	@Test
	public void test() {

//		String yuwei = "001242101430373032313800000000000c03020217ba10070c52f20056000000e11910212359551404000000000104000155122504000000002a02000030011831010f7104020e0b03e9360279182af80100ffffff000000000000710200002ac60000532c00000000000060250000000000005ed2000000320000660600000000ea040200c000ef0400000400";
//		String yuwei2 = "001242101430373032313800040000000c030301e9946406451539028902b500df19102214425814040000000417020001010400065bc0030202c32504000000002a02000030011f31010b7104020b0a01e936027918aa820100006a0d00003d860000680e000037aa0000569f000015e0000069aa000017d4000064d40000044c0000565600000352ea040200c000ef0400008400";
//		String hexNoOBD = "012030405066363636383800000000000000010159c68c06cbd91e0000000000001910221008360104000000030202000003020000e936027530982601000019aa00005208000040d40000448e0000c350000000000000c35000000000000052a1000001900000c35000000000ea5a0003050100000187000405000000000000050400009e380006040000814a0007040000000000100e000400fa0008000800060006000c001202008d001301000014011f0015020000001601000017020019001801000019020000";
//		String hexWithOBD = "012030405066363636383800000000000000030159c48206cbd9910095000001421911041425440104000000000202000003020000e93602802000000100ffffff0000000000ffffff0000000000ffffff0000000000ffffff0000000000ffffff0000000000ffffff00000000ea5a00030501000000000004050000000000000504000001f4000604000000220007040000000000100e000400fa00070008000800070009001202011b0013010000140116001502fe0c001601f000170200010018010500190201c2ec3960c002044460d0010062f00200006050012860f0012860b001646330016464600128649002000d60a00200006014010060100100500a020000ed0b7001040000000070030100";
//		String chehulu = "012030405060363636383800000000000000030159c3a306cbd67500c4000000081910221422070104000000050202000003020000e93602753076060100002eaa00005208000036e70000520800003869000052080000308a00005208000033700000164400002ca8000017a2ea5a000305010000022600040500000000000005040000d590000604000081820007040000000000100e000400fa0003000400030005000a001202008d001301000014011f001502000000160100001702001900180106001902019a";
//		String hexWarn = "012030405066363636383800000000000000030159c3d206cbd8c800a4000000f21910241842560104000003740202000003020000fa1604051303000100ffffff000200ffffff000300ffffff";
//		byte[] bs = Tools.hexStringToByteArray(chehulu);
//
		byte[] bs = {1};
//		Kafka.producerSendMessage(bs, "haha", ByteArraySerializer.class.getName(), "13888");
//		Kafka.resolveProducerMessageAndSend();
	}


	@Test
	public void consumer() {
//		 Kafka.consumerUse("haha", StringDeserializer.class.getName());
//		Kafka.consumerUse("haha", ByteArrayDeserializer.class.getName());
	}

	@Test
	public void th() {

		byte[] msg = {1, 1};

		KafkaUtil kafka = new KafkaUtil();
		// 模拟位置信息
		KafkaProducer producer = kafka.getProducer(ByteArraySerializer.class.getName());
		kafka.testSend(producer, msg, "msg0200");
		// 模拟前端发送消息
		KafkaProducer producer1 = kafka.getProducer(StringSerializer.class.getName());
		String json = "{\"id\":8300,\n" +
				" \"simNo\":123, \n" +
				"    \"indicate\":\"12\",\n" +
				"    \"text\":\"你好吗\"\n" +
				"}";
		kafka.testSend(producer1, json, "command");
	}

	public static void main(String[] args) {
		ControllerConsumer controllerConsumer = new ControllerConsumer();

		Thread thread = new Thread(controllerConsumer, "controller");
		thread.start();


	}


}
