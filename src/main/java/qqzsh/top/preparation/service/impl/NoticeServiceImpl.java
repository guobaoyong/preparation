package qqzsh.top.preparation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.entity.Notice;
import qqzsh.top.preparation.respository.NoticeRepository;
import qqzsh.top.preparation.service.NoticeService;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2019-12-27 11:11
 * @description 通知接口实现类
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public Notice getNewOne() {
        return noticeRepository.getNewOne();
    }

    @Override
    public Notice getNewOneAD() {
        return noticeRepository.getNewOneAD();
    }
}
