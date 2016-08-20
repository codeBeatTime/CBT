package com.codebeattime.interceptor;

import com.codebeattime.dao.LoginTicketDAO;
import com.codebeattime.dao.UserDAO;
import com.codebeattime.model.HostHolder;
import com.codebeattime.model.LoginTicket;
import com.codebeattime.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/11.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        if(request.getCookies()!=null){
            //找到cookie中的ticket
            for(Cookie cookie: request.getCookies()) {
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if(ticket!=null){
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            //这个ticket失效
            if(loginTicket==null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus()!=0){
                return true;
            }
            //ticket有效
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        //true表示剩下两个函数会执行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //渲染之前
        if (modelAndView!=null && hostHolder.getUser()!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
           //结束的时候
            hostHolder.clear();
    }
}
