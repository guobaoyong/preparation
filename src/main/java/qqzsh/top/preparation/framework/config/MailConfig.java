package qqzsh.top.preparation.framework.config;

import qqzsh.top.preparation.framework.web.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author zsh
 * @site https://www.qqzsh.top
 * @create 2019-12-31 11:54
 * @description 邮件配置类
 */
@Configuration
public class MailConfig {

    @Autowired
    private ConfigService configService;

    /**
     * 获取邮件发送实例
     *
     * @return
     */
    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //指定用来发送Email的邮件服务器主机名
        mailSender.setHost("smtp.qq.com");
        //默认端口，标准的SMTP端口
        mailSender.setPort(587);
        //用户名
        String key = configService.getKey("sys.mail.username");
        String[] split = key.split("----");
        mailSender.setUsername(split[0]);
        //密码
        mailSender.setPassword(split[1]);
        return mailSender;
    }
}
