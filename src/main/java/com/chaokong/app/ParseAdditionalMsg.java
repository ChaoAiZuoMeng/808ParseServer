package com.chaokong.app;

import com.chaokong.pojo.trace.Trace;
import com.chaokong.tool.MyBuffer;

public interface ParseAdditionalMsg {
	
	void parse(Trace trace, MyBuffer buffer, int length);
	
}
