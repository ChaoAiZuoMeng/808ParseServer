package com.chaokong.app;

import java.util.Arrays;

import com.chaokong.pojo.trace.ObdInfo;
import com.chaokong.pojo.trace.Trace;
import com.chaokong.tool.MyBuffer;

public class ParseAddiEAMsg implements ParseAdditionalMsg {

	public void parse(Trace trace, MyBuffer buffer, int length) {
		int getableLength = length;
		ObdInfo obdInfo = trace.getObdInfo();
		while(getableLength > 0){
			short addiBaseID = buffer.getShort();
			byte addiBaseLength = buffer.get();
			switch(addiBaseID) {
			case 0x0003:
				buffer.get();
				long GPSTotalMilage = buffer.getUnsignedInt();
				obdInfo.setAllMileage(String.valueOf(GPSTotalMilage));
				break;
			case 0x0004:
				buffer.get();
				long totalOilConsumption = buffer.getUnsignedInt();
				obdInfo.setAvgOilWear(String.valueOf(totalOilConsumption));
				break;
			case 0x0005:
				long totalRunTime = buffer.getUnsignedInt();
				obdInfo.setAllRuntime(String.valueOf(totalRunTime));
				break;
			case 0x0006:
				long totalFlameoutTime = buffer.getUnsignedInt();
				obdInfo.setAllCutOff(String.valueOf(totalFlameoutTime));
				break;
			case 0x0007:
				long totalIdleTime = buffer.getUnsignedInt();
				obdInfo.setAllIdling(String.valueOf(totalIdleTime));
				break;
			case 0x0010:
				short lately1sCollectPointNum = buffer.getShort();
				short lately1sCollectPointTime = buffer.getShort();
				short[] collectPointAccelerationAvg = new short[lately1sCollectPointNum];
				for (int i = 0; i < lately1sCollectPointNum; i++) {
					collectPointAccelerationAvg[i] = buffer.getShort();
				}
				short lately1sMaxAccelerationAvg = buffer.getShort();
				obdInfo.setLastCount(String.valueOf(lately1sCollectPointNum));
				obdInfo.setLastSpace(String.valueOf(lately1sCollectPointTime));
				obdInfo.setAvgSpeed(collectPointAccelerationAvg);
				obdInfo.setMaxSpeed(String.valueOf(lately1sMaxAccelerationAvg));
				break;
			case 0x0012:
				double vehicleVoltage = buffer.getShort() / 10.0;
				obdInfo.setCarVoltage(String.valueOf(vehicleVoltage));
				break;
			case 0x0013:
				double terminalBatteryVoltage = buffer.get() / 10.0;
				obdInfo.setCellVoltage(String.valueOf(terminalBatteryVoltage));
				break;
			case 0x0014:
				int csqValue = buffer.getUnsignedByte();
				obdInfo.setSignalIntensity(String.valueOf(csqValue));
				break;
//			case 0x0015:
//				int vehicleTypeID = buffer.getUnsignedShort();
//				System.err.println("车辆类型ID：" + vehicleTypeID);
//				break;
//			case 0x0016:
//				int obdProtocalType = buffer.getUnsignedByte();
//				System.err.println("OBD协议类型:" + obdProtocalType);
//				break;
//			case 0x0017:
//				int driveCycleTag = buffer.getUnsignedShort();
//				System.err.println("驾驶循环标签:" + driveCycleTag);
//				break;
			case 0x0018:
				int gpsSatelliteNum = buffer.getUnsignedByte();
				obdInfo.setGpsCount(String.valueOf(gpsSatelliteNum));
				break;
			case 0x0019:
				double gpsLocationAccuracy = (buffer.getShort() / 100.0);
				obdInfo.setGpsLocation(String.valueOf(gpsLocationAccuracy));
				break;
			default:
				buffer.gets(addiBaseLength);
				break;
			}
			// +3 每次还要算上消息ID和消息长度 的长度3
			getableLength -= addiBaseLength + 3;
		}
	}
	
}
