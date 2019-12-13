package com.chaokong.pojo;

import java.io.UnsupportedEncodingException;

public interface MessageBody {
	/**
	 * 从kafka接收到的json数据，拼装成消息体发送至kafka，由cgi接收
	 */
	void assembly(String id) throws UnsupportedEncodingException;

}
