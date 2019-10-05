package qqzsh.top.preparation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zsh
 * @site https://qqzsh.top
 * @create 2019-10-01 16:17
 * @Description 支付宝配置
 */
@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AliPayConfig {

    // 商户appid
    private String appid;

    // 私钥 pkcs8格式的
    private String rsa_private_key;

    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private String notify_url;

    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    private String return_url;

    // 请求网关地址
    private String url;

    // 编码
    private String charset;

    // 返回格式
    private String format;

    // 支付宝公钥
    private String alipay_public_key;

    // RSA2
    private String signtype;


}
