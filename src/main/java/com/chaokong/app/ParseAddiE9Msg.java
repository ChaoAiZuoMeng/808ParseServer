package com.chaokong.app;

import com.chaokong.tool.MyBuffer;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ParseAddiE9Msg implements ParseAdditionalMsg {

	private static Logger vehicleLog = Logger.getLogger("vehicleLog");

	public Map parse(MyBuffer buffer, int length) {
//		vehicleLog.info("重量数据流：===========================================================");
		int weightUnit = buffer.getUnsignedByte();
		int ratedWeight = buffer.getUnsignedShort();
		int currentWeight = buffer.getUnsignedShort();
		int dataType = buffer.get();

		int sensorDataLength = (length - 6) >> 2;
		int[] sensorData = new int[sensorDataLength];
		for (int i = 0; i < sensorDataLength; i += 2) {
			sensorData[i] = buffer.getInt();
			sensorData[i + 1] = buffer.getInt();
		}
		
//		vehicleLog.info("重量单位：" + weightUnit);
//		vehicleLog.info("额定载重：" + ratedWeight);
//		vehicleLog.info("当前载重:" + currentWeight);
//		vehicleLog.info("数据类型:" + dataType);
//		vehicleLog.info("传感器距离和重量:" + Arrays.toString(sensorData));
		
		Map<String, Object> e9Msg = new HashMap<String, Object>();
		e9Msg.put("weightUnit", weightUnit);
		e9Msg.put("ratedWeight", ratedWeight);
		// ** HOLLOO 实时重量 * 10
		e9Msg.put("currentWeight", currentWeight * 10);
		e9Msg.put("dataType", dataType);
		e9Msg.put("sensorData", sensorData);
		
		return e9Msg;
	}
}
