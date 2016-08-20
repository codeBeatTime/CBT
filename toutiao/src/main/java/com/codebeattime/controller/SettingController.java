package com.codebeattime.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by codebeattime on 2016/6/26.
 */
@Controller
public class SettingController {
    @RequestMapping("/setting")
    @ResponseBody
    public String setting() {
        return "Setting:OK";
    }
}
