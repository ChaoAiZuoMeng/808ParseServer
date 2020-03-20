package com.chaokong.pojo.trace;

import lombok.Data;

@Data
public class Others {
	private String allMileage="";//车辆行驶里程
	private String nowMileage="";//本次行驶里程
	private String avgOilWear="";//总油耗
	private String nowOilWear="";//当前剩余油耗
	private String oilHeight="";//油位高度
	private String speedThreshold="";//速度阈值
	private String impulseSpeed="";//脉冲速度
}
