package qqzsh.top.preparation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zsh
 * @site qqzsh.top
 * @create 2019-09-17 15:58
 * @description
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${userImageFilePath}")
    private String userImageFilePath;

    @Value("${articleImageFilePath}")
    private String articleImageFilePath;

    @Value("${noticeImageFilePath}")
    private String noticeImageFilePath;

    /**
     * 静态资源的加载和Swagger2配置
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //静态css、js、img
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        //用户头像
        registry.addResourceHandler("/userImage/**")
                .addResourceLocations("file:" + userImageFilePath);
        //文章、通知广告图片
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:" + articleImageFilePath)
                .addResourceLocations("file:" + noticeImageFilePath);;
    }
}
