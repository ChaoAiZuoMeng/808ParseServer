package com.chaokong.pojo.trace;

import lombok.Data;

@Data
public class Alarm {
    private String alarmSource;//报警来源
    private String alsrmDispose;//是否处理	默认赋值为"0"，0：未处理1：已处理
    private int alarmMetaData;//报警原始数据
}
