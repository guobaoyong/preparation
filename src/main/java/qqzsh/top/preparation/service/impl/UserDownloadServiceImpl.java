package qqzsh.top.preparation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.entity.UserDownload;
import qqzsh.top.preparation.respository.UserDownloadRepository;
import qqzsh.top.preparation.service.UserDownloadService;

import javax.transaction.Transactional;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-09-25 21:09
 * @Description 用户下载Service实现类
 */
@Service("userDownloadService")
@Transactional
public class UserDownloadServiceImpl implements UserDownloadService {

    @Autowired
    private UserDownloadRepository userDownloadRepository;

    @Override
    public Integer getCountByUserIdAndArticleId(Integer userId, Integer articleId) {
        return userDownloadRepository.getCountByUserIdAndArticleId(userId, articleId);
    }

    @Override
    public void save(UserDownload userDownload) {
        userDownloadRepository.save(userDownload);
    }

    @Override
    public void deleteByArticleId(Integer articleId) {
        userDownloadRepository.deleteByArticleId(articleId);
    }

}
