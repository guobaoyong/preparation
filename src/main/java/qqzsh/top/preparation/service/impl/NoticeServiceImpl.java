package qqzsh.top.preparation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.entity.Notice;
import qqzsh.top.preparation.entity.Order;
import qqzsh.top.preparation.respository.NoticeRepository;
import qqzsh.top.preparation.service.NoticeService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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

    @Override
    public List<Notice> list(Notice notice, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = new PageRequest(page - 1, pageSize, direction, properties);
        Page<Notice> pageNotice = noticeRepository.findAll(new Specification<Notice>() {

            @Override
            public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (notice != null) {
                    if (notice.getName() != null && notice.getName() != "") {
                        predicate.getExpressions().add(cb.like(root.get("name"), "%" + notice.getName().trim() + "%"));
                    }
                    if (notice.getType() != null && notice.getType() != "") {
                        predicate.getExpressions().add(cb.equal(root.get("type"), notice.getType()));
                    }
                }
                return predicate;
            }
        }, pageable);
        return pageNotice.getContent();
    }

    @Override
    public Long getTotal(Notice notice) {
        Long count = noticeRepository.count(new Specification<Notice>() {

            @Override
            public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (notice != null) {
                    if (notice.getName() != null && notice.getName() != "") {
                        predicate.getExpressions().add(cb.equal(root.get("name"), "%" + notice.getName().trim() + "%"));
                    }
                    if (notice.getType() != null && notice.getType() != "") {
                        predicate.getExpressions().add(cb.equal(root.get("type"), notice.getType()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public void delete(Integer id) {
        noticeRepository.delete(id);
    }

    @Override
    public Notice findById(Integer id) {
        return noticeRepository.findOne(id);
    }

    @Override
    public void save(Notice notice) {
        noticeRepository.save(notice);
    }


}
