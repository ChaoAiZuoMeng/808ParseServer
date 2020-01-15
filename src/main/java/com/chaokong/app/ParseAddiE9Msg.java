package com.chaokong.app;

import java.util.ArrayList;
import java.util.List;

import com.chaokong.pojo.trace.SensorsInfo;
import com.chaokong.pojo.trace.Trace;
import com.chaokong.tool.MyBuffer;

public class ParseAddiE9Msg implements ParseAdditionalMsg {

	public void parse(Trace trace, MyBuffer buffer, int length) {
		int weightUnit = buffer.getUnsignedByte();
		int ratedWeight = buffer.getUnsignedShort();
		int currentWeight = buffer.getUnsignedShort();
		int dataType = buffer.get();

		List<SensorsInfo> sensorInfoList = new ArrayList<>();
		int sensorDataLength = (length - 6) >> 2;
		int[] sensorData = new int[sensorDataLength];
		SensorsInfo sensorsInfo = null;
		short j = 1;
		for (int i = 0; i < sensorDataLength; i += 2) {
			sensorData[i] = buffer.getInt();
			sensorData[i + 1] = buffer.getInt();
			
			sensorsInfo = new SensorsInfo();
			sensorsInfo.setSensorLength(sensorData[i] + "");
			sensorsInfo.setSensorWeight(sensorData[i + 1] + "");
			sensorsInfo.setSensorIndex(j);
			sensorInfoList.add(sensorsInfo);
			j++;
		}
		
		trace.setAllWeight(ratedWeight + "");
		// ** HOLLOO 实时重量 * 10
		trace.setNowWeight(currentWeight * 10 + "");
		trace.getWeight().setSensorCount((short)(sensorDataLength >> 1));
		trace.getWeight().setSensorInfoList(sensorInfoList);;
	}
}
