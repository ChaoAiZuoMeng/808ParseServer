package com.chaokong.pojo;

import lombok.Data;

import java.util.Arrays;

@Data
public class CalibrationData {

    private String simNo;
    private byte[] caliDataBuf;


    @Override
    public String toString() {
        return "CalibrationData [simNo=" + simNo + ", caliDataBuf=" + Arrays.toString(caliDataBuf) + "]";
    }
}
