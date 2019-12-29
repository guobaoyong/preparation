package qqzsh.top.preparation.controller.admin;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import qqzsh.top.preparation.entity.Notice;
import qqzsh.top.preparation.service.NoticeService;
import qqzsh.top.preparation.util.DateUtil;
import qqzsh.top.preparation.util.RedisUtil;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2019-12-27 17:26
 * @description 通知广告管理
 */
@Controller
@RequestMapping("/admin/notice")
public class NoticeAdminController {

    @Autowired
    private NoticeService noticeService;

    @Value("${noticeImageFilePath}")
    private String noticeImageFilePath;

    @Resource
    private RedisUtil<Notice> noticeRedisUtil;

    @ResponseBody
    @RequiresPermissions(value = {"分页查询通知广告"})
    @RequestMapping("/list")
    public Map<String, Object> list(Notice notice,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "limit", required = false) Integer limit) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<Notice> noticeList = noticeService.list(notice, page, limit, Sort.Direction.DESC, "id");
        Long total = noticeService.getTotal(notice);
        resultMap.put("code", 0);
        resultMap.put("count", total);
        resultMap.put("data", noticeList);
        return resultMap;
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"删除通知广告"})
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //删除通知广告信息
        noticeService.delete(id);
        // 更新缓存
        updateRedis();
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 多选删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/deleteSelected")
    @RequiresPermissions(value = {"删除通知广告"})
    public Map<String, Object> deleteSelected(String ids) throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            // 删除通知广告
            noticeService.delete(Integer.parseInt(idsStr[i]));
        }
        // 更新缓存
        updateRedis();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        return resultMap;
    }

    @RequiresPermissions(value = {"查看通知广告内容"})
    @GetMapping("/show")
    public ModelAndView show(@RequestParam(name = "id",required = false) Integer id) {
        // 根据id查找通知广告
        Notice notice = noticeService.findById(id);
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "通知广告内容");
        mav.addObject("content",notice.getContent());
        mav.setViewName("admin/shownotice");
        return mav;
    }

    /**
     * Layui编辑器图片上传处理
     *
     * @param file 待上传文件
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/uploadImage")
    public Map<String, Object> uploadImage(MultipartFile file) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (!file.isEmpty()) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 新文件名
            String newFileName = DateUtil.getCurrentDateStr() + suffixName;
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(noticeImageFilePath + DateUtil.getCurrentDatePath() + newFileName));
            map.put("code", 0);
            map.put("msg", "上传成功");
            Map<String, Object> map2 = new HashMap<>();
            map2.put("src", "/image/" + DateUtil.getCurrentDatePath() + newFileName);
            map2.put("title", newFileName);
            map.put("data", map2);
        }
        return map;
    }

    @RequiresPermissions(value = {"通知广告增加修改页"})
    @GetMapping("/toAddPage")
    public ModelAndView toAddPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "通知广告增加页面");
        mav.setViewName("admin/addNotice");
        return mav;
    }

    @RequiresPermissions(value = {"通知广告增加修改"})
    @PostMapping("/add")
    public ModelAndView add(Notice notice){
        notice.setPublishDate(new Date());
        noticeService.save(notice);
        // 更新缓存
        updateRedis();
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", "发布成功");
        mav.setViewName("admin/addNoticeSuccess");
        return mav;
    }

    /**
     * 更新缓存的通知和广告
     */
    void updateRedis(){
        //加载通知实体到redis
        noticeRedisUtil.del("notice");
        Notice newOne = noticeService.getNewOne();
        noticeRedisUtil.set("notice",newOne);
        //加载广告实体到redis
        noticeRedisUtil.del("ad");
        Notice newOneAD = noticeService.getNewOneAD();
        noticeRedisUtil.set("ad",newOneAD);
    }
}
