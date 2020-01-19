package com.chaokong.app;

import com.chaokong.pojo.trace.Trace;
import com.chaokong.tool.MyBuffer;

public class ParseAddiFAMsg implements ParseAdditionalMsg {

	public void parse(Trace trace, MyBuffer buffer, int length) {
		int getableLength = length;
		while(getableLength > 0)
		{
			short addiWarnID = buffer.getShort();
			byte addiWarnLength = buffer.get();
			
			if(addiWarnID == 0x0405) {
				byte warnSensorNum = buffer.get();
				int intLength = warnSensorNum * 2;
				int[] sensorWarnInfo = new int[intLength];
				
				for (int i = 0; i < intLength; i += 2) {
					short sensorNum = buffer.getShort();
					int sensorDistance = buffer.getInt();
					sensorWarnInfo[i] = sensorNum;
					sensorWarnInfo[i + 1] = sensorDistance;
				}
				trace.getWeight().setSensorWarnInfo(sensorWarnInfo);
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
	}

}
