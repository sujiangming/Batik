package com.panda.controller;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.panda.pojo.User;
import com.panda.service.UserService;

@Controller
public class UserController {

    /**
     * @category 依赖注入 DI IOC
     */
    @Resource
    UserService userService;// @Resource注解 等价于 userService = new UserServiceImpl();

    /**
     * 登录
     * @param parameter
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("/login")
    public @ResponseBody String login(
            @RequestParam Map<String, String> parameter, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // 1、获取前端传递过来的数据
        String account = parameter.get("account");
        String pwd = parameter.get("pwd");
        if (account != null && pwd != null){
            System.out.println("已接收到前台传来的用户名和密码！！" + "\n "+" 用户名：" + account + "密码："+ pwd);
        }
        // 2、使用前端参数请求查询数据库是否存在当前的用户
        User user = userService.login(account, pwd);
        // 3、判断当前用户是否存在，如果不存在，则返回给前端登录失败信息
        if (null == user) {
            parameter.put("message", "用户名或密码错误~");
            parameter.put("status", "0");
            return JSON.toJSONString(parameter);
        }
        //4、登录成功，需要将用户信息保存在request对象中，
        //   便于在前端页面获取到当前是哪一个用户正在登录系统
        session.setAttribute("userInfo", user);
        parameter.put("message","用户名密码正确！！");

        //5、构造登录成功返回给前端的数据
        parameter.put("message", "登录成功~");
        parameter.put("status", "1");
        //System.out.println("登录成功返回的结果数据：" + JSON.toJSONString(parameter));
        return JSON.toJSONString(parameter);

    }
}
