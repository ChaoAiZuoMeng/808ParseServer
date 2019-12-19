package com.chaokong.thread;

import com.chaokong.factory.MessageBodyFactory;
import com.chaokong.pojo.MessageBody;
import com.chaokong.util.Kafka;
import com.chaokong.util.KafkaUtil;
import com.chaokong.util.PropertiesUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

// 从 command 接收 发送至 hexMsg （由各消息体实现）
public class ControllerConsumer implements Runnable {
	private static Logger logger = Logger.getLogger(Kafka.class);

	// private final static String BOOTSTRAP = PropertiesUtil.getValueByKey("kafka.properties", "kafka.url");
	// static String BOOTSTRAP = "172.18.0.45:9092";
	// static String BOOTSTRAP = "192.168.8.95:9092";
	private static String BOOTSTRAP = "10.211.55.3:9092";
	// command
	private final static String TOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_command");
	private final static String GROUPID = PropertiesUtil.getValueByKey("kafka.properties", "kafka.group.id");


	@Override
	public void run() {
		consumer();
	}


	/**
	 * 目前从kafka接收json
	 * 之后需要从接口获取
	 * 注意这里的topic 已经有测试方法 发送到该topic(command) 也就是说消费该topic会产生问题
	 */
	public void consumer() {
		KafkaUtil kafka = new KafkaUtil();
		// 加载生产者和消费者的配置
		KafkaConsumer consumer = kafka.getConsumer(BOOTSTRAP, GROUPID, StringDeserializer.class.getName(), TOPIC);
		logger.info("开始接收数据。");
		KafkaProducer producer = kafka.getProducer(BOOTSTRAP, StringSerializer.class.getName());

		// 需要不停拉取，不然只尝试一次
		while (true) {
			ConsumerRecords<String, String> records = kafka.getRecords(consumer);

			if (!hasObject(records)) {
				logger.error("找不到记录。");
			} else {
				resolveProducerMessage(records, producer);
			}
		}
	}

	// 从gateway(topic)接收
	private void resolveProducerMessage(ConsumerRecords<String, String> records, KafkaProducer producer) {
//		String name = Thread.currentThread().getName();
//		System.err.println(name);
		if (records.isEmpty()) {
			logger.warn("没有接收到数据，数据记录数为: " + records.count() + "条。");
		} else {
			logger.info("接收到" + records.count() + "条数据。");
			for (ConsumerRecord<String, String> record : records) {
				String json = record.value();
				logger.info("接收到的数据是" + json);
//			String json = "{\"id\":8300,\n" +
//					"    \"indicate\":\"12\",\n" +
//					"    \"text\":\"你好吗\"\n" +
//					"}";

				String id = getIdByJson(json);
				assembly(id, json, producer);
			}
		}
	}

	/*
	if null return false
	or
	not null return true
 	*/
	private boolean hasObject(Object object) {
		return Optional.ofNullable(object).isPresent();
	}

	private static void assembly(String id, String json, KafkaProducer producer) {
		Gson gson = new Gson();
		MessageBody instance = MessageBodyFactory.getInstanceByMessageId(id);
		// json转为对象
		MessageBody object = gson.fromJson(json, instance.getClass());
//		System.out.println(jt);
		try {
			object.assembly(id, producer);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	private static String getIdByJson(String json) {
		JsonParser jsonParser = new JsonParser();
		JsonElement element = jsonParser.parse(json);
		JsonObject root = element.getAsJsonObject();
		return root.getAsJsonPrimitive("id").toString();
	}


}
