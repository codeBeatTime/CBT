package com.codebeattime.service;

import com.codebeattime.dao.MessageDAO;
import com.codebeattime.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    //增加一条消息
    public void addMessage(Message message){
        messageDAO.addMessage(message);
    }

    //查询固定会话下的所有message
    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }
    //查询当前用户下的所有message
    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }
    //返回某个人某个会话下没读的个数
    public int getConversationUnread(int userId,String conversationId){
        return messageDAO.getConversationUnReadCount(userId,conversationId);
    }

}
