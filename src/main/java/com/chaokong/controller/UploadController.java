package com.chaokong.controller;

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

public class UploadController extends HttpServlet {
	private static String BOOTSTRAP = "10.211.55.3:9092";
	// command
	private final static String TOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_command");

	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

		// 8300
		// String msgId = request.getParameter("msgId");

		String simNo = request.getParameter("simNo");
		String msgBody = request.getParameter("info");
		String hex = Transfer.str2HexStr(msgBody, "gbk");
		KafkaUtil kafkaUtil = new KafkaUtil();
		KafkaProducer producer = kafkaUtil.getProducer(StringSerializer.class.getName());
		kafkaUtil.producerSend(producer, hex, TOPIC, simNo);

	}


	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response) {
	}

}