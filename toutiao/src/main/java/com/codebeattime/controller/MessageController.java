package com.codebeattime.controller;

import com.codebeattime.model.HostHolder;
import com.codebeattime.model.User;
import com.codebeattime.model.ViewObject;
import com.codebeattime.service.MessageService;
import com.codebeattime.model.Message;
import com.codebeattime.service.UserService;
import com.codebeattime.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
@Controller
public class MessageController {
    private  static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    //增加一条站内信
    @RequestMapping(path={"/msg/addMessage"},method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String addMessage(@RequestParam("fromId")int fromId,@RequestParam("toId") int toId,@RequestParam("content") String content){
        try{
            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            message.setConversationId(fromId<toId ? String.format("%d_%d",fromId,toId) :String.format("%d_%d",toId,fromId));
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(0,message.getConversationId());
        }catch(Exception e){
            logger.error("添加站内信失败"+ e.getMessage());
            return ToutiaoUtil.getJSONString(1,"添加站内信失败");
        }

    }
    //选择详情页
    @RequestMapping(path={"/msg/detail"},method={RequestMethod.GET})
    public String getConversationDetail(Model model,@RequestParam("conversationId") String conversationId){
        try{
            List<Message> conversationList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<>();
            for(Message message : conversationList){
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                User user = userService.getUser(message.getFromId());
                if(user==null) continue;
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch(Exception e){
            logger.error("选择详情页失败" + e.getMessage() );
        }
        return "letterDetail";
    }
    //当前用户下的所有会话
    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("unread", messageService.getConversationUnread(localUserId, msg.getConversationId()));
                conversations.add(vo);

            }
            model.addAttribute("conversations", conversations);
            return "letter";
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }
}
