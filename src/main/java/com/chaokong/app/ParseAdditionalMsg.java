package com.chaokong.app;

import com.chaokong.tool.MyBuffer;

import java.util.Map;

public interface ParseAdditionalMsg {
	
	Map parse(MyBuffer buffer, int length);
	
}
