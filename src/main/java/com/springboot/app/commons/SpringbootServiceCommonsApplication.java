package com.springboot.app.commons;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
// para que la dependencia de spring-boot-jpa no nos pida configurar una BD
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class SpringbootServiceCommonsApplication {
}
