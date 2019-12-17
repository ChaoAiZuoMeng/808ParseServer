package com.chaokong.pojo.response;


import com.chaokong.pojo.MessageBody;
import lombok.Data;

/**
 * 平台通用应答
 *
 * @author huakaiMay
 */
@Data
public class JT_8001 implements MessageBody {
	private short responseMessageSerialNo;
	private short responseMessageId;
	private byte responseResult;



	public String toString() {
		return "应答结果:" + this.responseResult;
	}




	public void assembly(String id) {
		
	}
}


