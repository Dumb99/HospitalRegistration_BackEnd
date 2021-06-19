package com.dcq.easyexcel;

import com.alibaba.excel.EasyExcel;

public class testRead {
    public static void main(String[] args) {
        String fileName = "E:\\excel\\01.xlsx";

        EasyExcel.read(fileName,UserData.class,new excelListenner()).sheet().doRead();
    }
}
