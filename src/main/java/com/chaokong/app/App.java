package com.chaokong.app;

import com.chaokong.thread.ControllerConsumer;
import com.chaokong.thread.LocationConsumer;
import com.chaokong.tool.CoordinateTransformUtil;
import com.chaokong.tool.DateUtil;
import com.chaokong.tool.MyBuffer;
import com.chaokong.tool.StringUtil;
import com.chaokong.util.YunCar.*;
import com.chaokong.util.YunCar.Details.Builder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;

@Service
public class App {

	private static Logger vehicleLog = Logger.getLogger("vehicleLog");


	@PostConstruct
	public void launch() {
		Thread controller = new Thread(new ControllerConsumer(), "controller");
		controller.start();
		Thread location = new Thread(new LocationConsumer(), "location");
		location.start();
//		System.out.println("init");
	}


	@PreDestroy
	public void destory() {
		System.out.println("dead");
	}


	public static Car parse0200MessageBody(byte[] bytes) {
		MyBuffer buffer = new MyBuffer(bytes);
		// 转换为protobuf
		Car.Builder carBuilder = Car.newBuilder();
		Details.Builder detailBuilder = Details.newBuilder();
		Sensor.Builder sensorBuilder = Sensor.newBuilder();
		Describe.Builder describeBuilder = Describe.newBuilder();
		Alarm.Builder alarmbuilder = Alarm.newBuilder();

		// 终端手机号	SimNo 	BCD[6]
		// 制造商ID 		BYTE[5]
		String obdId = buffer.getBcdString(6);
//        long manufacturerId = Tools.bytesToLong(buffer.gets(5));
//        vehicleLog.info("终端obd:" + obdId);
//        vehicleLog.info("制造商id:" + manufacturerId);

		// 基本位置信息
		int alarmFlag = buffer.getInt();
		int status = buffer.getInt();
		int latitude = buffer.getInt();
		int longitude = buffer.getInt();
		short altitude = buffer.getShort();
		short speed = buffer.getShort();
		short course = buffer.getShort();
		byte[] timeBytes = buffer.gets(6);
		String time = "20" + String.format("%02X", timeBytes[0]) + "-" + String.format("%02X", timeBytes[1]) + "-"
				+ String.format("%02X", timeBytes[2]) + " " + String.format("%02X", timeBytes[3]) + ":"
				+ String.format("%02X", timeBytes[4]) + ":" + String.format("%02X", timeBytes[5]);
		String uptime = DateUtil.datetimeToString(DateUtil.getSystemDate());
		String strWarn = Integer.toBinaryString(alarmFlag);
		strWarn = StringUtil.leftPad(strWarn, 32, '0');
		String strStatus = Integer.toBinaryString(status);
		strStatus = StringUtil.leftPad(strStatus, 32, '0');

//		vehicleLog.info("报警标志：" + alarmFlag);
//		vehicleLog.info("状态：" + status);
//		vehicleLog.info("纬度：" + latitude);
//		vehicleLog.info("经度：" + longitude);
//		vehicleLog.info("高程：" + altitude);
//		vehicleLog.info("速度：" + speed);
//		vehicleLog.info("方向：" + course);
//		vehicleLog.info("gps时间：" + time);
//		vehicleLog.info("上传时间："+ uptime);

		// 附加位置信息
		String allMillage = "";
		String oilHeight = "";
		ParseAdditionalMsg pam;
		Map msgMap;
		while (buffer.getlength() > 2) {
			int additionId = buffer.getUnsignedByte();
			int additionLength = buffer.getUnsignedByte();

//			vehicleLog.info("附加信息ID:" + additionId);
//			vehicleLog.info("附加信息长度：" + additionLength);


			if (additionLength > 0) {
				if (additionId == 0xE9) {
					pam = new ParseAddiE9Msg();
					msgMap = pam.parse(buffer, additionLength);
					toE9Proto(msgMap, detailBuilder, sensorBuilder);
				} else if (additionId == 0x01) {
					allMillage = (buffer.getInt() / 10.0) + "";
				} else if (additionId == 0x02) {
					oilHeight = (buffer.getShort() / 10.0) + "";
				} else if (additionId == 0xEA && additionLength > 4) {
					pam = new ParseAddiEAMsg();
					msgMap = pam.parse(buffer, additionLength);
					toStatusProto(msgMap, describeBuilder);
				} else if (additionId == 0xEC) {
					pam = new ParseAddiECMsg();
					msgMap = pam.parse(buffer, additionLength);
					toStatusProto(msgMap, describeBuilder);
				} else if (additionId == 0xFA) {
					pam = new ParseAddiFAMsg();
					msgMap = pam.parse(buffer, additionLength);
					toStatusProto(msgMap, describeBuilder);
				} else {
					buffer.gets(additionLength);
				}
			}
		}

		detailBuilder.setUptime(uptime);
		detailBuilder.setGPSTime(time);
		detailBuilder.setObdId(obdId);
		double[] doubles = CoordinateTransformUtil.wgs84togcj02(
				Double.parseDouble(longitude / 1000000.000000 + ""),
				Double.parseDouble(latitude / 1000000.000000 + ""));
		doubles = CoordinateTransformUtil.gcj02tobd09(doubles[0], doubles[1]);
		detailBuilder.setLatitude(String.format("%.6f", new Object[]{Double.valueOf(doubles[1])}));
		detailBuilder.setLongitude(String.format("%.6f", new Object[]{Double.valueOf(doubles[0])}));
		detailBuilder.setSpeed(speed / 10.0 + "");
		detailBuilder.setDirection(course + "");
//		describeBuilder.setStatusPrimeval(strStatus);
//		alarmbuilder.setAlarmPrimeval(strWarn);
		describeBuilder.setStatusMetadata(status);
		alarmbuilder.setAlarmMetadata(alarmFlag);
		alarmbuilder.setAlsrmDispose("0");
		detailBuilder.setAllMileage(allMillage);
		detailBuilder.setOilHeight(oilHeight);
		detailBuilder.setElevation(altitude + "");
		detailBuilder.setAcc(strStatus.substring(31));
		detailBuilder.setLocation(strStatus.substring(30, 31));

		// 若有obd数据，更新为obd数据
		List<Status> statusList = describeBuilder.getStatusList();
		if (statusList != null) {
			for (Status statuss : statusList) {
				if (statuss.getStatusFields().equals("16")) {
					detailBuilder.setSpeed(statuss.getStatusvalue());
				} else if (statuss.getStatusFields().equals("1")) {
					detailBuilder.setAllMileage(statuss.getStatusvalue());
				} else if (statuss.getStatusFields().equals("17")) {
					detailBuilder.setNowOilWear(statuss.getStatusvalue());
				} else if (statuss.getStatusFields().equals("2")) {
					detailBuilder.setAvgOilWear(statuss.getStatusvalue());
				} else if (statuss.getStatusFields().equals("22")) {
					detailBuilder.setCentigrade(statuss.getStatusvalue());
				}
			}
		}

		Car car = carBuilder.setDetails(detailBuilder.build()).setAlarm(alarmbuilder.build()).setDescribe(describeBuilder.build())
				.setSensor(sensorBuilder.build()).build();
		return car;

	}

