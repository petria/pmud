package org.freakz.pmud.pmudclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class PmudclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmudclientApplication.class, args);
    }

}
