package com.chaokong.app;

import com.chaokong.pojo.trace.ObdInfo;
import com.chaokong.pojo.trace.Trace;
import com.chaokong.tool.MyBuffer;

public class ParseAddiECMsg implements ParseAdditionalMsg {

	public void parse(Trace trace, MyBuffer buffer, int length) {
		int getableLength = length;
		ObdInfo obdInfo = trace.getObdInfo();
		while(getableLength > 0){
			short addiVanID = buffer.getShort();
			byte addiVanLength = buffer.get();
			switch (addiVanID) {
			case 0x60C0:
				short OBDEngineSpeed = buffer.getShort();
				obdInfo.setObdRevSpeed(String.valueOf(OBDEngineSpeed));
				break;
			case 0x60D0:
				int OBDSpeed = buffer.getUnsignedByte();
				obdInfo.setObdSpeed(String.valueOf(OBDSpeed));
				break;
			case 0x62F0:
				short OBDRestOilCapacity = (short) (buffer.getShort() / 10.0);
				obdInfo.setObdDoilpre(String.valueOf(OBDRestOilCapacity));
				break;
			case 0x6050:
				int OBDCoolantLiquidTemp = buffer.getUnsignedByte() - 40;
				obdInfo.setObdCoolTemp(String.valueOf(OBDCoolantLiquidTemp));
				break;
			case 0x60F0:
				int OBDIntakeTemp = buffer.getUnsignedByte() - 40;
				obdInfo.setObdAirTemp(String.valueOf(OBDIntakeTemp));
				break;
			case 0x60B0:
				int OBDInletManifoldPress = buffer.getUnsignedByte();
				obdInfo.setObdAirAbsPress(String.valueOf(OBDInletManifoldPress));
				break;
			case 0x6330:
				int OBDIntakePress = buffer.getUnsignedByte();
				obdInfo.setObdAirPress(String.valueOf(OBDIntakePress));
				break;
			case 0x6460:
				int OBDEnviromentTemp = buffer.getUnsignedByte() - 40;
				obdInfo.setObdEnvTemp(String.valueOf(OBDEnviromentTemp));
				break;
			case 0x6490:
				short OBDAcceleratorPosition = buffer.get();
				obdInfo.setObdExpediteLocation(String.valueOf(OBDAcceleratorPosition));
				break;
			case 0x60A0:
				short OBDOilPress = buffer.getShort();
				obdInfo.setObdFuelPress(String.valueOf(OBDOilPress));
				break;
			case 0x6014:
				int OBDDTCStatus = buffer.getUnsignedByte();
				obdInfo.setObdFaultStatus(String.valueOf(OBDDTCStatus));
				break;
			case 0x6010:
				int OBDDTCNum = buffer.getUnsignedByte();
				obdInfo.setObdFaultCount(String.valueOf(OBDDTCNum));
				break;
			case 0x500A:
				short OBDAirFlow = (short) (buffer.getShort() / 10.0);
				obdInfo.setObdAirFlow(String.valueOf(OBDAirFlow));
				break;
			case 0x5001:
				byte OBDClutchSwitch = buffer.get();
				obdInfo.setObdClutch(String.valueOf(OBDClutchSwitch));
				break;
			case 0x5002:
				byte OBDStopBrakeSwitch = buffer.get();
				obdInfo.setObdBrake(String.valueOf(OBDStopBrakeSwitch));
				break;
			case 0x5003:
				byte OBDParkBrakeSwitch = buffer.get();
				obdInfo.setObdPark(String.valueOf(OBDParkBrakeSwitch));
				break;
			// 车机上传字节中暂时不包含以下数据
			/*case 0x5004:
				byte OBDThrottleValvePos = buffer.get();
				break;
			case 0x5005:
				int OBDFuelUtilizationRate = buffer.getUnsignedShort();
				break;
			case 0x5006:
				int OBDFuelTemp = buffer.getUnsignedShort();
				break;
			case 0x5007:
				int OBDEngineOilTemp = buffer.getUnsignedShort();
				break;
			case 0x5008:
				byte OBDEngineLubePress  = buffer.get();
				break;
			case 0x5009:
				byte OBDBrakePedalPos = buffer.get();
				break;
			case 0x5101:
				int engineNetOutputTorque = buffer.getUnsignedByte();
				break;
			case 0x5102:
				int frictionTorque = buffer.getUnsignedByte();
				break;
			case 0x5103:
				int scrUpstreamNOxSensorOutput = buffer.getUnsignedShort();
				break;
			case 0x5104:
				int scrDownstreamNOxSensorOutput = buffer.getUnsignedShort();
				break;
			case 0x5105:
				short reactantAllowance = buffer.get();
				break;
			case 0x5106:
				int airInflow = buffer.getUnsignedShort();
				break;
			case 0x5107:
				short scrEntranceTemp = buffer.getShort();
				break;
			case 0x5108:
				short scrExitTemp = buffer.getShort();
				break;
			case 0x5109:
				short dpfPressDiff = buffer.getShort();
				break;
			case 0x510A:
				byte engineTorqueMode = buffer.get(); 
				break;
			case 0x510B:
				byte acceleratorPedal = buffer.get();
				break;
			case 0x510C:
				byte ureaBoxTemp = buffer.get();
				break;
			case 0x510D:
				int actualUreaInjection = buffer.getInt();
				break;
			case 0x510E:
				int cumulativeUreaConsumption = buffer.getInt();
				break;
			case 0x510F:
				short dpfExhaustTemp = buffer.getShort();
				break;
			case 0x5110:
				short engineFuelFlow = buffer.getShort();
				break;*/
			default:
				buffer.gets(addiVanLength);
				break;
			}
			getableLength -= addiVanLength + 3;
		}
	}

}
