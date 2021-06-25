package com.dcq.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dcq.yygh.model.user.UserInfo;
import com.dcq.yygh.vo.user.LoginVo;

import java.util.Map;

public interface  UserInfoService extends IService<UserInfo> {
    Map<String, Object> loginUser(LoginVo loginVo);
}
