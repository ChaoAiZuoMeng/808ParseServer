package com.chaokong.util;

import com.chaokong.app.App;
import com.chaokong.tool.Tools;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

public class Kafka {

	private static Logger logger = Logger.getLogger(Kafka.class);
	private static Logger vehicleLog = Logger.getLogger("vehicleLog");

//		private final static String BOOTSTRAP = PropertiesUtil.getValueByKey("kafka.properties", "kafka.url");
	private final static String SENDTOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_gpsdata");
	private final static String ACCEPTTOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_msg");
	private final static String GROUPID = PropertiesUtil.getValueByKey("kafka.properties", "kafka.group.id");
		static String BOOTSTRAP = "10.211.55.3:9092";
//		static String BOOTSTRAP = "172.18.0.45:9092";
//	static String BOOTSTRAP = "192.168.8.95:9092";


	private Kafka() { }

	/**
	 *
	 * @param deserializer 指定value的反序列化
	 * @return
	 */
	private static Properties setConsumerProperties(Object deserializer) {
		Properties props = new Properties();
		// 服务器ip 集群用逗号分隔
		props.put("bootstrap.servers", BOOTSTRAP);
		// 对于每条数据，每个consumer只能消费一次
//			 props.put("group.id", GROUPID);
			props.put("group.id", "4123");
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
			return props;
	}

	/**
	 *
	 * @param serializer 指定value的序列化
	 * @return
	 */
	private static Properties setProducerProperties(Object serializer) {
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
		return props;
	}


	public static void resolveProducerMessageAndSend() {
		// 加载生产者和消费者的配置
		KafkaConsumer consumer = initConsumer();
		logger.info("开始接收数据。");
		KafkaProducer producer = initProducer();

		// 需要不停拉取，不然只尝试一次
		while (true) {
			ConsumerRecords<String, byte[]> records = getRecords(consumer);

			if (!hasObject(records)) {
				logger.error("找不到记录。");
			} else {

				resolveProducerMessage(records, producer);
			}
		}

	}


	// 从gateway(topic)接收
	private static void resolveProducerMessage(ConsumerRecords<String, byte[]> records, KafkaProducer producer) {
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


	private static YunCar.Car parseMessage(byte[] message) {
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
	private static void producerSend(KafkaProducer producer, byte[] message) {
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
	private static void messageLog(byte[] message, String logName) {

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
	private static boolean hasObject(Object object) {
		return Optional.ofNullable(object).isPresent();
	}

	private static ConsumerRecords getRecords(KafkaConsumer consumer) {
		ConsumerRecords<String, byte[]> records = null;
		try {
			// 拉取数据
			records = consumer.poll(1000);
		} catch (Exception e) {
			logger.error("连接失败，请查看日志" + e.getMessage(), e);
		}
		return records;
	}

	// 给consumer加载配置，并指定topic
	private static KafkaConsumer initConsumer() {
		Properties properties = setConsumerProperties(ByteArrayDeserializer.class.getName());
		KafkaConsumer consumer = new KafkaConsumer<String, byte[]>(properties);
		// 指定消费的topic
//		consumer.subscribe(Arrays.asList(ACCEPTTOPIC));
		consumer.subscribe(Arrays.asList(ACCEPTTOPIC));
		return consumer;
	}

	private static KafkaProducer initProducer() {
		Properties properties = setProducerProperties(ByteArraySerializer.class.getName());
		return new KafkaProducer<String, byte[]>(properties);
	}

	/**
	 * 发送消息到指定的topic
	 * @param message
	 * @param topic
	 * @param serializer value的序列化
	 */
	public static void producerSendMessage(Object message, String topic, Object serializer, String key) {
		Properties properties = setProducerProperties(serializer);
		KafkaProducer<String, Object> producer = new KafkaProducer<String, Object>(properties);
			try {
				producer.send(new ProducerRecord<String, Object>(topic, key, message)).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		logger.info("发送成功");
	}

	// 测试消费数据
	public static void consumerUse() {
		Properties properties = setConsumerProperties(ByteArrayDeserializer.class.getName());
		KafkaConsumer consumer = new KafkaConsumer<String, byte[]>(properties);
		// 指定消费的topic
		consumer.subscribe(Arrays.asList("msg0200"));
//		consumer.subscribe(Arrays.asList("gateway"));

		// 需要不停拉取，不然只尝试一次
		while (true) {
			ConsumerRecords<String, byte[]> records = getRecords(consumer);
			System.out.println(records.count());
			for (ConsumerRecord<String, byte[]> record : records) {
				byte[] value = record.value();
				System.out.print("收到数据：");
				for (byte b : value) {
					System.out.print(b + " ");
				}
				System.out.println();
			}

		}

	}


	// 消费指定的topic
	public static void consumerUse(String topic, Object deserializer) {
		Properties properties = setConsumerProperties(deserializer);
		KafkaConsumer consumer = new KafkaConsumer<String, Object>(properties);
		// 指定消费的topic
		consumer.subscribe(Arrays.asList(topic));
//		consumer.subscribe(Arrays.asList("gateway"));

		// 需要不停拉取，不然只尝试一次
		while (true) {
			ConsumerRecords<String, Object> records = getRecords(consumer);
			System.out.println(records.count());
			for (ConsumerRecord<String, Object> record : records) {
				String key = record.key();
				Object value = record.value();
				System.out.println(key + " === " + value);
			}

		}

	}


}
