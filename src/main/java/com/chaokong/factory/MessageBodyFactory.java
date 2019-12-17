package com.chaokong.factory;

import com.chaokong.pojo.*;
import com.chaokong.pojo.response.*;

public class MessageBodyFactory {


	/**
	 * 根据id创建实例
	 * @param id messageId
	 * @return
	 */
	public static MessageBody getInstanceByMessageId(String id) {

		switch (id) {
			case "8001":
				return new JT_8100();
			case "8003":
				return new JT_8003();
			case "8100":
				return new JT_8100();
			case "8201":
				return new JT_8201();
			case "8300":
				return new JT_8300();
			case "8900":
				return new JT_8900();
			default:
				throw new RuntimeException("NoSuchInstanceException");
		}

	}


	public static MessageBody getInstance(MessageBody messageBody) {
		return messageBody;
	}
}
