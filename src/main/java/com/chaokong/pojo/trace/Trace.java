package com.chaokong.pojo.trace;

import com.chaokong.app.ParseAddiE9Msg;
import com.chaokong.app.ParseAddiEAMsg;
import com.chaokong.app.ParseAddiECMsg;
import com.chaokong.app.ParseAddiFAMsg;
import com.chaokong.app.ParseAdditionalMsg;
import com.chaokong.tool.CoordinateTransformUtil;
import com.chaokong.tool.DateUtil;
import com.chaokong.tool.MyBuffer;
import com.chaokong.tool.StringUtil;

import lombok.Data;

@Data
public class Trace {

    private String macID;// 终端ID
    private String uptime;//上传时间
    private String gpsTime;//GPS时间
    private String longitude;//经度
    private String latitude;//纬度
    private String location;//定位
    private String direction;//方向
    private String elevation;//海拔高度
    private String speed;//速度
    private String nowWeight;//当前载重
    private String allWeight;//额定载重
    private String acc;//acc状态
    private int statusMetadata;//状态原始数据

    private Alarm alarm;
    private ObdInfo obdInfo;
    private Weight weight;
    private Others others;


    /**
     * 解析获得基本位置信息
     *
     * @param buffer
     */
    public void parseBasicLocMsg(MyBuffer buffer) {
        // 终端手机号 SimNo BCD[6]
        String obdId = buffer.getBcdString(6);
        // 制造商ID BYTE[5]
//		long manufacturerId = Tools.bytesToLong(buffer.gets(5));

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

        this.setUptime(uptime);
        this.setGpsTime(time);
        this.setMacID(obdId);
        double[] doubles = CoordinateTransformUtil.wgs84togcj02(
                Double.parseDouble(longitude / 1000000.000000 + ""),
                Double.parseDouble(latitude / 1000000.000000 + ""));
        doubles = CoordinateTransformUtil.gcj02tobd09(doubles[0], doubles[1]);
        this.setLatitude(String.format("%.6f", new Object[]{Double.valueOf(doubles[1])}));
        this.setLongitude(String.format("%.6f", new Object[]{Double.valueOf(doubles[0])}));
        this.setSpeed(speed / 10.0 + "");
        this.setDirection(course + "");
//		describeBuilder.setStatusPrimeval(strStatus);
//		alarmbuilder.setAlarmPrimeval(strWarn);
        this.setStatusMetadata(status);
        this.getAlarm().setAlarmMetaData(alarmFlag);
        this.getAlarm().setAlsrmDispose("0");
        this.setElevation(altitude + "");
        this.setAcc(strStatus.substring(31));
        this.setLocation(strStatus.substring(30, 31));
    }

    /**
     * 解析获得附加位置信息
     *
     * @param buffer
     */
    public void parseAddiLocMsg(MyBuffer buffer) {
        ParseAdditionalMsg pam;
        while (buffer.getlength() > 2) {
            int additionId = buffer.getUnsignedByte();
            int additionLength = buffer.getUnsignedByte();

            if (additionLength > 0) {
                if (additionId == 0xE9) {
                    pam = new ParseAddiE9Msg();
                    pam.parse(this, buffer, additionLength);
                } else if (additionId == 0x01) {
                    String allMillage = (buffer.getInt() * 100) + "";
                    this.getOthers().setAllMileage(allMillage);
                } else if (additionId == 0x02) {
                    String oilHeight = (buffer.getShort() / 10.0) + "";
                    this.getOthers().setOilHeight(oilHeight);
                } else if (additionId == 0xEA && additionLength > 4) {
                    pam = new ParseAddiEAMsg();
                    pam.parse(this, buffer, additionLength);
                } else if (additionId == 0xEC || additionId == 0xEB) {
                    pam = new ParseAddiECMsg();
                    pam.parse(this, buffer, additionLength);
                } else if (additionId == 0xFA) {
                    pam = new ParseAddiFAMsg();
                    pam.parse(this, buffer, additionLength);
                } else {
                    buffer.gets(additionLength);
                }
            }
        }
    }
}
