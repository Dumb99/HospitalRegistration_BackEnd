package com.dcq.yygh.user.api;

import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.common.utils.AuthContextHolder;
import com.dcq.yygh.model.user.UserInfo;
import com.dcq.yygh.user.service.UserInfoService;
import com.dcq.yygh.vo.user.LoginVo;
import com.dcq.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(value = "用户接口")
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;
    //用户登录接口
    @ApiOperation(value = "用户登录接口")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
        Map<String,Object> map = userInfoService.loginUser(loginVo);
        return Result.ok(map);
    }

    //用户认证接口
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo,
                           HttpServletRequest request){
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }

    //获取用户id信息接口
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }
}
