package com.chaokong.app;

import com.chaokong.tool.MyBuffer;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParseAddiEAMsg implements ParseAdditionalMsg {

	private static Logger vehicleLog = Logger.getLogger("vehicleLog");

	public Map parse(MyBuffer buffer, int length) {
		int getableLength = length;
		Map<String,String> eaMsg = new HashMap<String,String>();
//		vehicleLog.info("基础数据流：===========================================================");
		while(getableLength > 0){
			short addiBaseID = buffer.getShort();
			byte addiBaseLength = buffer.get();
			if(addiBaseID == 0x0003) {
				buffer.get();
				long GPSTotalMilage = buffer.getUnsignedInt();
//				vehicleLog.info("总里程：" + GPSTotalMilage);
				eaMsg.put("1", String.valueOf(GPSTotalMilage));
			}
			else if(addiBaseID == 0x0004) {
				buffer.get();
				long totalOilConsumption = buffer.getUnsignedInt();
//				vehicleLog.info("总油耗：" + totalOilConsumption);
				eaMsg.put("2", String.valueOf(totalOilConsumption));
			}
			else if(addiBaseID == 0x0005) {
				long totalRunTime = buffer.getUnsignedInt();
//				vehicleLog.info("总运行时长：" + totalRunTime);
				eaMsg.put("3", String.valueOf(totalRunTime));
			}
			else if(addiBaseID == 0x0006) {
				long totalFlameoutTime = buffer.getUnsignedInt();
//				vehicleLog.info("总熄火时长:" + totalFlameoutTime);
				eaMsg.put("4", String.valueOf(totalFlameoutTime));
			}
			else if(addiBaseID == 0x0007) {
				long totalIdleTime = buffer.getUnsignedInt();
//				vehicleLog.info("总怠速时长：" + totalIdleTime);
				eaMsg.put("5", String.valueOf(totalIdleTime));
			}
			else if(addiBaseID == 0x0010) {
				short lately1sCollectPointNum = buffer.getShort();
				short lately1sCollectPointTime = buffer.getShort();
				short[] collectPointAccelerationAvg = new short[lately1sCollectPointNum];
				for (int i = 0; i < lately1sCollectPointNum; i++) {
					collectPointAccelerationAvg[i] = buffer.getShort();
				}
				short lately1sMaxAccelerationAvg = buffer.getShort();
//				vehicleLog.info("最近1秒内采集点个数：" + lately1sCollectPointNum);
//				vehicleLog.info("最近1秒内采集点间隔：" + lately1sCollectPointTime);
//				vehicleLog.info("每个采集点加速度平均值：" + Arrays.toString(collectPointAccelerationAvg));
//				vehicleLog.info("最近1秒最大加速度：" + lately1sMaxAccelerationAvg);
				eaMsg.put("6", String.valueOf(lately1sCollectPointNum));
				eaMsg.put("7", String.valueOf(lately1sCollectPointTime));
				eaMsg.put("8", Arrays.toString(collectPointAccelerationAvg));
				eaMsg.put("9", String.valueOf(lately1sMaxAccelerationAvg));
			}
			else if(addiBaseID == 0x0012) {
				double vehicleVoltage = buffer.getShort() / 10.0;
//				vehicleLog.info("车辆电压：" + vehicleVoltage);
				eaMsg.put("10", String.valueOf(vehicleVoltage));
			}
			else if(addiBaseID == 0x0013){
				double terminalBatteryVoltage = buffer.get() / 10.0;
//				vehicleLog.info("终端内置电池电压：" + terminalBatteryVoltage);
				eaMsg.put("11", String.valueOf(terminalBatteryVoltage));
			}
			else if(addiBaseID == 0x0014) {
				int csqValue = buffer.getUnsignedByte();
//				vehicleLog.info("网络信号强度：" + csqValue);
				eaMsg.put("12", String.valueOf(csqValue));
			}
//			else if(addiBaseID == 0x0015) {
//				int vehicleTypeID = buffer.getUnsignedShort();
//				System.err.println("车辆类型ID：" + vehicleTypeID);
//				eaMsg.put("车辆类型ID", String.valueOf(vehicleTypeID));
//			}
//			else if(addiBaseID == 0x0016) {
//				int obdProtocalType = buffer.getUnsignedByte();
//				System.err.println("OBD协议类型:" + obdProtocalType);
//				eaMsg.put("OBD协议类型", String.valueOf(obdProtocalType));
//			}
//			else if(addiBaseID == 0x0017) {
//				int driveCycleTag = buffer.getUnsignedShort();
//				System.err.println("驾驶循环标签:" + driveCycleTag);
//				eaMsg.put("驾驶循环标签", String.valueOf(driveCycleTag));
//			}
			else if(addiBaseID == 0x0018) {
				int gpsSatelliteNum = buffer.getUnsignedByte();
//				vehicleLog.info("GPS收星数:" + gpsSatelliteNum);
				eaMsg.put("13", String.valueOf(gpsSatelliteNum));
			}
			else if(addiBaseID == 0x0019) {
				double gpsLocationAccuracy = (buffer.getShort() / 100.0);
//				vehicleLog.info("GPS位置精度：" + gpsLocationAccuracy);
				eaMsg.put("14", String.valueOf(gpsLocationAccuracy));
			}
			else {
				buffer.gets(addiBaseLength);
			}
			// +3 每次还要算上消息ID和消息长度 的长度3
			getableLength -= addiBaseLength + 3;
		}
		return eaMsg;
	}
	
}
