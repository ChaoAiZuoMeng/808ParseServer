package com.chaokong.pojo;

import java.util.Arrays;

public class CalibrationData {
	
	private String simNo;
	private byte[] caliDataBuf;
	
	public String getSimNo() {
		return simNo;
	}
	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}
	public byte[] getCaliDataBuf() {
		return caliDataBuf;
	}
	public void setCaliDataBuf(byte[] caliDataBuf) {
		this.caliDataBuf = caliDataBuf;
	}
	
	@Override
	public String toString() {
		return "CalibrationData [simNo=" + simNo + ", caliDataBuf=" + Arrays.toString(caliDataBuf) + "]";
	}
}
