package com.common.util;

public class ConvertUtil {

    public static int getOffset(Integer pageNum, Integer pageSize){
        return pageSize * (pageNum-1);
    }
}
