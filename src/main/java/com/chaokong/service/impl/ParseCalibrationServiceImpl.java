package com.chaokong.service.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.chaokong.pojo.CalibrationData;
import com.chaokong.service.IParseCalibrationService;
import com.chaokong.tool.MyBuffer;
import com.chaokong.tool.Tools;

@Service
public class ParseCalibrationServiceImpl implements IParseCalibrationService {

	/**
	 *  解析出上传标定文件的数据(HexString) 生成透传内容的数据内容(byte[])
	 * 
	 * @param caliHex
	 * @return
	 */
	public CalibrationData parseCalibrationData(String caliHex) {

		System.out.println(caliHex);
		String[] split = caliHex.split(":");
		String simNo = split[0];
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
		buff.put(new byte[] { 0, 0, 0, 0, 0, 0 });
		for (byte[] log : logs) {
			buff.put(new byte[] { 0, 0 });
			buff.put(log[log.length - 2]);
			buff.put(log[log.length - 1]);
			for (int i = 0; i < log.length - 2; i += 2) {
				buff.put(new byte[] { 0, 0 });
				buff.put(log[i]);
				buff.put(log[i + 1]);
			}
		}
		buff.getBuff().reset();
		CalibrationData caliData = new CalibrationData();
		caliData.setSimNo(simNo);
		caliData.setCaliDataBuf(buff.array());
		return caliData;
	}
}