	// EA基础数据流数据转为proto
	// EC货车扩展数据流数据转为proto
	// FA报警扩展数据流转为proto
	private static void toStatusProto(Map map, com.chaokong.util.YunCar.Describe.Builder describeBuilder) {
		for (Object keys : map.keySet()) {
			String key = (String) keys;
			String value = (String) map.get(key);
			describeBuilder.addStatus(Status.newBuilder().setStatusFields(key).setStatusvalue(value).build());
		}
	}

	// E9传感器重量数据转为proto
	private static void toE9Proto(Map msgMap, Builder detailBuilder, com.chaokong.util.YunCar.Sensor.Builder sensorBuilder) {
		detailBuilder.setAllWeight(msgMap.get("ratedWeight") + "");
//		if(manufacturerId == fetchManufacturerId("cheHuLu")) {
		detailBuilder.setNowWeight(msgMap.get("currentWeight") + "");
		int[] sensorData = (int[]) msgMap.get("sensorData");
		int sensorIndex = 1;
		for (int i = 0; i < sensorData.length; i += 2) {
			String sensorLength = String.valueOf(sensorData[i]);
			String sensorWeight = String.valueOf(sensorData[i + 1]);
			sensorBuilder.addSensorInfo(SensorInfo.newBuilder().setSensorLength(sensorLength).setSensorWeight(sensorWeight).setSensorIndex(String.valueOf(sensorIndex)).build());
			sensorIndex++;
		}

	}

	// 配置文件中获取制造商ID
	/*private static long fetchManufacturerId(String manufacturerName)
	{
		return Long.valueOf(PropertiesUtil.getValueByKey("manufacturerId.properties", manufacturerName));
	}*/
}
