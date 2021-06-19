package com.dcq.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class testWrite {
    public static void main(String[] args) {
        //构建数据list
        List<UserData> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            UserData userData = new UserData();
            userData.setUuid(i+1);
            userData.setName("lucy"+i);
            list.add(userData);
        }
        //设置文件路劲
        String fileName = "E:\\excel\\01.xlsx";

        EasyExcel.write(fileName,UserData.class).sheet("用户信息")
                .doWrite(list);
    }
}
