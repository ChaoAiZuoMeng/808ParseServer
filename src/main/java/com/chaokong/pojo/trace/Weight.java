package com.chaokong.pojo.trace;

import java.util.List;

import lombok.Data;

@Data
public class Weight {
    private short sensorCount;//传感器启动个数
    private List<SensorsInfo> sensorInfoList;//传感器距离重量信息
    private int[] sensorWarnInfo;//传感器报警信息
}
