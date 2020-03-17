package com.chaokong.pojo.trace;

import lombok.Data;

@Data
public class ObdInfo {
	private String allMileage="";//总里程
	private String avgOilWear="";//总油耗
	private String allRuntime="";//总运行时长
	private String allCutOff="";//总熄火时长
	private String allIdling="";//总怠速时长
	private String lastCount="";//最近1秒内采集点个数
	private String lastSpace="";//最近1秒内采集点间隔
	private short[] avgSpeed=new short[0];//每个采集点加速度平均值
	private String maxSpeed="";//最近1秒最大加速度
	private String carVoltage="";//车辆电压
	private String cellVoltage="";//终端内置电池电压
	private String signalIntensity="";//网络信号强度
	private String gpsCount="";//GPS收星数
	private String gpsLocation="";//GPS位置精度
	private String obdRevSpeed="";//OBD转速
	private String obdSpeed="";//OBD车速
	private String obdDoilpre="";//OBD剩余油量百分比
	private String obdCoolTemp="";//OBD冷却液温度
	private String obdAirTemp="";//OBD进气口温度
	private String obdAirAbsPress="";//OBD进气歧管绝对压力
	private String obdAirPress="";//OBD进气压力
	private String obdEnvTemp="";//OBD环境温度
	private String obdExpediteLocation="";//OBD加速踏板位置
	private String obdFuelPress="";//OBD燃油压力
	private String obdFaultStatus="";//OBD故障码状态
	private String obdFaultCount="";//OBD故障码个数
	private String obdAirFlow="";//OBD空气流量
	private String obdClutch="";//OBD离合器开关
	private String obdBrake="";//OBD制动刹车开关
	private String obdPark="";//OBD驻车刹车开关
}
