package qqzsh.top.preparation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.entity.Comment;
import qqzsh.top.preparation.respository.CommentRepository;
import qqzsh.top.preparation.service.CommentService;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:57
 * @Description 评论Service实现类
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void save(Comment link) {
        commentRepository.save(link);
    }
}
