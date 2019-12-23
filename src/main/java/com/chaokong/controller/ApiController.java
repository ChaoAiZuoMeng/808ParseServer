package com.chaokong.controller;

import com.chaokong.pojo.CalibrationData;
import com.chaokong.pojo.ResponseStatus;
import com.chaokong.service.IParseCalibrationService;
import com.chaokong.util.KafkaUtil;
import com.chaokong.util.ParseUtil;
import com.chaokong.util.PropertiesUtil;
import com.google.gson.Gson;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.log4j.Logger;
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
import java.util.Arrays;


@Controller
@RequestMapping("/api")
public class ApiController {

	private final static String TOPIC = PropertiesUtil.getValueByKey("kafka.properties", "kafka.topic_calibrationdata");
	private static Logger calibrationLog = Logger.getLogger("calibrationLog");
	
	@Resource
	private IParseCalibrationService iParseCalibrationService;

	@ResponseBody
	@RequestMapping(value = "/uploadCaliFile", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String receiveCaliFileApi(HttpServletRequest request, HttpServletResponse response) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		int length = 0;
		byte[] b = new byte[1024];
		try {
			is = request.getInputStream();
			while ((length = is.read(b)) != -1) {
				baos.write(b, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new Gson().toJson(new ResponseStatus(500, "网络连接异常"));
		}finally {
			try {
				is.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		byte[] bs = baos.toByteArray();
		String hexData = "";
		try {
			hexData = ParseUtil.binaryToString(bs);
		} catch (UnsupportedEncodingException e) {
			calibrationLog.warn("接收上传的标定数据转换为hex出错" + Arrays.toString(bs));
			e.printStackTrace();
			return new Gson().toJson(new ResponseStatus(500, "标定数据转换失败"));
		}
		CalibrationData calibrationData;
		try {
			calibrationData = iParseCalibrationService.parseCalibrationData(hexData);
		}catch (Exception e) {
			e.printStackTrace();
			return new Gson().toJson(new ResponseStatus(500, "标定数据解析错误,请检查标定参数设置"));
		}
		String simNo = calibrationData.getSimNo();
		byte[] caliDataBuf = calibrationData.getCaliDataBuf();
		KafkaUtil kafka = new KafkaUtil();
		KafkaProducer producer = kafka.getProducer(ByteArraySerializer.class.getName());
		kafka.producerSend(producer, caliDataBuf, TOPIC, simNo);
		return new Gson().toJson(new ResponseStatus(200, "标定数据上传成功"));
	}
}
