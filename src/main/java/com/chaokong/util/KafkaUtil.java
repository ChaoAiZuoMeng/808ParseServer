package com.chaokong.util;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Properties;

public class KafkaUtil {

	private final static String BOOTSTRAP = PropertiesUtil.getValueByKey("kafka.properties", "kafka.url");
	private static Logger logger = Logger.getLogger(KafkaUtil.class);


	/**
	 * 设置参数 返回一个consumer
	 *
	 * @param groupId      groupId
	 * @param deserializer value反序列化  目前所有的key都为string
	 * @param topic        topic
	 * @return KafkaConsumer
	 */
	public KafkaConsumer getConsumer(String groupId, Object deserializer, String topic) {
		Properties props = new Properties();
		// 服务器ip 集群用逗号分隔
		props.put("bootstrap.servers", BOOTSTRAP);
		// 对于每条数据，每个consumer只能消费一次
//			 props.put("group.id", GROUPID);
		props.put("group.id", groupId);
		// 是否自动提交
		props.put("enable.auto.commit", "true");
		// poll的回话处理时长
		props.put("auto.commit.interval.ms", "1000");
		// 心跳时间，超过这个时间被认为是无效的消费者
		props.put("session.timeout.ms", "12000");
		/*
		 * latest：从最新的偏移量开始拉取。
		 * earliest：从最早的偏移量开始拉取。默认
		 */
		props.put("auto.offset.reset", "earliest");
		// 反序列化
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", deserializer);

		KafkaConsumer consumer = new KafkaConsumer(props);
		// 指定消费的topic
		consumer.subscribe(Arrays.asList(topic));
		return consumer;
	}


	/**
	 * 设置参数返回一个 producer
	 *
	 * @param serializer value序列化  目前所有的key都为string
	 * @return KafkaProducer
	 */
	public KafkaProducer getProducer(Object serializer) {
		Properties props = new Properties();
		// ip
		props.put("bootstrap.servers", BOOTSTRAP);
		/*
		 * 用来做应答
		 * 0: 客户端发送数据到kafka，不等待集群应答
		 * 1: 客户端发送数据到kafka，等待leader应答，不等待follower的应答
		 * -1: 客户端发送数据到kafka，leader 等待follower应答，leader再向client应答
		 * all: 同-1
		 * 想要跟高的吞吐量，设置为异步、ack = 0;
		 * 想要数据不丢失，设置为同步、ack = -1\all
		 */
		props.put("acks", "-1");
		props.put("retries", 0);
		props.put("batch.size", 262144);
		props.put("linger.ms", 5);
		props.put("buffer.memory", 67108864);
		// key 序列化的方式 (数据存储到磁盘)
		props.put("key.serializer", StringSerializer.class.getName());
		// value 序列化的方式
		props.put("value.serializer", serializer);
		KafkaProducer producer = new KafkaProducer(props);
		return producer;
	}


	/**
	 * 该方法为测试发送方法
	 *
	 * @param producer 需要一个producer
	 * @param message  任意类型的message
	 * @param topic
	 */
	public void testSend(KafkaProducer producer, Object message, String topic) {
		try {
			for (int i = 0; i < 150; i++) {
				logger.info("开始发送数据 ---");
				producer.send(new ProducerRecord(topic, null, message)).get();
			}
		} catch (Exception e) {
			logger.error("发送异常: " + e.getMessage(), e);
		}
		logger.info("发送成功 ---");
	}


	/**
	 * 指定一个序列化的producer，发送消息到指定topic
	 *
	 * @param producer 指定producer的value序列化
	 * @param message
	 * @param topic
	 */
	public void producerSend(KafkaProducer producer, Object message, String topic) {
		try {
			logger.info("发送到===" + topic + "===" + message);
			producer.send(new ProducerRecord(topic, null, message)).get();
		} catch (Exception e) {
			logger.error("发送异常: " + e.getMessage(), e);
		}
		logger.info("发送成功 ---");
	}

	/**
	 * 指定一个序列化的producer，发送消息到指定topic
	 *
	 * @param producer 指定producer的value序列化
	 * @param message
	 * @param topic
	 * @param key
	 */
	public void producerSend(KafkaProducer producer, Object message, String topic, String key) {
		try {
			logger.info("发送到===" + topic + "===" + key + "===" + message);
			producer.send(new ProducerRecord(topic, key, message)).get();
		} catch (Exception e) {
			logger.error("发送异常: " + e.getMessage(), e);
		}
//		logger.info("发送成功");
	}


	/**
	 * 给定一个consumer，返回该consumer消费到的记录
	 *
	 * @param consumer
	 * @return ConsumerRecords
	 */
	public ConsumerRecords getRecords(KafkaConsumer consumer) {
		ConsumerRecords records = null;
		try {
			// 拉取数据
			records = consumer.poll(1000);
		} catch (Exception e) {
			logger.error("连接失败，请查看日志" + e.getMessage(), e);
		}
		return records;
	}


//	// 消费指定的topic
//	public  void consumerUse(String topic, Object deserializer) {
//		Properties properties = setConsumerProperties(deserializer);
//		KafkaConsumer consumer = new KafkaConsumer<String, Object>(properties);
//		// 指定消费的topic
//		consumer.subscribe(Arrays.asList(topic));
//
//		// 需要不停拉取，不然只尝试一次
//		while (true) {
//
//			ConsumerRecords<String, Object> records = getRecords(consumer);
//			System.out.println(records.count());
//			for (ConsumerRecord<String, Object> record : records) {
//				String key = record.key();
//				Object value = record.value();
//				System.out.println(key + " === " + value);
//			}
//
//		}
//
//	}


}
