package com.chaokong.app;

import com.chaokong.tool.MyBuffer;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ParseAddiECMsg implements ParseAdditionalMsg {

	private static Logger vehicleLog = Logger.getLogger("vehicleLog");

	public Map parse(MyBuffer buffer, int length) {
		int getableLength = length;
		Map<String,String> ecMsg = new HashMap<String,String>();
//		vehicleLog.info("货车扩展数据流:===============================================================");
		while(getableLength > 0){
			short addiVanID = buffer.getShort();
			byte addiVanLength = buffer.get();
			if(addiVanID == 0x60C0) {
				short OBDEngineSpeed = buffer.getShort();
//				vehicleLog.info("OBD转速：" + OBDEngineSpeed);
				ecMsg.put("15", String.valueOf(OBDEngineSpeed));
			}
			else if(addiVanID == 0x60D0) {
				int OBDSpeed = buffer.getUnsignedByte();
//				vehicleLog.info("OBD车速： " + OBDSpeed);
				ecMsg.put("16", String.valueOf(OBDSpeed));
			}
			else if(addiVanID == 0x62F0) {
				short OBDRestOilCapacity = (short) (buffer.getShort() / 10.0);
//				vehicleLog.info("OBD剩余油量百分比:" + OBDRestOilCapacity);
				ecMsg.put("17", String.valueOf(OBDRestOilCapacity));
			}
			else if(addiVanID == 0x6050) {
				int OBDCoolantLiquidTemp = buffer.getUnsignedByte() - 40;
//				vehicleLog.info("OBD冷却液温度：" + OBDCoolantLiquidTemp);
				ecMsg.put("18", String.valueOf(OBDCoolantLiquidTemp));
			}
			else if(addiVanID == 0x60F0) {
				int OBDIntakeTemp = buffer.getUnsignedByte() - 40;
//				vehicleLog.info("OBD进气口温度：" + OBDIntakeTemp);
				ecMsg.put("19", String.valueOf(OBDIntakeTemp));
			}
			else if(addiVanID == 0x60B0) {
				int OBDInletManifoldPress = buffer.getUnsignedByte();
//				vehicleLog.info("OBD进气歧管绝对压力：" + OBDInletManifoldPress);
				ecMsg.put("20", String.valueOf(OBDInletManifoldPress));
			}
			else if(addiVanID == 0x6330) {
				int OBDIntakePress = buffer.getUnsignedByte();
//				vehicleLog.info("OBD进气压力：" + OBDIntakePress);
				ecMsg.put("21", String.valueOf(OBDIntakePress));
			}
			else if(addiVanID == 0x6460) {
				int OBDEnviromentTemp = buffer.getUnsignedByte() - 40;
//				vehicleLog.info("OBD环境温度：" + OBDEnviromentTemp);
				ecMsg.put("22", String.valueOf(OBDEnviromentTemp));
			}
			else if(addiVanID == 0x6490) {
				short OBDAcceleratorPosition = buffer.get();
//				vehicleLog.info("OBD加速踏板位置：" + OBDAcceleratorPosition);
				ecMsg.put("23", String.valueOf(OBDAcceleratorPosition));
			}
			else if(addiVanID == 0x60A0) {
				short OBDOilPress = buffer.getShort();
//				vehicleLog.info("OBD燃油压力：" + OBDOilPress);
				ecMsg.put("24", String.valueOf(OBDOilPress));
			}
			else if(addiVanID == 0x6014) {
				int OBDDTCStatus = buffer.getUnsignedByte();
//				vehicleLog.info("OBD故障码状态：" + OBDDTCStatus);
				ecMsg.put("25", String.valueOf(OBDDTCStatus));
			}
			else if(addiVanID == 0x6010) {
				int OBDDTCNum = buffer.getUnsignedByte();
//				vehicleLog.info("OBD故障码个数：" + OBDDTCNum);
				ecMsg.put("26", String.valueOf(OBDDTCNum));
			}
			else if(addiVanID == 0x500A) {
				short OBDAirFlow = (short) (buffer.getShort() / 10.0);
//				vehicleLog.info("OBD空气流量：" + OBDAirFlow);
				ecMsg.put("27", String.valueOf(OBDAirFlow));
			}
			else if(addiVanID == 0x5001) {
				byte OBDClutchSwitch = buffer.get();
				ecMsg.put("29", String.valueOf(OBDClutchSwitch));
			}
			else if(addiVanID == 0x5002) {
				byte OBDStopBrakeSwitch = buffer.get();
				ecMsg.put("30", String.valueOf(OBDStopBrakeSwitch));
			}
			else if(addiVanID == 0x5003) {
				byte OBDParkBrakeSwitch = buffer.get();
				ecMsg.put("31", String.valueOf(OBDParkBrakeSwitch));
			}
			// 车机上传字节中暂时不包含以下数据
			/*else if(addiVanID == 0x5004) {
				byte OBDThrottleValvePos = buffer.get();
			}
			else if(addiVanID == 0x5005) {
				int OBDFuelUtilizationRate = buffer.getUnsignedShort();
			}
			else if(addiVanID == 0x5006) {
				int OBDFuelTemp = buffer.getUnsignedShort();
			}
			else if(addiVanID == 0x5007) {
				int OBDEngineOilTemp = buffer.getUnsignedShort();
			}
			else if(addiVanID == 0x5008) {
				byte OBDEngineLubePress  = buffer.get();
			}
			else if(addiVanID == 0x5009) {
				byte OBDBrakePedalPos = buffer.get();
			}
			else if(addiVanID == 0x5101) {
				int engineNetOutputTorque = buffer.getUnsignedByte();
			}
			else if(addiVanID == 0x5102) {
				int frictionTorque = buffer.getUnsignedByte();
			}
			else if(addiVanID == 0x5103) {
				int scrUpstreamNOxSensorOutput = buffer.getUnsignedShort();
			}
			else if(addiVanID == 0x5104) {
				int scrDownstreamNOxSensorOutput = buffer.getUnsignedShort();
			}
			else if(addiVanID == 0x5105) {
				short reactantAllowance = buffer.get();
			}
			else if(addiVanID == 0x5106) {
				int airInflow = buffer.getUnsignedShort();
			}
			else if(addiVanID == 0x5107) {
				short scrEntranceTemp = buffer.getShort();
			}
			else if(addiVanID == 0x5108) {
				short scrExitTemp = buffer.getShort();
			}
			else if(addiVanID == 0x5109) {
				short dpfPressDiff = buffer.getShort();
			}
			else if(addiVanID == 0x510A) {
				byte engineTorqueMode = buffer.get(); 
			}
			else if(addiVanID == 0x510B) {
				byte acceleratorPedal = buffer.get();
			}
			else if(addiVanID == 0x510C) {
				byte ureaBoxTemp = buffer.get();
			}
			else if(addiVanID == 0x510D) {
				int actualUreaInjection = buffer.getInt();
			}
			else if(addiVanID == 0x510E) {
				int cumulativeUreaConsumption = buffer.getInt();
			}
			else if(addiVanID == 0x510F) {
				short dpfExhaustTemp = buffer.getShort();
			}
			else if(addiVanID == 0x5110) {
				short engineFuelFlow = buffer.getShort();
			}*/
			else {
				buffer.gets(addiVanLength);
			}
			getableLength -= addiVanLength + 3;
		}
		return ecMsg;
	}

}
