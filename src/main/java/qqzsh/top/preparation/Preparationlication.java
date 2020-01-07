package qqzsh.top.preparation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author zsh
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class Preparationlication {

    public static void main(String[] args) {
        SpringApplication.run(Preparationlication.class, args);
    }
}