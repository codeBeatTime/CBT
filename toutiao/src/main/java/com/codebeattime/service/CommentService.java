package com.codebeattime.service;

import com.codebeattime.dao.CommentDao;
import com.codebeattime.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;
    public int addComment(Comment comment){
        return commentDao.addComment(comment);
    }
    public List<Comment> selectByEntity(int entityId,int entityType){
        return commentDao.selectByEntity(entityId,entityType);
    }
    public void deleteComment(int entityId,int entityType,int status){
        commentDao.updateStatus(entityId,entityType,1);
    }
    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }
}
