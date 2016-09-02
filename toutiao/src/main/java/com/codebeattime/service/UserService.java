package com.codebeattime.service;

import com.codebeattime.dao.StaticImageDAO;
import com.codebeattime.dao.UserDAO;
import com.codebeattime.model.LoginTicket;
import com.codebeattime.model.User;
import com.codebeattime.util.ToutiaoUtil;
import com.codebeattime.dao.LoginTicketDAO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by codebeattime on 2016/7/2.
 */
@Service
public class  UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;
    @Autowired
    private StaticImageDAO staticImageDAO;
    public Map<String,Object> register(String username,String password){
        //错误信息处理
        Map<String,Object>  map = new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msgname","用户名已经被注册");
            return map;
        }
        //注册,实际上就是把信息插入到数据库
        user = new User();
        user.setName(username);
        //为了让密码更加保密
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(staticImageDAO.getRandImage().getUrl());
        //头像就先不处理
        //密码加密 md5加密写在util中 用户的密码+随机的密码
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

          //注册完成后马上登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    //登陆功能
    //登陆功能就是给客户端一个ticket
    public Map<String,Object> login(String username,String password) {
        //错误信息过滤
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }
        //密码验证
        if (!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }
        //登陆成功
        //下发一个ticket
        String ticket = addLoginTicket(user.getId());
        //返回到map中
        map.put("ticket",ticket);
        return map;
    }
    private String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        Date date = new Date();
        date.setTime(date.getTime()+ 1000*3600*24*7);
        loginTicket.setUserId(userId);
        loginTicket.setStatus(0);
        loginTicket.setExpired(date);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }
    public User getUser(int id) {
        return userDAO.selectById(id);
    }
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}
