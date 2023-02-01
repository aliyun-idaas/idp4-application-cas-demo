package com.idsmanager.demo.casclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Boot
 */
@SpringBootApplication(scanBasePackages = {"com.idsmanager.demo.casclient"})
public class BootWebApplication {


    /**
     * 注意:  请不要直接  run
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(BootWebApplication.class, args);
    }
}
