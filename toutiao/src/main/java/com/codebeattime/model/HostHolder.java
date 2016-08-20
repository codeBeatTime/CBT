package com.codebeattime.model;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/7/11.
 */
@Component
public class HostHolder {
    //线程本地变量，每一个线程拥有各自的users
    private  static ThreadLocal<User> users = new ThreadLocal<User>();
    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
