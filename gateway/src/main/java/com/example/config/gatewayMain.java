package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * @author cc
 * @date 2023年08月21日 0:14
 */
@SpringBootApplication
@ComponentScan("com.example")
public class gatewayMain {
    private static final Logger LOG = LoggerFactory.getLogger(gatewayMain.class);

    public static void main (String[]args){
        SpringApplication app = new SpringApplication(gatewayMain.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("gateway地址: \thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"));
    }
}
