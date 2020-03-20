package com.chaokong.app;

import com.chaokong.pojo.trace.*;
import com.chaokong.thread.ControllerConsumer;
import com.chaokong.thread.LocationConsumer;
import com.chaokong.tool.MyBuffer;
import com.chaokong.tool.Tools;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;



@Service
public class App {
    private static Logger vehicleLog = Logger.getLogger("vehicleLog");
    private static ControllerConsumer controllerConsumer = new ControllerConsumer();
    private static LocationConsumer locationConsumer = new LocationConsumer();

    @PostConstruct
    public void launch() {
        Thread controller = new Thread(controllerConsumer, "controller");
        controller.start();
        Thread location = new Thread(locationConsumer, "location");
        location.start();
    }

    @PreDestroy
    public void destory() {
        controllerConsumer.shutDown();
        locationConsumer.shutDown();
    }

    public static String parse0200MessageBody(byte[] bytes) {
        MyBuffer buffer;
        buffer = new MyBuffer(bytes);

        Trace trace = new Trace();
        trace.setAlarm(new Alarm());
        trace.setObdInfo(new ObdInfo());
        trace.setWeight(new Weight());
        trace.setOthers(new Others());

        trace.parseBasicLocMsg(buffer);
        trace.parseAddiLocMsg(buffer);

        Gson gson = new Gson();
        return gson.toJson(trace);
    }

    // 配置文件中获取制造商ID
	/*private static long fetchManufacturerId(String manufacturerName)
	{
		return Long.valueOf(PropertiesUtil.getValueByKey("manufacturerId.properties", manufacturerName));
	}*/

    public static void main(String[] args) {
        String obd = "01739550121900000000000000030159c48206cbd9910095000001421911041425440104000000000202000003020000e93602802000000100ffffff0000000000ffffff0000000000ffffff0000000000ffffff0000000000ffffff0000000000ffffff00000000ea5a00030501000000000004050000000000000504000001f4000604000000220007040000000000100e000400fa00070008000800070009001202011b0013010000140116001502fe0c001601f000170200010018010500190201c2ec3860c002044460d0010062f00200006050012860f0012860b0016463300164646001286490010d60a00200006014010060100100500a020000ed0b7001040000000070030100";
        String noObd = "01739550121900000000000000010159c68c06cbd91e0000000000001910221008360104000000030202000003020000e936027530982601000019aa00005208000040d40000448e0000c350000000000000c35000000000000052a1000001900000c35000000000ea5a0003050100000187000405000000000000050400009e380006040000814a0007040000000000100e000400fa0008000800060006000c001202008d001301000014011f0015020000001601000017020019001801000019020000";
        String hexWarn = "01203040506600000000000000030159c3d206cbd8c800a4000000f21910241842560104000003740202000003020000fa1604051303000100ffffff000200ffffff000300ffffff";
        String yuwei = "00124210143000000000000c03020217ba10070c52f20056000000e11910212359551404000000000104000155122504000000002a02000030011831010f7104020e0b03e9360279182af80100ffffff000000000000710200002ac60000532c00000000000060250000000000005ed2000000320000660600000000ea040200c000ef0400000400";
        String cb = "017395501499000000000000000301b3902b069b8ab700bf02e4011820031713564801040000368b030202eee9360280e80c9901000071170000238c0000671600002c5600005258000014b400004f2300001518000063fe000002580000675a000001f4ea5a0003050800154eac0004050b000e66360005040001534400060400012ce40007040000175e00100e000400fa00990107009b009802ee001202011f0013010000140100001502000000160113001702002f001801130019020049eb2a60c002043b60d0014b6050017c60f00128633001636460013c601401006010010061000205b96040012b";
//		
//		byte[] b = Tools.HexString2Bytes(obd);
//		String obdJson = parse0200MessageBody(b);
//		System.out.println(obdJson);
//		System.out.println(obdJson.getBytes().length); // car 520个字节	Json 1300个字节

//		byte[] b2 = Tools.HexString2Bytes(noObd);
//		String noobdJson = parse0200MessageBody(b2);
//		System.out.println(noobdJson);
//		System.out.println(noobdJson.getBytes().length); // car	391个字节		Json 1049个字节
//		
        byte[] b3 = Tools.HexString2Bytes(cb);
        String hexWarnJson = parse0200MessageBody(b3);
        System.out.println(hexWarnJson);
        System.out.println(hexWarnJson.getBytes().length); // car  188个字节		Json 418个字节

//		yuwei // car  244个字节		Json 782个字节
    }
}
