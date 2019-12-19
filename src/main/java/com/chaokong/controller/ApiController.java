package com.chaokong.controller;

import com.chaokong.pojo.CalibrationData;
import com.chaokong.service.IParseCalibrationService;
import com.chaokong.util.KafkaUtil;
import com.chaokong.util.ParseUtil;
import com.chaokong.util.PropertiesUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


@Controller
@RequestMapping("/api")
public class ApiController {

	private static String BOOTSTRAP = "10.211.55.3:9092";
	private final static String TOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_calibrationdata");
	@Resource
	private IParseCalibrationService iParseCalibrationService;

	@ResponseBody
	@RequestMapping(value = "/uploadCaliFile", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public void receiveCaliFileApi(HttpServletRequest request, HttpServletResponse response) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is;
		int length = 0;
		byte[] b = new byte[1024];
		try {
			is = request.getInputStream();
			while ((length = is.read(b)) != -1) {
				baos.write(b, 0, length);
			}
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bs = baos.toByteArray();
		String hexData = "";
		try {
			hexData = ParseUtil.binaryToString(bs);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		CalibrationData calibrationData = iParseCalibrationService.parseCalibrationData(hexData);
		String simNo = calibrationData.getSimNo();
		byte[] caliDataBuf = calibrationData.getCaliDataBuf();
		KafkaUtil kafka = new KafkaUtil();
		KafkaProducer producer = kafka.getProducer(BOOTSTRAP, ByteArraySerializer.class.getName());
		kafka.producerSend(producer, caliDataBuf, TOPIC, simNo);
	}
}
