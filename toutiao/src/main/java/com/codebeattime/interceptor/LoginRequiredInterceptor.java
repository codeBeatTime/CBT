package com.codebeattime.interceptor;

import com.codebeattime.dao.LoginTicketDAO;
import com.codebeattime.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/7/11.
 */
@Component
public class LoginRequiredInterceptor  implements HandlerInterceptor {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(hostHolder.getUser()==null){
            //不过没有当前用户则跳转到首页登陆
            response.sendRedirect("/?pop=1");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
