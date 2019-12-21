package com.chaokong.thread;

import com.chaokong.factory.MessageBodyFactory;
import com.chaokong.pojo.MessageBody;
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

// 从 jsonMsg 接收 发送至 command （由各消息体实现）
public class ControllerConsumer implements Runnable {

	public volatile boolean flag = true;

	private static Logger logger = Logger.getLogger(ControllerConsumer.class);

	// command
	private final static String TOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.json_msg");
	private final static String GROUPID = PropertiesUtil.getValueByKey("kafka.properties", "kafka.group.id");


	@Override
	public void run() {
		while (flag)
			consumer();
	}

	public void shutDown() {
		flag = false;
	}


	/**
	 * 目前从kafka接收json
	 * 之后需要从接口获取
	 * 注意这里的topic 已经有测试方法 发送到该topic(command) 也就是说消费该topic会产生问题
	 */
	public void consumer() {
		KafkaUtil kafka = new KafkaUtil();
		// 加载生产者和消费者的配置
		KafkaConsumer consumer = kafka.getConsumer(GROUPID, StringDeserializer.class.getName(), TOPIC);
//		logger.info("开始接收数据。");
		KafkaProducer producer = kafka.getProducer(StringSerializer.class.getName());

		// 需要不停拉取，不然只尝试一次
		while (flag) {
			ConsumerRecords<String, String> records = kafka.getRecords(consumer);

			if (!hasObject(records)) {
				logger.error("找不到记录。");
			} else {
				resolveProducerMessage(records, producer);
			}


		}
	}

	private void resolveProducerMessage(ConsumerRecords<String, String> records, KafkaProducer producer) {
//		String name = Thread.currentThread().getName();
//		System.err.println(name);
		if (records.isEmpty()) {
//			logger.warn("没有接收到数据，数据记录数为: " + records.count() + "条。");
			try {
				Thread.sleep(3000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
//			logger.info("接收到" + records.count() + "条数据。");
			for (ConsumerRecord<String, String> record : records) {
				String json = record.value();
				logger.info("接收到下发指令数据" + json);

				String key = getIdAndSimNoByJson(json);
				if (hasObject(key)) {
					assembly(key, json, producer);
				}
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

	private static void assembly(String key, String json, KafkaProducer producer) {
		Gson gson = new Gson();
		String id = key.substring(0, 4);
		MessageBody instance = MessageBodyFactory.getInstanceByMessageId(id);
		// json转为对象
		MessageBody object = gson.fromJson(json, instance.getClass());
//		System.out.println(jt);
		try {
			object.assembly(key, producer);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	private static String getIdAndSimNoByJson(String json) {
		try {
			JsonParser jsonParser = new JsonParser();
			JsonElement element = jsonParser.parse(json);
			JsonObject root = element.getAsJsonObject();
			String id = root.getAsJsonPrimitive("id").toString();
			String simNO = root.getAsJsonPrimitive("simNo").toString();
			return id + ":" +simNO;
		} catch (Exception e) {
			logger.error(json + "数据格式不正确 " + e.getMessage(), e);
		}
		return null;
	}


}
