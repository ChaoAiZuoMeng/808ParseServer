package com.chaokong.pojo;


import lombok.Data;

/**
 * 数据下行透传
 *
 * @author huakaiMay
 */
@Data
public class JT_8900 implements MessageBody {
	private int count;
	private byte messageType;
	private byte[] messageContent;


	public void assembly(String id) {

	}
}


