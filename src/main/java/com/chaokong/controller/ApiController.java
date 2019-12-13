package com.chaokong.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chaokong.service.IParseCalibrationService;
import com.chaokong.util.ParseUtil;


@Controller
@RequestMapping("/api")
public class ApiController {
	
	@Resource
	private IParseCalibrationService iParseCalibrationService;
	
	@ResponseBody
	@RequestMapping(value = "/updateCaliFile",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
	public void receiveCaliFileApi(HttpServletRequest request,HttpServletResponse response) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is;
		int length = 0;
		byte[] b = new byte[1024];
		try {
			is = request.getInputStream();
			while((length = is.read(b)) != -1)
			{
				baos.write(b, 0, length);
			}
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bs = baos.toByteArray();
		String hexData = "";
		try {
			hexData = ParseUtil.binaryToString(bs);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		iParseCalibrationService.parseCalibrationData(hexData);
	}
}
