package com.chaokong.service.impl;

import com.chaokong.pojo.CalibrationData;
import com.chaokong.service.IParseCalibrationService;
import com.chaokong.tool.MyBuffer;
import com.chaokong.tool.Tools;
import com.chaokong.util.KafkaUtil;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ParseCalibrationServiceImpl implements IParseCalibrationService {

	private static Logger calibrationLog = Logger.getLogger("calibrationLog");

	/**
	 * 解析出上传标定文件的数据(HexString) 生成透传内容的数据内容(byte[])
	 *
	 * @param caliHex
	 * @return
	 */
	public CalibrationData parseCalibrationData(String caliHex) {

		calibrationLog.info("接收到标定数据==" + caliHex);
		String[] split = caliHex.split(":");
		String simNo = split[0];
		while(simNo.length() < 12) {
			simNo = "0" + simNo;
		}
		String messageContent = split[1];
		messageContent = messageContent.replace(",", "");
		messageContent = messageContent.replace("\n", "");
		messageContent = messageContent.replace("\\", "");
		messageContent = messageContent.replace(" ", "");
		byte[] bytes = Tools.HexString2Bytes(messageContent);

		MyBuffer myBuffer = new MyBuffer(bytes);
		// 车牌号[12]
		byte[] car_no = myBuffer.gets(12);
		// 额定载重[4]
		int zaizhong_eding = myBuffer.getInt();
		// 分度值[2]
		short aShort = myBuffer.getShort();
		// 传感器个数[1]
		byte no_sensor = myBuffer.get();
		// 单传感器记录数[2]
		short log_single = myBuffer.getShort();
		// 标定数据集合体
		ArrayList<byte[]> logs = new ArrayList<>();
		int i1 = (no_sensor + 1) * 2;
		for (int i = log_single; i > 0; i--) {
			byte[] gets = myBuffer.gets(i1);
			logs.add(gets);
		}
		// CRC32校验码[4]
		int anInt = myBuffer.getInt();
		MyBuffer buff = new MyBuffer(20 + logs.size() * ((byte[]) logs.get(0)).length * 2);
		buff.put(10);
		buff.put(zaizhong_eding * 100);
		buff.put(no_sensor + 0);
		buff.put(log_single);
		buff.put(new byte[]{0, 0, 0, 0, 0, 0});
		for (byte[] log : logs) {
			buff.put(new byte[]{0, 0});
			buff.put(log[log.length - 2]);
			buff.put(log[log.length - 1]);
			for (int i = 0; i < log.length - 2; i += 2) {
				buff.put(new byte[]{0, 0});
				buff.put(log[i]);
				buff.put(log[i + 1]);
			}
		}
		buff.getBuff().reset();
		CalibrationData caliData = new CalibrationData();
		caliData.setSimNo(simNo);
		caliData.setCaliDataBuf(buff.gets(buff.getlength()));
		return caliData;
	}

	public static void main(String[] args) {
		String caliHex = "016594700014:e8b7af422d5445535400000000000fa0000a06000c827c839b69f24de20000823c835c69a74d8e000a81fb831d695c4d3b001481bb82de69114ce7001e817a829e68c64c940028813a825f687b4c40003280f9822068304bed003c80b981e067e54b990046807981a1679a4b46005080388162674f4af2005a7ff8812367044a9f00647fb780e366b94a4b006e734a9e47"; 
		ParseCalibrationServiceImpl pcs = new ParseCalibrationServiceImpl();
		CalibrationData calibrationData = pcs.parseCalibrationData(caliHex);
		System.err.println("simNo: " + calibrationData.getSimNo());
		System.err.println("data: " + Tools.bytes2hex(calibrationData.getCaliDataBuf()));
		String simNo = calibrationData.getSimNo();
		byte[] caliDataBuf = calibrationData.getCaliDataBuf();
		KafkaUtil kafka = new KafkaUtil();
		KafkaProducer producer = kafka.getProducer(ByteArraySerializer.class.getName());
		kafka.producerSend(producer, caliDataBuf, "caliDataDown", simNo);
	}
}
