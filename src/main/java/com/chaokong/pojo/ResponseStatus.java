package com.chaokong.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

// controller 接口响应
@Data
@AllArgsConstructor
public class ResponseStatus {
	
	private int code;
	private String describe;
}
