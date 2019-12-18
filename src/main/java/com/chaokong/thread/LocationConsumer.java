package com.chaokong.thread;

import com.chaokong.app.App;
import com.chaokong.tool.Tools;
import com.chaokong.util.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.log4j.Logger;

import java.util.Optional;

// 接收msg0200 发送至 大数据 gateway
public class LocationConsumer implements Runnable {
	private static Logger logger = Logger.getLogger(Kafka.class);
	private static Logger vehicleLog = Logger.getLogger("vehicleLog");
	//		private final static String BOOTSTRAP = PropertiesUtil.getValueByKey("kafka.properties", "kafka.url");
	// static String BOOTSTRAP = "172.18.0.45:9092";
	// static String BOOTSTRAP = "192.168.8.95:9092";
	private static String BOOTSTRAP = "10.211.55.3:9092";
	private final static String SENDTOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_gpsdata");
	private final static String ACCEPTTOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_msg");
	private final static String GROUPID = PropertiesUtil.getValueByKey("kafka.properties", "kafka.group.id");


	@Override
	public void run() {
		consumer();
	}

	private void consumer() {
		KafkaUtil kafka = new KafkaUtil();
		// 加载生产者和消费者的配置
		KafkaConsumer consumer = kafka.getConsumer(BOOTSTRAP, GROUPID, ByteArrayDeserializer.class.getName(), ACCEPTTOPIC);
		logger.info("开始接收数据。");
		KafkaProducer producer = kafka.getProducer(BOOTSTRAP, ByteArraySerializer.class.getName());

		// 需要不停拉取，不然只尝试一次
		while (true) {
			ConsumerRecords<String, byte[]> records = kafka.getRecords(consumer);

			if (!hasObject(records)) {
				logger.error("找不到记录。");
			} else {
				resolveProducerMessage(records, producer);
			}
		}
	}


	// 从gateway(topic)接收
	private void resolveProducerMessage(ConsumerRecords<String, byte[]> records, KafkaProducer producer) {
//		String name = Thread.currentThread().getName();
//		System.err.println(name);
		if (records.isEmpty())
			logger.warn("没有接收到数据，数据记录数为: " + records.count() + "条。");
		else
			logger.info("接收到" + records.count() + "条数据。");
		for (ConsumerRecord<String, byte[]> record : records) {
			byte[] message = record.value();
			// log
			messageLog(message, "接收到的数据为: ");

			// parse
			YunCar.Car car = parseMessage(message);


			if (!hasObject(car))
				logger.error("解析数据异常，此数据不会发送");
			else
				producerSend(producer, car.toByteArray());
			vehicleLog.info("车辆信息：" + car);
		}
	}


	private YunCar.Car parseMessage(byte[] message) {
		YunCar.Car car = null;
		try {
			// parse  byte[] -> protobuf
			car = App.parse0200MessageBody(message);
		} catch (Exception e) {
			logger.error("数据解析失败，查看传输数据是否正确");
		}
		return car;

	}


	// 发送到新的topic
	private void producerSend(KafkaProducer producer, byte[] message) {
		try {
			logger.info("开始发送数据 ---");
			producer.send(new ProducerRecord<String, byte[]>(SENDTOPIC, null, message));
			messageLog(message, "发送的数据为");
			logger.info("以上数据发送成功 ---");
		} catch (Exception e) {
			logger.error("发送异常: " + e.getMessage(), e);
		}
	}

	// 用日志对消息进行格式化输出
	private void messageLog(byte[] message, String logName) {

		String hex = Tools.bytes2hex(message);
		String log = logName;
		for (byte b : message) {
			log += b + " ";
		}
		logger.info(log);
		logger.info("hex: " + hex);
	}

	/*
	if null return false
	or
	not null return true
 	*/
	private boolean hasObject(Object object) {
		return Optional.ofNullable(object).isPresent();
	}


}
