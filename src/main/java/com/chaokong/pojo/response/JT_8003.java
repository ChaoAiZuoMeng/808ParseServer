package com.chaokong.pojo.response;

import com.chaokong.pojo.MessageBody;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 补传分包请求
 *
 * @author huakaiMay
 */
@Data
public class JT_8003 implements MessageBody {
	private short responseMessageSerialNo;
	private byte repassPacketsCount;
	private ArrayList<Short> repassPacketsNo;

	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("原始消息的第一个分包流水号:" + this.responseMessageSerialNo).append(",重传包数:" + this.repassPacketsCount);

		if (this.repassPacketsNo != null && this.repassPacketsNo.size() > 0) {
			sBuilder.append(",重传包Id列表:");
			for (Iterator<Short> iterator = this.repassPacketsNo.iterator(); iterator.hasNext(); ) {
				short packetNo = (iterator.next()).shortValue();
				sBuilder.append(packetNo).append(",");
			}

		}
		return sBuilder.toString();
	}


	public void assembly(String id) {

	}
}


