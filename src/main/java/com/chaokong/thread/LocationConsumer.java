package com.chaokong.thread;

import com.chaokong.app.App;
import com.chaokong.tool.Tools;
import com.chaokong.util.KafkaUtil;
import com.chaokong.util.PropertiesUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Optional;

// 接收msg0200 发送至 大数据 gateway
public class LocationConsumer implements Runnable {
    private static Logger logger = Logger.getLogger("dailyFile");
    //	private static Logger vehicleLog = Logger.getLogger("vehicleLog");
    private static Logger error = Logger.getLogger("errorFile");
    private final static String SENDTOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_gpsdata");
    private final static String ACCEPTTOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_msg");
    private final static String GROUPID = PropertiesUtil.getValueByKey("kafka.properties", "kafka.group.id");

    public volatile boolean flag = true;

    @Override
    public void run() {
        while (flag)
            consumer();
    }

    public void shutDown() {
        flag = false;
    }

    private void consumer() {
        KafkaUtil kafka = new KafkaUtil();
        // 加载生产者和消费者的配置
        KafkaConsumer consumer = kafka.getConsumer(GROUPID, ByteArrayDeserializer.class.getName(), ACCEPTTOPIC);
//		logger.info("开始接收数据。");
        KafkaProducer producer = kafka.getProducer(StringSerializer.class.getName());

        // 需要不停拉取，不然只尝试一次
        while (flag) {
            ConsumerRecords<String, byte[]> records = kafka.getRecords(consumer);

            if (!hasObject(records)) {
                logger.error("找不到记录。");
            } else {
                resolveProducerMessage(records, producer);
            }
        }
    }


    // 从msg0200(topic)接收
    private void resolveProducerMessage(ConsumerRecords<String, byte[]> records, KafkaProducer producer) {
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
            for (ConsumerRecord<String, byte[]> record : records) {
                byte[] message = record.value();

                logger.info("接收===" + ACCEPTTOPIC + "===消息体数据===" + Tools.bytes2hex(message));

                try {
                    String car = parseMessage(message);
                    producerSend(producer, car);
//					vehicleLog.info("车辆信息：" + car);
                } catch (Exception e) {
                    error.error(Tools.bytes2hex(message));
                    error.error("解析数据异常，此数据不会发送");
                }
            }
        }
    }


    private String parseMessage(byte[] message) {
        String car = null;
        try {
            // parse  byte[] -> JSON
            car = App.parse0200MessageBody(message);
        } catch (Exception e) {
            error.error("0200数据消息体解析失败===" + Tools.bytes2hex(message));
        }
        return car;

    }


    // 发送到新的topic
    private void producerSend(KafkaProducer producer, String message) {
        try {
            producer.send(new ProducerRecord<String, String>(SENDTOPIC, null, message));
            logger.info("发送到===" + SENDTOPIC + "===JSON数据===" + message);
        } catch (Exception e) {
            error.error("发送异常: " + e.getMessage(), e);
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


}
