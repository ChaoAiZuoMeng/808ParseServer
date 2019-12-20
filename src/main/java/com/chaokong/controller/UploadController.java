package com.chaokong.controller;

import com.chaokong.tool.MyBuffer;
import com.chaokong.tool.Tools;
import com.chaokong.tool.Transfer;
import com.chaokong.util.KafkaUtil;
import com.chaokong.util.PropertiesUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class UploadController extends HttpServlet {
	private static String BOOTSTRAP = "192.168.8.95:9092";
	// command
	private final static String TOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_command");

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

		// 8300
		// String msgId = request.getParameter("msgId");

		String simNo = request.getParameter("simNo");
		String info = request.getParameter("info");
		MyBuffer buffer = new MyBuffer();
		buffer.put((byte)1);
		buffer.put(info);
		String msgBody = Tools.bytes2hex(buffer.array());
		buffer.getBuff().reset();
		KafkaUtil kafkaUtil = new KafkaUtil();
		KafkaProducer producer = kafkaUtil.getProducer(StringSerializer.class.getName());
		kafkaUtil.producerSend(producer, msgBody, TOPIC, simNo);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	}
}