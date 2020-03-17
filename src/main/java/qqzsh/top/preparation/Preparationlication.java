package qqzsh.top.preparation;

import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author zsh
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@Slf4j
public class Preparationlication {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(Preparationlication.class, args);
        log.info("平台启动成功,耗时："+((System.currentTimeMillis() - start)/1000)+"秒");
    }
}