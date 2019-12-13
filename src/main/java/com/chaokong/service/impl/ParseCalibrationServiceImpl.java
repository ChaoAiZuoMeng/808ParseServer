package com.chaokong.service.impl;

import org.springframework.stereotype.Service;

import com.chaokong.service.IParseCalibrationService;

@Service
public class ParseCalibrationServiceImpl implements IParseCalibrationService{
	
	/**
	 * 解析出上传标定文件的数据(HexString)	生成消息体(byte[])
	 * @param caliHex
	 * @return
	 */
	public byte[] parseCalibrationData(String caliHex) {
		
		System.out.println(caliHex);
		return null;
	}
}
