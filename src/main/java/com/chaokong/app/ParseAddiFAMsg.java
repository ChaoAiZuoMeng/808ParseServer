package com.chaokong.app;

import com.chaokong.tool.MyBuffer;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ParseAddiFAMsg implements ParseAdditionalMsg {

	private static Logger vehicleLog = Logger.getLogger("vehicleLog");

	public Map parse(MyBuffer buffer, int length) {
		int getableLength = length;
		Map<String,String> faMsg = new HashMap<String,String>();
//		vehicleLog.info("扩展包报警数据流：=============================================================");
		while(getableLength > 0)
		{
			short addiWarnID = buffer.getShort();
			byte addiWarnLength = buffer.get();
			if(addiWarnID == 0x0405) {
				byte warnSensorNum = buffer.get();
//				vehicleLog.info("传感器报警个数：" + warnSensorNum);
				faMsg.put("28", String.valueOf(warnSensorNum));
				for (int i = 0; i < warnSensorNum; i++) {
					short sensorNum = buffer.getShort();
					int sensorDistance = buffer.getInt();
//					vehicleLog.info(sensorNum + "号传感器报警，距离:" + sensorDistance);
					faMsg.put("28" + sensorNum, String.valueOf(sensorDistance));
				}
			}
//			else if(addiWarnID == 0x0106) {
//				byte idleWarnQuality = buffer.get();
////				System.out.println("怠速报警属性：" + idleWarnQuality);
//				if(idleWarnQuality == (byte)0) {
//					int warnLastTime = buffer.getUnsignedShort();
////					System.out.println("报警持续时长：" + warnLastTime);
//					int idleOilConsumption = buffer.getUnsignedShort();
////					System.out.println("怠速耗油量：" + idleOilConsumption);
//					int idleMaxSpeed = buffer.getUnsignedShort();
////					System.out.println("怠速转速最大值：" + idleMaxSpeed);
//					int idleMinSpeed = buffer.getUnsignedShort();
////					System.out.println("怠速转速最小值：" + idleMinSpeed);
//				}
//			}
			else {
				buffer.gets(addiWarnLength);
			}
			// +3 每次还要算上消息ID和消息长度 的长度3
			getableLength -= addiWarnLength + 3;
		}
		return faMsg;
	}

}
