package com.chaokong.pojo.response;


import com.chaokong.pojo.MessageBody;
import lombok.Data;

/**
 * 终端注册应答
 *
 * @author huakaiMay
 */
@Data
public class JT_8100 implements MessageBody {
	private short registerResponseMessageSerialNo;
	private byte registerResponseResult;
	private String registerNo;


	public String toString(String id) {
		StringBuilder sb = new StringBuilder();
		sb.append("注册结果:").append(this.registerResponseResult).append(",鉴权码:").append(this.registerNo);
		return sb.toString();
	}


	public void assembly(String id) {

	}
}


