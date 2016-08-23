package com.codebeattime.controller;

import com.codebeattime.service.UserService;
import com.codebeattime.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Created by Administrator on 2016/7/7.
 */
@Controller
public class LoginController {
    private static  final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    //实现注册功能
    @RequestMapping(value = {"/reg/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username, @RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue = "0") int remberme,
                      HttpServletResponse response){
        try{
            Map<String,Object> map = userService.register(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(remberme>0){
                    cookie.setMaxAge(3600*24*7);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0,"注册成功");
            }else{
                return ToutiaoUtil.getJSONString(1,map);
            }
        }catch(Exception e){
            logger.error("注册异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }
    }

    //实现登陆功能

    @RequestMapping(value = {"/login/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,@RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue = "0") int remberme,
                        HttpServletResponse response){
        try{
            Map<String,Object> map = userService.login(username,password);
            if(map.containsKey("ticket")){
                //把cookie写回给Response
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(remberme>0){
                    cookie.setMaxAge(3600*24*7);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0,"登陆成功");
            }else{
                return ToutiaoUtil.getJSONString(1,map);
            }
        }catch(Exception e){
            logger.error("登陆异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登陆异常");
        }

    }
    @RequestMapping(value = {"/logout/"},method = {RequestMethod.GET,RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }


}
